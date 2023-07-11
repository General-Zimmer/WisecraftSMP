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

public class GibRoles extends BukkitRunnable {

    WisecraftSMP plugin;
    WisecraftCoreApi core;
    LuckPerms luck;


    public GibRoles() {
        this.plugin = WisecraftSMP.getInstance();
        this.core = WisecraftSMP.getCore();
        this.luck = WisecraftSMP.getLuck();

    }
    @Override
    public void run() {
        ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        ConcurrentHashMap<UUID, Infop> infom = new ConcurrentHashMap<>(core.getInfom());

        new Roles(plugin, infom, players, luck).runTaskAsynchronously(plugin);
    }

}
