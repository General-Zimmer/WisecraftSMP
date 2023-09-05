package xyz.wisecraft.smp.modules.cropharvester.listener;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.wisecraft.smp.modules.cropharvester.events.PrepareCropHarvestEvent;
import xyz.wisecraft.smp.modules.cropharvester.util.UtilRandom;
import xyz.wisecraft.smp.storage.OtherStorage;

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

        PrepareCropHarvestEvent harvestEvent = new PrepareCropHarvestEvent(e.getPlayer(), ageable);

        Bukkit.getPluginManager().callEvent(harvestEvent);
        if (harvestEvent.isCancelled()) return;
        // Add the Grief checking here

        int BlockAge = ageable.getAge();

        if (OtherStorage.getTools().containsKey(itemInHand.getType()) && BlockAge == ageable.getMaximumAge()) {

            /*
            int size = Integer.parseInt(OtherStorage.getTools().get(itemInHand.getType()).substring(0,1));
            UtilRandom.farmBlocksXByX(size, clickedBlock, itemInHand,e.getPlayer());

             */


            UtilRandom.farmOptimalFarmingBlocks(4,4,clickedBlock, itemInHand, e.getPlayer());
        }
    }


}
