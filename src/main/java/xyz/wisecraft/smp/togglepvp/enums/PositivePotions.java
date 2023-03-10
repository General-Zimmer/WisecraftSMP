package xyz.wisecraft.smp.togglepvp.enums;

import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class PositivePotions {

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
}
