package xyz.wisecraft.smp.cropharvester.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.wisecraft.smp.Storage;
import xyz.wisecraft.smp.cropharvester.util.UtilRandom;

import java.util.HashMap;

public class HarvestListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Block clickedBlock = e.getClickedBlock();
        ItemStack itemInHand = e.getPlayer().getInventory().getItemInMainHand();

        if ( !(clickedBlock.getBlockData() instanceof Ageable)) return;

        Ageable ageable = (Ageable) clickedBlock.getBlockData();
        int BlockAge = ageable.getAge();

        HashMap<Material, String> idk = new HashMap<>(Storage.getTools());
        if (Storage.getTools().containsKey(itemInHand.getType()) && BlockAge == ageable.getMaximumAge()) {
            int size = Integer.parseInt(Storage.getTools().get(itemInHand.getType()).substring(0,1));
            UtilRandom.farmBlocksXByX(size, clickedBlock, itemInHand,e.getPlayer());
        }
    }



}
