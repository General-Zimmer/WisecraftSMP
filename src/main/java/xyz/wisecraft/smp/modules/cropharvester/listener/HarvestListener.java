package xyz.wisecraft.smp.modules.cropharvester.listener;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.wisecraft.smp.modules.cropharvester.events.PrepareCropHarvestEvent;
import xyz.wisecraft.smp.modules.cropharvester.storage.HarvestStorage;
import xyz.wisecraft.smp.modules.cropharvester.util.UtilRandom;

/**
 * HarvestListener
 */
public class HarvestListener implements Listener {


    /**
     * Harvest crops
     * @param e The event
     */


    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Block clickedBlock = e.getClickedBlock();
        ItemStack itemInHand = e.getPlayer().getInventory().getItemInMainHand();

        if (clickedBlock == null) return;

        if ( !(clickedBlock.getBlockData() instanceof Ageable ageable)) return;


        int BlockAge = ageable.getAge();

        if (HarvestStorage.getTools().containsKey(itemInHand.getType()) && BlockAge == ageable.getMaximumAge()) {
            int size = Integer.parseInt(HarvestStorage.getTools().get(itemInHand.getType()).substring(0,1));
            Player p = e.getPlayer();
            PrepareCropHarvestEvent harvestEvent = new PrepareCropHarvestEvent(p, ageable);

            Bukkit.getPluginManager().callEvent(harvestEvent);
            if (harvestEvent.isCancelled()) return;


            UtilRandom.farmBlocksXByX(size, clickedBlock, itemInHand, p);
        }
    }


}
