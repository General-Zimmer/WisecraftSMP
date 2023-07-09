package xyz.wisecraft.smp.features.advancements.threads;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.node.Node;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.wisecraft.core.data.templates.Infop;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.features.advancements.util.UtilAdv;


import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class roles extends BukkitRunnable {

    private final ConcurrentHashMap<UUID, Infop> infom;
    private final WisecraftSMP plugin;
    private final ArrayList<Player> players;
    private final LuckPerms luck;

    public roles(WisecraftSMP plugin, ConcurrentHashMap<UUID, Infop> infom, ArrayList<Player> players, LuckPerms luck) {
        this.players = players;
        this.infom = infom;
        this.plugin = plugin;
        this.luck = luck;
    }
    public void run() {

        Node citizenN = UtilAdv.buildNode("citizen");
        Node nobleN = UtilAdv.buildNode("noble");

        for (Player p : players) {


            boolean hasCitizen = false;
            boolean hasNoble = false;
            //Check if user has citizen
            for (Node node : luck.getPlayerAdapter(Player.class).getUser(p).getNodes()) {
                if (node.getKey().equals(citizenN.getKey()))
                    hasCitizen = true;
                if (node.getKey().equals(nobleN.getKey()))
                    hasNoble = true;

            }

            if (!hasCitizen)
                gibCitizen(p);
            if (!hasNoble)
                gibNoble(p);

        }
    }

    public void gibCitizen(Player p) {
        //Check if user meets requirements
        Infop data = infom.get(p.getUniqueId());
        if (data.getTime() >= 300 && data.getBlocksPlace() >= 2000 && data.getBlocksBroke() >= 4000) {
            NamespacedKey key = new NamespacedKey(plugin, "citizen");

            // Give it if all are true
            UtilAdv.gibCri("citizen", key, p);
        }
    }

    public void gibNoble(Player p) {
        //Check if user meets requirements
        Infop data = infom.get(p.getUniqueId());
        if (data.getTime() >= 1200 && data.getBlocksPlace() >= 10000 && data.getBlocksBroke() >= 15000) {
            NamespacedKey key = new NamespacedKey(plugin, "noble");

            // Give it if all are true
            UtilAdv.gibCri("noble", key, p);
        }
    }

}
