package xyz.wisecraft.smp.modules.jobsextra.listeners;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobsPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.SmithingRecipe;
import xyz.wisecraft.smp.modules.cropharvester.events.PrepareCropHarvestEvent;
import xyz.wisecraft.smp.modules.heirloom.recipes.HeirloomRunes;
import xyz.wisecraft.smp.modules.heirloom.recipes.SmithRecipes;
import xyz.wisecraft.smp.modules.heirloom.util.UtilRandom;
import xyz.wisecraft.smp.modules.jobsextra.JobsExtrasModule;
import xyz.wisecraft.smp.modules.jobsextra.storage.JobsStorage;

import java.util.ArrayList;
import java.util.List;

import static xyz.wisecraft.smp.modules.jobsextra.storage.JobsStorage.getJobLevel;
import static xyz.wisecraft.smp.modules.jobsextra.util.UtilCommon.sendNoMessage;

/**
 * This class is for extra jobs features.
 */
public class JobsFeatureListener implements org.bukkit.event.Listener {

    private final Job miner;
    private final Job blacksmith;
    private final Job farmer;
    private final Job runesmith;

    public JobsFeatureListener(JobsExtrasModule module) {

        this.runesmith = JobsStorage.setJobLevel(module, "Runesmith");
        this.farmer = JobsStorage.setJobLevel(module, "Farmer");
        this.miner = JobsStorage.setJobLevel(module, "Miner");
        this.blacksmith = JobsStorage.setJobLevel(module, "Blacksmith");

        JobsStorage.addBlacksmithCraft(Material.DIAMOND_AXE);
        JobsStorage.addBlacksmithCraft(Material.DIAMOND_HOE);
        JobsStorage.addBlacksmithCraft(Material.DIAMOND_SHOVEL);
        JobsStorage.addBlacksmithCraft(Material.DIAMOND_SWORD);
        JobsStorage.addBlacksmithCraft(Material.DIAMOND_HELMET);
        JobsStorage.addBlacksmithCraft(Material.DIAMOND_CHESTPLATE);
        JobsStorage.addBlacksmithCraft(Material.DIAMOND_LEGGINGS);
        JobsStorage.addBlacksmithCraft(Material.DIAMOND_BOOTS);

    }

    @EventHandler
    public void onMinerBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Job pJob = miner;
        JobsPlayer pJobs = Jobs.getPlayerManager().getJobsPlayer(p);

        // Check if player is in miner
        if (pJobs.isInJob(pJob) && pJobs.getJobProgression(pJob).getLevel() >= getJobLevel(pJob))
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

        ArrayList<Material> blacksmithCrafts = JobsStorage.getBlacksmithCrafts();

        if (p == null || pJobs.isInJob(pJob) &&
                pJobs.getJobProgression(pJob).getLevel() >= getJobLevel(pJob)) return;

        Material craftType = e.getRecipe().getResult().getType();

        if (blacksmithCrafts.contains(craftType)) {
            e.setCancelled(true);
            sendNoMessage(p, pJob, "craft diamond gear! Except for diamond pickaxe");
        }
    }

    @EventHandler
    public void onHarvestCrop(PrepareCropHarvestEvent e) {
        Player p = e.getPlayer();
        Job pJob = farmer;
        JobsPlayer pJobs = Jobs.getPlayerManager().getJobsPlayer(p);
        boolean isInJob = pJobs.isInJob(pJob);

        if (!(isInJob && pJobs.getJobProgression(pJob).getLevel() >= getJobLevel(pJob))) {
            e.setCancelled(true);
            sendNoMessage(p, pJob, "use cropharvester!");
        }
    }

    @EventHandler
    public void onRuneCraft(SmithItemEvent e) {
        if (e.isCancelled() || !(e.getWhoClicked() instanceof Player p)) return;

        Recipe re = e.getInventory().getRecipe();

        if (!(re instanceof SmithingRecipe recipe)) return;

        List<SmithingRecipe> recipes = SmithRecipes.getRecipe();
        boolean hasRecipe = UtilRandom.isRecipe(recipes, recipe);

        if (!hasRecipe) return;

        Job pJob = runesmith;
        JobsPlayer pJobs = Jobs.getPlayerManager().getJobsPlayer(p);
        boolean isInJob = pJobs.isInJob(pJob);

        if (!(isInJob && pJobs.getJobProgression(pJob).getLevel() >= getJobLevel(pJob))) {
            e.setCancelled(true);
            sendNoMessage(p, pJob, "craft heirlooms!");
        }

    }

    @EventHandler
    public void onHeirloomCraft(CraftItemEvent e) {
        if (e.isCancelled() || !(e.getWhoClicked() instanceof Player p)) return;
        Job pJob = runesmith;
        JobsPlayer pJobs = Jobs.getPlayerManager().getJobsPlayer(p);

        if (pJobs.isInJob(pJob) && pJobs.getJobProgression(pJob).getLevel() >= getJobLevel(pJob)) return;
        if (!(e.getRecipe() instanceof CraftingRecipe recipe)) return;

        List<CraftingRecipe> recipes = HeirloomRunes.getRecipe();
        boolean hasRecipe = UtilRandom.isRecipe(recipes, recipe);

        if (hasRecipe) {
            e.setCancelled(true);
            sendNoMessage(p, pJob, "craft runes!");
        }

    }


    // Methods and NOT events from here on out

}
