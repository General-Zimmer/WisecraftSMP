package xyz.wisecraft.smp;

import com.earth2me.essentials.Kit;
import com.earth2me.essentials.User;
import net.ess3.api.IEssentials;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.wisecraft.smp.util.Methods;

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
                if (!p.hasPlayedBefore() && Bukkit.getWorld("tutorial") != null)
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvtp " + p.getName() + " tutorial");
            }
        }.runTaskLater(plugin, 4);
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



    //The exception comes from kit expand
    @EventHandler()
    public void getStarterItemsBack(PlayerRespawnEvent e) throws Exception {

        //Does dis guy have home?
        boolean hasHome = false;
        User user = ess.getUser(e.getPlayer());
        if (user.hasHome() || e.isBedSpawn() || e.isAnchorSpawn())
            hasHome = true;


        //Give items if no home
        if (hasHome) return;


        World tut = Bukkit.getWorld("tutorial");
        if (tut != null)


            //Need a tick delay otherwise they will teleport to spawn. Need to figure out why
            new BukkitRunnable() {
            @Override
            public void run() {
                Location loc = new Location(tut, 5.5, 204, 8.5, -85, 2);
                e.getPlayer().teleport(loc);
            }
        }.runTaskLater(plugin, 1);


        Kit kit = new Kit("starter", ess);
        kit.expandItems(user);
        e.getPlayer().sendMessage(ChatColor.BLUE + "You didn't /sethome or place a bed! You have been granted some new items.");
        deathmap.remove(e.getPlayer().getUniqueId());


        }

    @EventHandler
    public void finishTutorial(PlayerCommandPreprocessEvent e) {
        if (e.getPlayer().getWorld().getName().equals("tutorial"))
            switch (e.getPlayer().getWorld().getName()) {
            //I love this, makes it real clean looking.
            case "resourceworld", "sethome" -> Methods.noFinishTut(e);
        }
    }

}
