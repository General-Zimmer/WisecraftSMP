package xyz.wisecraft.smp.modules.jobsextra.listeners;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;

import java.util.ArrayList;

/**
 * This class is for extra jobs features.
 */
public class JobsFeatureListener implements org.bukkit.event.Listener {

    private final Job miner;
    private final Job blacksmith;
    private Job explorer;
    private final ArrayList<Material> blacksmithCrafts = new ArrayList<>();
    public JobsFeatureListener() {

        this.miner = getSpecificJob("Miner");
        this.blacksmith = getSpecificJob("Blacksmith");
        this.explorer = getSpecificJob("Explorer");

        blacksmithCrafts.add(Material.DIAMOND_AXE);
        blacksmithCrafts.add(Material.DIAMOND_HOE);
        blacksmithCrafts.add(Material.DIAMOND_SHOVEL);
        blacksmithCrafts.add(Material.DIAMOND_SWORD);
        blacksmithCrafts.add(Material.DIAMOND_BOOTS);
        blacksmithCrafts.add(Material.DIAMOND_CHESTPLATE);
        blacksmithCrafts.add(Material.DIAMOND_HELMET);
        blacksmithCrafts.add(Material.DIAMOND_LEGGINGS);


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

        if (p == null || !Jobs.getPlayerManager().getJobsPlayer(p).isInJob(blacksmith)) return;

        Material craftType = e.getRecipe().getResult().getType();

        if (blacksmithCrafts.contains(craftType)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplorerGetElytra(PlayerAttemptPickupItemEvent e) {
        Player p = e.getPlayer();

        if (!Jobs.getPlayerManager().getJobsPlayer(p).isInJob(explorer)) {
            if (e.getItem().getItemStack().getType().equals(Material.ELYTRA)) {
                e.setCancelled(true);
            }
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
