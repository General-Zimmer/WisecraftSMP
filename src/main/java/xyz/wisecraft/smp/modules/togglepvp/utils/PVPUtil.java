package xyz.wisecraft.smp.modules.togglepvp.utils;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperMaps;
import xyz.wisecraft.smp.modules.togglepvp.storage.PVPStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

/**
 * Utility class for PVP related methods.
 */
public abstract class PVPUtil {

    private static final StorageHelperMaps<HashMap<UUID, Boolean>, UUID, Boolean> pvpPlayers = PVPStorage.getPVPPlayers();

    /**
     * Get all positive effects
     * @return ArrayList of PotionEffectTypes
     */
    public static ArrayList<PotionEffectType> getPositiveEffects() {
        ArrayList<PotionEffectType> effects = new ArrayList<>();
        effects.add(PotionEffectType.FIRE_RESISTANCE);
        effects.add(PotionEffectType.HEALTH_BOOST);
        effects.add(PotionEffectType.HEAL);
        effects.add(PotionEffectType.DAMAGE_RESISTANCE);
        effects.add(PotionEffectType.NIGHT_VISION);
        effects.add(PotionEffectType.INVISIBILITY);
        effects.add(PotionEffectType.ABSORPTION);
        effects.add(PotionEffectType.FAST_DIGGING);
        effects.add(PotionEffectType.SPEED);
        effects.add(PotionEffectType.INCREASE_DAMAGE);
        effects.add(PotionEffectType.REGENERATION);
        effects.add(PotionEffectType.WATER_BREATHING);
        effects.add(PotionEffectType.LUCK);
        effects.add(PotionEffectType.CONDUIT_POWER);
        effects.add(PotionEffectType.DOLPHINS_GRACE);
        effects.add(PotionEffectType.HERO_OF_THE_VILLAGE);
        effects.add(PotionEffectType.SLOW_FALLING);
        // effects.add(PotionEffectType.);


        
        return effects;
    }

    /**
     * Checks if the effects are positive
     * @param effects Collection of PotionEffects to check
     * @return true if the effects are positive
     */
    public static boolean isEffectsPositive(Collection<PotionEffect> effects) {
        for (PotionEffect PEffect : effects) {
            if (!PVPUtil.getPositiveEffects().contains(PEffect.getType()))
                return false;
        }
        return true;
    }

    /**
     * Checks if the effect is positive
     * @param effect PotionEffectType to check
     * @return true if the effect is positive
     */
    public static boolean isEffectsPositive(PotionEffectType effect) {
        return PVPUtil.getPositiveEffects().contains(effect);
    }

    /**
     * Checks if 2 players have pvp disabled, and reset cooldown time if they don't.
     * @param attacker Player who attacked @param victim
     * @param victim Player who got attacked by attacker
     * @return returns true if either players have pvp disabled
     */
    public static boolean checkPVPStates(Player attacker, Player victim) {
        if(attacker == null || attacker.equals(victim)) return false;

        Boolean isVictimPVPOff = pvpPlayers.get(victim.getUniqueId());
        Boolean isAttackerPVPOff = pvpPlayers.get(attacker.getUniqueId());
        if(isAttackerPVPOff) {
            UtilChat.send(attacker, "PVP_DISABLED");
            return true;
        } else if (isVictimPVPOff != null && isVictimPVPOff) {
            UtilChat.send(attacker, "PVP_DISABLED_OTHERS", victim.getName());
            return true;
        } else {
            UtilPlayers.setCooldownTime(attacker);
            UtilPlayers.setCooldownTime(victim);
            return false;
        }

    }

}
