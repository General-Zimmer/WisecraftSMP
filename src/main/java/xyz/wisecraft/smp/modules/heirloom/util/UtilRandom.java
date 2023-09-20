package xyz.wisecraft.smp.modules.heirloom.util;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionData;
import xyz.wisecraft.smp.modules.heirloom.HeirloomModule;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.BaseHeirloom;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.HeirloomType;

import java.util.ArrayList;

public abstract class UtilRandom {


    /**
     * Implementation not done
     *
     * @return Adds the allowed effects to Heirlooms
     */
    public static ArrayList<Effect> initEffects() {
        return null; // Implementation missing
    }
    public static PotionData getPotionEffectForArrow(ItemStack bow, PotionMeta potionMeta) {
        PersistentDataContainer pdc = bow.getItemMeta().getPersistentDataContainer();
        return null;
    }

    public static ItemStack checkMainHandForBow(Player player) {
        ItemStack bow = new ItemStack(Material.BOW);
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (mainHand.equals(bow)) {
            return mainHand;
        }
        return null;
    }

    public static void createBowHeirLoom(Player player) {
        ItemStack heirloomBow = checkMainHandForBow(player);
        if (heirloomBow != null) {
            BaseHeirloom.createHeirLoom(heirloomBow, HeirloomType.BOWHEIRLOOM);
        }
    }
}
