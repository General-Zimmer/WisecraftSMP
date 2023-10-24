package xyz.wisecraft.smp.modules.heirloom.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;

public class SmithingListener implements Listener {

    @EventHandler
    public void onSmithing(PrepareSmithingEvent e) {
        e.getInventory();
    }
}
