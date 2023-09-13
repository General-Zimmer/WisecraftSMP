package xyz.wisecraft.smp.modules.heirloom.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionData;

public abstract class UtilRandom {


    public PotionData getPotionEffectForArrow(ItemStack bow, PotionMeta potionMeta) {
        PersistentDataContainer pdc = bow.getItemMeta().getPersistentDataContainer();

    }
}
