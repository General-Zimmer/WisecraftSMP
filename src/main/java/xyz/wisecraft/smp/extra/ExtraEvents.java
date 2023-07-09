package xyz.wisecraft.smp.extra;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.features.savinggrace.Angel;
import xyz.wisecraft.smp.extra.util.Methods;
import xyz.wisecraft.smp.storage.OtherStorage;

import java.util.HashMap;
import java.util.UUID;

public class ExtraEvents implements Listener {
    private final WisecraftSMP plugin = WisecraftSMP.getInstance();

    private final HashMap<UUID, Angel> angels = OtherStorage.getAngels();

    public ExtraEvents() {
    }

    @EventHandler
    public void finishTutorial(PlayerCommandPreprocessEvent e) {
        String world = e.getPlayer().getWorld().getName();
        String cmd = e.getMessage().toLowerCase().split(" ")[0];
        if (world.equals("tutorial"))
            switch (cmd) {
                //I love this, makes it real clean looking.
                case "/resourceworld", "/sethome", "/resource", "/wisecraft" -> Methods.noFinishTut(e);
            }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (!angels.containsKey(p.getUniqueId()))
            angels.put(p.getUniqueId(), new Angel(p.hasPermission("wisecraft.donator")));


        new BukkitRunnable() {
            @Override
            public void run() {
                if (!p.hasPlayedBefore() && Bukkit.getWorld("tutorial") != null)
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvtp " + p.getName() + " tutorial");
            }
        }.runTaskLater(plugin, 4);
    }
}
