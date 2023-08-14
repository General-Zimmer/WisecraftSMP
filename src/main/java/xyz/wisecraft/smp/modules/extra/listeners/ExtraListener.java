package xyz.wisecraft.smp.modules.extra.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.extra.util.UtilRandom;
import xyz.wisecraft.smp.modules.savinggrace.Angel;
import xyz.wisecraft.smp.storage.OtherStorage;

import java.util.HashMap;
import java.util.UUID;

/**
 * Listener class for player events.
 */
public class ExtraListener implements Listener {
    private final WisecraftSMP plugin = WisecraftSMP.getInstance();

    private final HashMap<UUID, Angel> angels = OtherStorage.getAngels();

    /**
     * Prevents players from finishing the tutorial.
     * @param e The PlayerCommandPreprocessEvent.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void finishTutorial(PlayerCommandPreprocessEvent e) {
        String world = e.getPlayer().getWorld().getName();
        String cmd = e.getMessage().toLowerCase().split(" ")[0];
        if (world.equals("tutorial"))
            switch (cmd) {
                //I love this, makes it real clean looking.
                case "/resourceworld", "/sethome", "/resource", "/wisecraft" -> {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.RED + "Finish the tutorial first!");
                    e.getPlayer().sendMessage(ChatColor.GOLD + "You can always return with /wisecraft tutorial");
                }
            }
    }

    /**
     * Adds the player to the hashmap when they join.
     * @param e The PlayerJoinEvent.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!p.hasPlayedBefore() && Bukkit.getWorld("tutorial") != null)
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvtp " + p.getName() + " tutorial");
            }
        }.runTaskLater(plugin, 4);
    }
}
