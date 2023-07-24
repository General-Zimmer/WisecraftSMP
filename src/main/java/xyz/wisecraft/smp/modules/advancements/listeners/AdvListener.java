package xyz.wisecraft.smp.modules.advancements.listeners;

import com.fren_gor.ultimateAdvancementAPI.events.PlayerLoadingCompletedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.wisecraft.smp.modules.advancements.AdvancementsModule;

public class AdvListener implements Listener {

    AdvancementsModule plugin;

    public AdvListener(AdvancementsModule plugin) {
        this.plugin = plugin;
    }

}
