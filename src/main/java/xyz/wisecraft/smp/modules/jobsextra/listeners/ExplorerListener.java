package xyz.wisecraft.smp.modules.jobsextra.listeners;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import xyz.wisecraft.smp.modules.jobsextra.JobsExtrasModule;
import xyz.wisecraft.smp.modules.jobsextra.event.ExplorerObtainsElytraEvent;
import xyz.wisecraft.smp.modules.jobsextra.storage.JobsStorage;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class ExplorerListener implements Listener {
    private final HashMap<Job, Integer> jobLevels = new HashMap<>();
    private final Job explorer;

    public ExplorerListener(JobsExtrasModule module) {
        int defaultLevel = module.plugin.getConfig().getInt("JOBS_SETTINGS.DEFAULT_ABILITY_LEVEL");
        this.explorer = module.getSpecificJob("Explorer");
        jobLevels.put(explorer, module.plugin.getConfig().getInt("JOBS_SETTINGS.EXPLORER_ABILITY_LEVEL", defaultLevel));
    }

    @EventHandler
    public void onExplorerPickup(PlayerAttemptPickupItemEvent e) {
        Player p = explorerChecks(e.getPlayer(), e.getItem());
        if (p == null) return;

        JobsPlayer pJobs = Jobs.getPlayerManager().getJobsPlayer(p);
        HashMap<UUID, Date> elytraDrops = JobsStorage.getElytraDrop();
        Item item = e.getItem();
        UUID id = item.getUniqueId();


        if (elytraDrops.containsKey(id)) {
            elytraDrops.remove(id);
            return;
        }

        if ((!(pJobs.isInJob(explorer) && pJobs.getJobProgression(explorer).getLevel() >= jobLevels.get(explorer))))
            e.setCancelled(true);
    }

    @EventHandler
    public void onExplorerBreak(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof ItemFrame)) return;
        Player p = explorerChecks(e.getDamager(), ((ItemFrame) e.getEntity()).getItem());
        if (p == null) return;


        JobsPlayer pJobs = Jobs.getPlayerManager().getJobsPlayer(p);
        boolean isInJob = pJobs.isInJob(explorer);
        int lvl = jobLevels.get(explorer);

        if (isInJob && pJobs.getJobProgression(explorer).getLevel() >= lvl) {
            callElytraObtainedEvent(e, p);
        } else {
            e.setCancelled(true);
            p.sendMessage("You need to have the job \"explorer\" and be level " + lvl +"+ to get Elytras from the end!");
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onElytraDrop(PlayerDropItemEvent e) {
        if (explorerChecks(e.getPlayer(), e.getItemDrop()) == null) return;

        UUID uuid = e.getItemDrop().getUniqueId();

        JobsStorage.addElytraDrop(uuid, new Date());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onElytraDeath(PlayerDeathEvent e) {
        Player p = explorerChecks(e.getEntity());

        if (p == null) return;

        p.getInventory().contains(Material.ELYTRA);
        final HashSet<ItemStack> items = new HashSet<>();

        e.getDrops().removeIf(item -> {
            if (item.getType().equals(Material.ELYTRA)) {
                items.add(item);
                return true;
            }
            return false;
        });

        Location loc = p.getLocation();

        items.forEach(item -> {
            Item floutyItem = p.getWorld().spawn(loc, Item.class, itemEntity -> itemEntity.setItemStack(item));

            JobsStorage.addElytraDrop(floutyItem.getUniqueId(), new Date());
        });
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onElytraCraft(CraftItemEvent e) {
        Player p = explorerChecks(e.getWhoClicked(), e.getRecipe().getResult());

        if (p == null) return;

        JobsPlayer pJobs = Jobs.getPlayerManager().getJobsPlayer(p);

        boolean isInJob = pJobs.isInJob(explorer);

        if (isInJob && pJobs.getJobProgression(explorer).getLevel() >= jobLevels.get(explorer)) {
            callElytraObtainedEvent(e, p);
        } else {
            e.setCancelled(true);
            p.sendMessage("You need to have the job \"explorer\" and be level " + jobLevels.get(explorer) + "+ to craft elytras!");
        }
    }

    // helper methods, NO EVENTS!

    /**
     * Checks if itemstack is elytra
     * @param maybePlayer The entity that might be a player
     * @param item The item that might be an elytra
     * @return The player if the player is in the end and the item is an elytra, null otherwise.
     */
    private Player explorerChecks(Entity maybePlayer, ItemStack item) {

        ItemStack itemFrame = item.getType().equals(Material.ELYTRA) ? item : null;
        // null checks
        if (itemFrame == null) return null;

        return explorerChecks(maybePlayer);
    }

    /**
     * Checks if item entity is in the end
     * @param maybePlayer The entity that might be a player
     * @param item The item entity that might be in the end
     * @return The player if the player is in the end and the item is in the end, null otherwise.
     */
    private Player explorerChecks(Entity maybePlayer, Item item) {
        if (!item.getWorld().toString().contains("end")) return null;

        return explorerChecks(maybePlayer, item.getItemStack());
    }

    /**
     * Checks if entity is a player and if the player is in the end
     * @param maybePlayer The entity that might be a player
     * @return The player if the player is in the end, null otherwise.
     */
    private Player explorerChecks(Entity maybePlayer) {
        Player player = maybePlayer instanceof Player ? (Player) maybePlayer : null;

        if (player == null || !player.getWorld().toString().contains("end")) return null;

        return player;
    }

    private void callElytraObtainedEvent(Cancellable e, Player p) {
        ExplorerObtainsElytraEvent event = new ExplorerObtainsElytraEvent(p);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) e.setCancelled(true);
    }

}
