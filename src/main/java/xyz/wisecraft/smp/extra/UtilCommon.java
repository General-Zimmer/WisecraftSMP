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
                biggest = attacker.getKey();
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
            if (!victimUUID.toString().equals(attackerUUID.toString()) && secSinceAttackerTimber < timeFrame) {
                players.put(secSinceAttackerTimber, attacker);
            }
        }
        if (players.isEmpty())
            return null;
        return players;
    }

}
