package xyz.wisecraft.smp.modules.togglepvp.listeners;

import com.songoda.ultimatetimber.events.TreeDamageEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.wisecraft.smp.modules.togglepvp.utils.PVPUtil;
import xyz.wisecraft.smp.util.UtilCommon;

public class PVPTimberListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onTimber(TreeDamageEvent e) {
        Player victim = e.getVictim();
        Player attacker = UtilCommon.getWhoTimber(victim);

        if (PVPUtil.checkPVPStates(attacker, victim))
            e.setCancelled(true);

    }
}
