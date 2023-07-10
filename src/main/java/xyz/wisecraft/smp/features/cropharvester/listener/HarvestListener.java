package xyz.wisecraft.smp.features.cropharvester.listener;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.wisecraft.smp.features.cropharvester.util.UtilRandom;
import xyz.wisecraft.smp.storage.OtherStorage;

public class HarvestListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Block clickedBlock = e.getClickedBlock();
        ItemStack itemInHand = e.getPlayer().getInventory().getItemInMainHand();

        if ( !(clickedBlock.getBlockData() instanceof Ageable ageable)) return;

        int BlockAge = ageable.getAge();

        if (OtherStorage.getTools().containsKey(itemInHand.getType()) && BlockAge == ageable.getMaximumAge()) {
            int size = Integer.parseInt(OtherStorage.getTools().get(itemInHand.getType()).substring(0,1));
            UtilRandom.farmBlocksXByX(size, clickedBlock, itemInHand,e.getPlayer());
        }
    }



}
