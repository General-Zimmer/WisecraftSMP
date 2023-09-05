package xyz.wisecraft.smp.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.wisecraft.core.data.templates.Timers;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.advancements.util.UtilAdv;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * UtilCommon
 */
public class UtilCommon {
    /**
     * Note: This can give false positives due to the fact that
     * <p> 1. trees can be made to last longer than TimeFrame or
     * <p> 2. if multiple people nearby are using Timber and the culprit wasn't the one with the highest time or
     * <p> 3. if someone can get over 32 blocks away from the victim
     * <p> A Timber cooldown of over TimeFrame is needed to prevent people from abusing 2. false positive reason
     * @param victim Player that was hit by a tree
     * @return Who might have used Timber
     */
    public static Player getWhoTimber(Player victim, ConcurrentHashMap<UUID, Timers> timers) {

        double timeFrame = WisecraftSMP.getInstance().getConfig().getDouble("SETTINGS.TIMBER_TIMEFRAME");

        UUID victimUUID = victim.getUniqueId();
        HashMap<Double, Player> players = whoBrokeTree(victimUUID, timeFrame, timers);
        if (players == null) return null;

        // Are they near the player who takes damage?
        HashMap<Double, Player> attackers = new HashMap<>();
        for (Map.Entry<Double, Player> attacker : players.entrySet()) {

            // Distance is 32
            if (victim.getLocation().distanceSquared(attacker.getValue().getLocation()) <= 1024)
                attackers.put(attacker.getKey(), attacker.getValue());
        }
        if (attackers.isEmpty()) return null;

        // Find the most likely person by highest time
        double biggest = -1;
        for (Map.Entry<Double, Player> attacker : attackers.entrySet()) {
            double timberSecAgo = attacker.getKey();
            if (timberSecAgo <= timeFrame && timberSecAgo > biggest)
                biggest = timberSecAgo;
        }

        if (biggest < 0) return null;

        return attackers.get(biggest);
    }

    private static HashMap<Double, Player> whoBrokeTree(UUID victimUUID, double timeFrame, ConcurrentHashMap<UUID, Timers> timers) {
        HashMap<Double, Player> players = new HashMap<>();
        // Check who broke a tree recently
        for (Player attacker : Bukkit.getOnlinePlayers()) {
            Timers attackerTimer = timers.get(attacker.getUniqueId());
            double secSinceAttackerTimber = UtilCommon.calcCurrentSeconds(attackerTimer.getTree());
            UUID attackerUUID = attacker.getUniqueId();

            if (!victimUUID.toString().equals(attackerUUID.toString()) && secSinceAttackerTimber < timeFrame)
                players.put(secSinceAttackerTimber, attacker);
        }
        if (players.isEmpty())
            return null;
        return players;
    }

    /**
     * Get the time in seconds since previousDate
     * @param previousDate Date to compare to
     * @return Seconds since previousDate
     */
    public static double calcCurrentSeconds(Date previousDate) {
        Date currentDate = new Date();
        return (currentDate.getTime() - previousDate.getTime())/1000.0;
    }

}
