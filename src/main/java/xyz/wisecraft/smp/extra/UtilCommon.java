package xyz.wisecraft.smp.extra;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.wisecraft.core.data.templates.Timers;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.advancements.util.UtilAdv;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UtilCommon {
    /**
     * Note: This can give false positives due to the fact that 1. trees can be made to last longer than TimeFrame or
     * 2. if multiple people nearby are using Timber and the culprit wasn't the one with the highest time or
     * 3. if someone can get over 32 blocks away from the victim
     * <p>
     * A Timber cooldown of over TimeFrame is needed to prevent people from abusing 2. false positive reason
     *
     * @param victim Player that was hit by a tree
     * @param timeFrame The time from Timber was used until it probably finished its animation
     * @return Who might have used Timber
     */
    public static Player getWhoTimber(Player victim, double timeFrame) {

        UUID victimUUID = victim.getUniqueId();
        HashMap<Double, Player> players = whoBrokeTree(victimUUID, timeFrame);
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

    private static HashMap<Double, Player> whoBrokeTree(UUID victimUUID, double timeFrame) {
        HashMap<Double, Player> players = new HashMap<>();
        // Check who broke a tree recently
        for (Player attacker : Bukkit.getOnlinePlayers()) {
            Timers attackerTimer = WisecraftSMP.core.getTimers().get(attacker.getUniqueId());
            double secSinceAttackerTimber = UtilAdv.calcCurrentSeconds(attackerTimer.getTree());
            UUID attackerUUID = attacker.getUniqueId();

            if (!victimUUID.toString().equals(attackerUUID.toString()) && secSinceAttackerTimber < timeFrame)
                players.put(secSinceAttackerTimber, attacker);
        }
        if (players.isEmpty())
            return null;
        return players;
    }

}
