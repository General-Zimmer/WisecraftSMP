package xyz.wisecraft.smp.modules.togglepvp.listeners;

import com.craftaro.ultimatetimber.events.TreeDamageEvent;
import com.craftaro.ultimatetimber.events.TreeFellEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import xyz.wisecraft.core.data.templates.Timers;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.togglepvp.utils.PVPUtil;
import xyz.wisecraft.smp.util.UtilCommon;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PVPTimberListener
 */
public class PVPTimberListener implements Listener {

    private final ConcurrentHashMap<UUID, Timers> timers = WisecraftSMP.getCore().getTimers();

    /**
     * Cancel timber damage if the victim does not have pvp on
     * @param e TreeFellEvent
     */
    @EventHandler(ignoreCancelled = true)
    public void onTimberDamage(TreeDamageEvent e) {
        Player victim = e.getVictim();
        Player attacker = UtilCommon.getWhoTimber(victim);

        if (PVPUtil.checkPVPStates(attacker, victim))
            e.setCancelled(true);

    }

    @EventHandler(ignoreCancelled = true)
    public void onTimber(TreeFellEvent e) {
        Player p = e.getPlayer();
        UUID UUID = p.getUniqueId();


        if (!timers.containsKey(UUID)) {
            timers.put(UUID, new Timers(new Date()));
        }
        else {
            timers.get(UUID).setTree(new Date());
        }

    }

    /**
     * When a tree is felled, start a timer for the player
     * @param e TreeFellEvent
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void Dying(PlayerDeathEvent e) {
        Player victim = e.getPlayer();

        if (!e.getDeathMessage().equalsIgnoreCase(victim.getName() + " died")) return;

        Player attacker = UtilCommon.getWhoTimber(victim);

        if (victim.equals(attacker)) {
            e.setDeathMessage(victim.getName() + " was crushed under their timber");
        } else if (attacker != null)
            e.setDeathMessage(victim.getName() + " was crushed under a tree because of " + attacker.getName());


    }

}
