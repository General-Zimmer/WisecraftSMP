package xyz.wisecraft.smp.advancements.events;

import com.songoda.ultimatetimber.events.TreeFellEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import xyz.wisecraft.core.data.templates.Infop;
import xyz.wisecraft.core.data.templates.Timers;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.advancements.util.UtilAdv;
import xyz.wisecraft.smp.extra.UtilCommon;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static xyz.wisecraft.smp.WisecraftSMP.core;

public class timberEvents implements Listener {

    private final WisecraftSMP plugin;
    private final ConcurrentHashMap<UUID, Infop> infom;
    private final ConcurrentHashMap<UUID, Timers> timers;

    public timberEvents() {
        this.plugin = WisecraftSMP.instance;
        this.infom = core.getInfom();
        this.timers = core.getTimers();
    }


    @SuppressWarnings("ConstantConditions")
    @EventHandler(priority = EventPriority.LOWEST)
    public void Dying(PlayerDeathEvent e) {
        Player victim = e.getPlayer();



        if (e.getDeathMessage().equalsIgnoreCase(victim.getName() + " died")) {
            Timers times = timers.get(victim.getUniqueId());
            double secSinceVictimTimber = UtilAdv.calcCurrentSeconds(times.getTree());

            // Could overwrite someone else killing them. Also, some trees could be made high and have leaves
            // behind them allowing exeptional high time here
            if (secSinceVictimTimber < 1.7) {
                NamespacedKey key = new NamespacedKey(plugin, "move");
                UtilAdv.gibCri("move", key, victim);
                e.setDeathMessage(victim.getName() + " was crushed under their timber");
                return;
            }

            Player attacker = UtilCommon.getWhoTimber(victim, 2.5);

            if (attacker != null)
                e.setDeathMessage(victim.getName() + " was crushed under a tree because of " + attacker.getName());

        }
    }

    //Keep at the bottom, it's ugly asf
    @EventHandler
    public void treecounter(TreeFellEvent e) {
        Player p = e.getPlayer();
        UUID UUID = p.getUniqueId();

        //1 tree required achievement
        NamespacedKey timbKey = new NamespacedKey(plugin, "timber");
        UtilAdv.gibCri("tree", timbKey, p);


        //Increment tree stat
        int trees = infom.get(UUID).getTimber()+1;
        infom.get(UUID).setTimber(trees);

        //Check for achievements
        if (trees >= 1000) {
            NamespacedKey key = new NamespacedKey(plugin, "lumberjack");
            UtilAdv.gibCri("tree1000", key, p);
        }
        if (trees >= 5000) {
            NamespacedKey key = new NamespacedKey(plugin, "experiencedlumb");
            UtilAdv.gibCri("tree5000", key, p);
        }
        if (trees >= 10000) {
            NamespacedKey key = new NamespacedKey(plugin, "expertlumb");
            UtilAdv.gibCri("tree10000", key, p);
        }
        if (trees >= 20000) {
            NamespacedKey key = new NamespacedKey(plugin, "juggerjack");

            UtilAdv.gibCri("tree20000", key, p);
        }

        if (!timers.containsKey(UUID)) {
            timers.put(UUID, new Timers(new Date()));
        }
        else {
            timers.get(UUID).setTree(new Date());
        }
    }
}
