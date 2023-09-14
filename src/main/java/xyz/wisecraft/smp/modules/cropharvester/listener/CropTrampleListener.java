package xyz.wisecraft.smp.modules.cropharvester.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.wisecraft.smp.modules.cropharvester.events.CropTrampleEvent;

public class CropTrampleListener implements Listener {

    @EventHandler
    public void onPlayerTrample(PlayerInteractEvent event) {
        Block currentBlock = event.getClickedBlock();
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand.getType().equals(Material.AIR) || currentBlock == null) {
            return;
        }


        if (event.getAction() == Action.PHYSICAL && currentBlock.getType().equals(Material.FARMLAND)) {
            CropTrampleEvent cropTrampleEvent = new CropTrampleEvent(event.getPlayer(), CropTrampleEvent.TrampleCause.PLAYER, event.getClickedBlock());
            Bukkit.getPluginManager().callEvent(cropTrampleEvent);
            event.setCancelled(cropTrampleEvent.isCancelled());
        }
    }

    @EventHandler
    public void onTrample(CropTrampleEvent event) {
        if ((event.getCause() == CropTrampleEvent.TrampleCause.MOB) || (event.getCause() == CropTrampleEvent.TrampleCause.PLAYER))
            event.setCancelled(true);
    }
}
