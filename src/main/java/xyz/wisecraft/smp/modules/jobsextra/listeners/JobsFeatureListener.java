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
import xyz.wisecraft.smp.modules.jobsextra.JobsExtrasModule;
import xyz.wisecraft.smp.storage.OtherStorage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is for extra jobs features.
 */
public class JobsFeatureListener implements org.bukkit.event.Listener {

    private final HashMap<Job, Integer> jobLevels = new HashMap<>();
    private final Job miner;
    private final Job blacksmith;
    private final Job farmer;

    public JobsFeatureListener(JobsExtrasModule module) {

        this.miner = module.getSpecificJob("Miner");
        jobLevels.put(miner, module.getPlugin().getConfig().getInt("JOBS_SETTINGS.DEFAULT_ABILITY_LEVEL"));
        this.blacksmith = module.getSpecificJob("Blacksmith");
        jobLevels.put(blacksmith, module.getPlugin().getConfig().getInt("JOBS_SETTINGS.DEFAULT_ABILITY_LEVEL"));
        this.farmer = module.getSpecificJob("Farmer");
        jobLevels.put(farmer, module.getPlugin().getConfig().getInt("JOBS_SETTINGS.DEFAULT_ABILITY_LEVEL"));

        OtherStorage.addBlacksmithCraft(Material.DIAMOND_AXE);
        OtherStorage.addBlacksmithCraft(Material.DIAMOND_HOE);
        OtherStorage.addBlacksmithCraft(Material.DIAMOND_SHOVEL);
        OtherStorage.addBlacksmithCraft(Material.DIAMOND_SWORD);
        OtherStorage.addBlacksmithCraft(Material.DIAMOND_HELMET);
        OtherStorage.addBlacksmithCraft(Material.DIAMOND_CHESTPLATE);
        OtherStorage.addBlacksmithCraft(Material.DIAMOND_LEGGINGS);
        OtherStorage.addBlacksmithCraft(Material.DIAMOND_BOOTS);

    }

    @EventHandler
    public void onMinerBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Job pJob = miner;
        JobsPlayer pJobs = Jobs.getPlayerManager().getJobsPlayer(p);

        // Check if player is in miner
        if (pJobs.isInJob(pJob) && pJobs.getJobProgression(pJob).getLevel() >= jobLevels.get(pJob))
            return;

        Material type = e.getBlock().getType();
        Material debris = Material.ANCIENT_DEBRIS;

        if (type.equals(debris))
            e.setCancelled(true);

    }

    @EventHandler
    public void onBlacksmithCraft(CraftItemEvent e) {
        Player p = e.getInventory().getHolder() instanceof Player ? (Player) e.getInventory().getHolder() : null;
        Job pJob = blacksmith;
        JobsPlayer pJobs = Jobs.getPlayerManager().getJobsPlayer(p);

        ArrayList<Material> blacksmithCrafts = OtherStorage.getBlacksmithCrafts();

        if (p == null || pJobs.isInJob(pJob) &&
                pJobs.getJobProgression(pJob).getLevel() >= jobLevels.get(pJob)) return;

        Material craftType = e.getRecipe().getResult().getType();

        if (blacksmithCrafts.contains(craftType)) {
            e.setCancelled(true);
            p.sendMessage("You need to have the job \"blacksmith\" to craft diamond gear! " +
                    "Except for diamond pickaxe and diamond chestplate");
        }
    }

    @EventHandler
    public void onHarvestCrop(PrepareCropHarvestEvent e) {
        Player p = e.getPlayer();
        Job pJob = farmer;
        JobsPlayer pJobs = Jobs.getPlayerManager().getJobsPlayer(p);
        boolean isInJob = pJobs.isInJob(pJob);

        if (!(isInJob && pJobs.getJobProgression(pJob).getLevel() >= jobLevels.get(pJob))) {
            e.setCancelled(true);
        }
    }


    // Methods and NOT events from here on out

}
