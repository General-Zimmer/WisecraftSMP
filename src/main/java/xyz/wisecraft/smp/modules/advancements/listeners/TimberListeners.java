package xyz.wisecraft.smp.modules.advancements.listeners;

import com.craftaro.ultimatetimber.events.TreeFellEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.wisecraft.core.WisecraftCoreApi;
import xyz.wisecraft.core.data.templates.Infop;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


/**
 * TimberListeners
 */
public class TimberListeners implements Listener {

    private final ConcurrentHashMap<UUID, Infop> infom;

    public TimberListeners(WisecraftCoreApi core) {
        this.infom = core.getInfom();
    }

    /**
     * Add 1 to the tree counter
     * @param e TreeFellEvent
     */
    //Keep at the bottom, it's ugly asf
    @EventHandler
    public void treeCounter(TreeFellEvent e) {
        Player p = e.getPlayer();
        UUID UUID = p.getUniqueId();

        //Increment tree stat
        int trees = infom.get(UUID).getTimber()+1;
        infom.get(UUID).setTimber(trees);

    }
}
