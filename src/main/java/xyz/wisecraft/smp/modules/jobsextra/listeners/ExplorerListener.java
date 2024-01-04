package xyz.wisecraft.smp.modules.jobsextra.listeners;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modules.jobsextra.JobsExtrasModule;
import xyz.wisecraft.smp.modules.jobsextra.event.ExplorerObtainsElytraEvent;
import xyz.wisecraft.smp.modules.jobsextra.storage.JobsStorage;

import static xyz.wisecraft.smp.modules.jobsextra.storage.JobsStorage.getJobLevel;
import static xyz.wisecraft.smp.modules.jobsextra.util.UtilCommon.sendNoMessage;

public class ExplorerListener implements Listener {
    private final Job explorer;
    private final NamespacedKey elytraKey = new NamespacedKey("jobsextra", "elytra");

    public ExplorerListener(JobsExtrasModule module) {
        explorer = JobsStorage.setJobLevel(module, "Explorer");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onExplorerPickup(PlayerAttemptPickupItemEvent e) {
        Player p = explorerChecks(e.getPlayer(), e.getItem());
        if (p == null) return;

        JobsPlayer pJobs = Jobs.getPlayerManager().getJobsPlayer(p);
        Item item = e.getItem();

        if (item.getPersistentDataContainer().get(elytraKey, PersistentDataType.STRING) != null) return;

        if (pJobs.isInJob(explorer) && pJobs.getJobProgression(explorer).getLevel() >= getJobLevel(explorer))
            callElytraObtainedEvent(e, p);
        else
            e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEndContainer(InventoryClickEvent e) {

        Player p = explorerChecks(e.getWhoClicked());
        if (p == null) return;
        Inventory clickedInv = e.getClickedInventory();
        if (clickedInv == null) {
            return;
        } else if (e.getCurrentItem() != null && !clickedInv.equals(p.getInventory()) && e.getCurrentItem().getType().equals(Material.ELYTRA)) {
            e.setResult(Event.Result.DENY);
            e.setCancelled(true);
            return;
        }

        if (clickedInv.equals(p.getInventory()) || !e.getCursor().getType().equals(Material.ELYTRA)) return;
        e.setResult(Event.Result.DENY);
        e.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEndContainer(InventoryDragEvent e) {
        Player p = explorerChecks(e.getWhoClicked());
        if (p == null) return;

        Inventory clickedInv = e.getInventory();

        if (clickedInv.equals(p.getInventory()) || !e.getOldCursor().getType().equals(Material.ELYTRA)) return;

        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onExplorerBreak(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof ItemFrame)) return;
        Player p = explorerChecks(e.getDamager(), ((ItemFrame) e.getEntity()).getItem());
        if (p == null) return;


        JobsPlayer pJobs = Jobs.getPlayerManager().getJobsPlayer(p);
        boolean isInJob = pJobs.isInJob(explorer);
        int lvl = getJobLevel(explorer);

        if (!(isInJob && pJobs.getJobProgression(explorer).getLevel() >= lvl)) {
            e.setCancelled(true);
            sendNoMessage(p, explorer, "obtain elytras from the end!");
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onElytraDrop(PlayerDropItemEvent e) {
        if (explorerChecks(e.getPlayer(), e.getItemDrop()) == null) return;

        @NotNull Item droppedItem = e.getItemDrop();
        droppedItem.getPersistentDataContainer().set(elytraKey, PersistentDataType.STRING, "bypass explorer restriction");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onElytraDeath(PlayerDeathEvent e) { // I Forgot why this is necessary. I guess it's nice if someone just dies and never returns or a friend is nearby
        Player p = explorerChecks(e.getEntity());

        if (p == null) return;

        e.getDrops().removeIf(item -> {
            if (item.getType().equals(Material.ELYTRA)) {
                Location loc = p.getLocation();

                Item droppedItem = p.getWorld().spawn(loc, Item.class, itemEntity -> itemEntity.setItemStack(item));
                droppedItem.getPersistentDataContainer().set(elytraKey, PersistentDataType.STRING, "bypass explorer restriction");
                return true;
            }
            return false;
        });

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onElytraCraft(CraftItemEvent e) {
        Player p = explorerChecks(e.getWhoClicked(), e.getRecipe().getResult());

        if (p == null) return;

        JobsPlayer pJobs = Jobs.getPlayerManager().getJobsPlayer(p);

        boolean isInJob = pJobs.isInJob(explorer);

        if (isInJob && pJobs.getJobProgression(explorer).getLevel() >= getJobLevel(explorer)) {
            callElytraObtainedEvent(e, p);
        } else {
            e.setCancelled(true);
            sendNoMessage(p, explorer, "craft elytras!");
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

    /**
     * Call an event when a player obtains an elytra
     * @param e Event to cancel if this event is cancelled
     * @param p The player to pass to the event
     */
    private void callElytraObtainedEvent(Cancellable e, Player p) {
        ExplorerObtainsElytraEvent event = new ExplorerObtainsElytraEvent(p);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) e.setCancelled(true);
    }

}
