package xyz.wisecraft.smp.modules.jobsextra.listeners;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import xyz.wisecraft.smp.modules.jobsextra.JobsExtrasModule;
import xyz.wisecraft.smp.modules.jobsextra.event.ExplorerObtainsElytraEvent;
import xyz.wisecraft.smp.modules.jobsextra.storage.JobsStorage;
import xyz.wisecraft.smp.util.UtilCommon;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class ExplorerListener implements Listener {
    private final HashMap<Job, Integer> jobLevels = new HashMap<>();
    private final Job explorer;

    public ExplorerListener(JobsExtrasModule module) {
        this.explorer = module.getSpecificJob("Explorer");
        jobLevels.put(explorer, module.getPlugin().getConfig().getInt("JOBS_SETTINGS.DEFAULT_ABILITY_LEVEL"));
    }

    @EventHandler
    public void onExplorerPickup(PlayerAttemptPickupItemEvent e) {
        Player p = e.getPlayer();
        Job pJob = explorer;
        JobsPlayer pJobs = Jobs.getPlayerManager().getJobsPlayer(p);
        HashMap<UUID, Date> elytraDrops = JobsStorage.getElytraDrop();
        Item item = e.getItem();

        if (!item.getItemStack().getType().equals(Material.ELYTRA)) return;

        if (elytraDrops.containsKey(item.getUniqueId())) {
            Date date = elytraDrops.get(item.getUniqueId());

            if (UtilCommon.calcCurrentSeconds(date) < 60*5) {
                elytraDrops.remove(item.getUniqueId());
                return;
            }
        }

        if ((p.getWorld().toString().contains("end") && !(pJobs.isInJob(pJob) && pJobs.getJobProgression(pJob).getLevel() >= jobLevels.get(pJob))))
            e.setCancelled(true);

    }

    @EventHandler
    public void onExplorerBreak(EntityDamageByEntityEvent e) {
        Player p = e.getDamager() instanceof Player ? (Player) e.getDamager() : null;
        ItemFrame itemFrame = e.getEntity() instanceof ItemFrame ? (ItemFrame) e.getEntity() : null;

        if (itemFrame == null || p == null || !itemFrame.getWorld().toString().contains("end")) return;

        JobsPlayer pJobs = Jobs.getPlayerManager().getJobsPlayer(p);
        boolean isInJob = pJobs.isInJob(explorer);
        int lvl = jobLevels.get(explorer);

        if (!isInJob) {
            e.setCancelled(true);
            p.sendMessage("You need to have the job \"explorer\" and be level " + lvl + " or higher to get Elytras from the end!");
        } else if (pJobs.getJobProgression(explorer).getLevel() >= lvl){
            callElytraObtainedEvent(e, p);
        }
    }

    @EventHandler
    public void onElytraDrop(PlayerDropItemEvent e) {
        UUID uuid = e.getItemDrop().getUniqueId();

        JobsStorage.addElytraDrop(uuid, new Date());
    }

    @EventHandler
    public void onElytraCraft(CraftItemEvent e) {
        Player p = e.getInventory().getHolder() instanceof Player ? (Player) e.getInventory().getHolder() : null;

        if (p == null) return;

        JobsPlayer pJobs = Jobs.getPlayerManager().getJobsPlayer(p);
        Material craftType = e.getRecipe().getResult().getType();
        boolean isInJob = pJobs.isInJob(explorer);

        if (craftType.equals(Material.ELYTRA) && !isInJob) {
            e.setCancelled(true);
            p.sendMessage("You need to have the job \"explorer\" to craft elytras!");
        } else if (pJobs.getJobProgression(explorer).getLevel() >= jobLevels.get(explorer)) {
            callElytraObtainedEvent(e, p);
        }
    }

    private void callElytraObtainedEvent(Cancellable e, Player p) {
        ExplorerObtainsElytraEvent event = new ExplorerObtainsElytraEvent(p);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) e.setCancelled(true);
    }

}
