package xyz.wisecraft.smp.features.togglepvp.listeners;

import com.songoda.ultimatetimber.events.TreeDamageEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.extra.UtilCommon;
import xyz.wisecraft.smp.features.togglepvp.utils.PVPUtil;

public class PVPTimberListener implements Listener {
    private final WisecraftSMP instance = WisecraftSMP.getInstance();
    private final FileConfiguration config = instance.getConfig();

    @EventHandler(ignoreCancelled = true)
    public void onTimber(TreeDamageEvent e) {
        Player victim = e.getVictim();
        Player attacker = UtilCommon.getWhoTimber(victim, config.getInt("TIMBER_TIMEFRAME"));

        if (PVPUtil.checkPVPStates(attacker, victim))
            e.setCancelled(true);

    }
}
