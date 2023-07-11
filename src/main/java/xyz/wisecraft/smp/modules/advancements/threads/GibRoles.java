package xyz.wisecraft.smp.modules.advancements.threads;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.wisecraft.core.WisecraftCoreApi;
import xyz.wisecraft.core.data.templates.Infop;
import xyz.wisecraft.smp.WisecraftSMP;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread for giving roles to players.
 */
public class GibRoles extends BukkitRunnable {

    private final WisecraftSMP plugin = WisecraftSMP.getInstance();
    private final WisecraftCoreApi core = WisecraftSMP.getCore();
    private final LuckPerms luck = WisecraftSMP.getLuck();

    @Override
    public void run() {
        ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        ConcurrentHashMap<UUID, Infop> infom = new ConcurrentHashMap<>(core.getInfom());

        new Roles(plugin, infom, players, luck).runTaskAsynchronously(plugin);
    }

}
