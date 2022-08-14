package xyz.wisecraft.smp;

import com.earth2me.essentials.Kit;
import com.earth2me.essentials.User;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Events implements Listener{

    private final IEssentials ess;
    public final ConcurrentHashMap<UUID, Boolean> deathmap;
    private final WisecraftSMP plugin;

    public Events(WisecraftSMP plugin, IEssentials ess, ConcurrentHashMap<UUID, Boolean> deathmap) {
        this.ess = ess;
        this.deathmap = deathmap;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onjoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getWorld("tutorial") == null)
                    return;
                if (!p.hasPlayedBefore())
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvtp " + p.getName() + " tutorial");
            }
        }.runTaskLater(WisecraftSMP.getPlugin(WisecraftSMP.class), 4);
    }

    @EventHandler
    public void hasDied(PlayerDeathEvent e) {
        UUID UUID = e.getEntity().getUniqueId();
        deathmap.putIfAbsent(UUID, true);
        new BukkitRunnable() {

            @Override
            public void run() {
                deathmap.remove(UUID);
            }
        }.runTaskLaterAsynchronously(plugin, 10);
    }

    @EventHandler()
    public void getStarterItemsBack(PlayerRespawnEvent e) throws Exception {

        //Does dis guy have home?
        boolean hasHome = false;
        User user = ess.getUser(e.getPlayer());
            if (user.hasHome() || e.isBedSpawn() || e.isAnchorSpawn()) {
                hasHome = true;
        }

            //Give items if no home
        if (!hasHome) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvtp " + e.getPlayer().getName() + " tutorial");
            Kit kit = new Kit("starter", ess);
            kit.expandItems(user);
            deathmap.remove(e.getPlayer().getUniqueId());
        }
    }
    @EventHandler
    public void finishTutorial(PlayerCommandPreprocessEvent e) {
        if (e.getPlayer().getWorld().getName().equals("tutorial") && e.getMessage().equals("resourceworld")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(Color.ORANGE + "Finish the tutorial first!");
        }
    }

}
