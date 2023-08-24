package xyz.wisecraft.smp.modules.jobsextra.listeners;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobsPlayer;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import xyz.wisecraft.smp.modules.cropharvester.events.PrepareCropHarvestEvent;

import java.util.ArrayList;

/**
 * This class is for extra jobs features.
 */
public class JobsFeatureListener implements org.bukkit.event.Listener {

    private final Job miner;
    private final Job blacksmith;
    private final Job explorer;
    private final Job farmer;
    private final ArrayList<Material> blacksmithCrafts = new ArrayList<>();
    public JobsFeatureListener() {

        this.miner = getSpecificJob("Miner");
        this.blacksmith = getSpecificJob("Blacksmith");
        this.explorer = getSpecificJob("Explorer");
        this.farmer = getSpecificJob("Farmer");

        blacksmithCrafts.add(Material.DIAMOND_AXE);
        blacksmithCrafts.add(Material.DIAMOND_HOE);
        blacksmithCrafts.add(Material.DIAMOND_SHOVEL);
        blacksmithCrafts.add(Material.DIAMOND_SWORD);
        blacksmithCrafts.add(Material.DIAMOND_HELMET);
        blacksmithCrafts.add(Material.DIAMOND_CHESTPLATE);
        blacksmithCrafts.add(Material.DIAMOND_LEGGINGS);
        blacksmithCrafts.add(Material.DIAMOND_BOOTS);

    }

    @EventHandler
    public void onMinerBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();

        // Check if player is in miner
        if (Jobs.getPlayerManager().getJobsPlayer(p).isInJob(miner))
            return;

        Material type = e.getBlock().getType();
        Material debris = Material.ANCIENT_DEBRIS;
        e.getBlock().getWorld();

        if (type.equals(debris))
            e.setCancelled(true);

    }

    @EventHandler
    public void onBlacksmithCraft(CraftItemEvent e) {
        Player p = e.getInventory().getHolder() instanceof Player ? (Player) e.getInventory().getHolder() : null;

        if (p == null || Jobs.getPlayerManager().getJobsPlayer(p).isInJob(blacksmith)) return;

        Material craftType = e.getRecipe().getResult().getType();

        if (blacksmithCrafts.contains(craftType)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplorerPickup(PlayerAttemptPickupItemEvent e) {
        Player p = e.getPlayer();

        JobsPlayer pJobs = Jobs.getPlayerManager().getJobsPlayer(p);

        if (p.getWorld().toString().contains("end") && !pJobs.isInJob(explorer)) {
            if (e.getItem().getItemStack().getType().equals(Material.ELYTRA)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onExplorerBreak(EntityDamageByEntityEvent e) {
        Player p = e.getDamager() instanceof Player ? (Player) e.getDamager() : null;
        ItemFrame itemFrame = e.getEntity() instanceof ItemFrame ? (ItemFrame) e.getEntity() : null;

        if (itemFrame == null || p == null) return;

        JobsPlayer pJobs = Jobs.getPlayerManager().getJobsPlayer(p);

        if (itemFrame.getWorld().toString().contains("end") && !pJobs.isInJob(explorer)) {
            e.setCancelled(true);
            p.sendMessage("You need to have the job \"explorer\" to get Elytras from the end!");
        }
    }

    @EventHandler
    public void onHarvestCrop(PrepareCropHarvestEvent e) {
        Player p = e.getPlayer();

        JobsPlayer pJobs = Jobs.getPlayerManager().getJobsPlayer(p);

        if (!pJobs.isInJob(farmer)) {
            e.setCancelled(true);
        }
    }


    // Methods and NOT events from here on out
    private Job getSpecificJob(String name) {
        for (Job job : Jobs.getJobs()) {
            if (job.getName().equalsIgnoreCase(name))
                return job;
        }
        return null;
    }

}
