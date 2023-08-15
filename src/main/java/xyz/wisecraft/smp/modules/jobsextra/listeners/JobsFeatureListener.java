package xyz.wisecraft.smp.modules.jobsextra.listeners;

import com.gamingmesh.jobs.Jobs;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * This class is for extra jobs features.
 */
public class JobsFeatureListener implements org.bukkit.event.Listener {

    @EventHandler
    public void onMinerBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();

        // Check if player is in miner
        if (!Jobs.getPlayerManager().getJobsPlayer(player).isInJob(Jobs.getJob("Miner")))
            return;

        if (e.getBlock().getType().equals(Material.ANCIENT_DEBRIS))
            e.setCancelled(true);

    }

    

}
