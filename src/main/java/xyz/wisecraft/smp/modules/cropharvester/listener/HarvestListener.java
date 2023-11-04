package xyz.wisecraft.smp.modules.cropharvester.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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

import java.util.HashMap;

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
        HashMap<Material, String> tools = HarvestStorage.getTools();

        if (tools.containsKey(itemInHand.getType()) && BlockAge == ageable.getMaximumAge()) {
            int size = Integer.parseInt(HarvestStorage.getTools().get(itemInHand.getType()).substring(0,1));
            Player p = e.getPlayer();
            PrepareCropHarvestEvent harvestEvent = new PrepareCropHarvestEvent(p, ageable);

            Bukkit.getPluginManager().callEvent(harvestEvent);
            if (harvestEvent.isCancelled()) return;


            // UtilRandom.initBlocks(2,6,clickedBlock, itemInHand, p);
            // UtilRandom.initBlocks(4,2,clickedBlock, itemInHand, p);
            UtilRandom.farmAreaWithMostCrops(3,5,clickedBlock, itemInHand, p);
        }
    }


}
