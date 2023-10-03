package xyz.wisecraft.smp.modules.heirloom.util;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionData;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modules.heirloom.HeirloomModule;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.BaseHeirloom;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.HeirloomType;

import java.util.ArrayList;

public abstract class UtilRandom {


    /**
     * Implementation not done
     * Implementation notes
     * lvl 1: Normal
     * lvl 2: Normal + Splash
     * lvl 3: Normal + Splash + Lingering
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
            // BaseHeirloom.createHeirLoom(heirloomBow, HeirloomType.BOWHEIRLOOM);
        }
    }

    public static boolean checkItemIsHeirloom(ItemStack itemStack) {
        ItemMeta itemMeta = (itemStack != null) ? itemStack.getItemMeta(): null;
        PersistentDataContainer pdc = (itemMeta != null) ? itemMeta.getPersistentDataContainer(): null;
        String pdcTypeString = (pdc != null) ? pdc.get(BaseHeirloom.getHeirloomTypeKey(), PersistentDataType.STRING): null;

        if (pdcTypeString == null || !pdcTypeString.equals(HeirloomType.BOWHEIRLOOM.toString())) {
            return false;
        }
        return true;
    }

    public static void setPotionNBT(Arrow arrow, Material potionType, NamespacedKey potionkey) {
        ItemMeta itemMeta = arrow.getItemStack().getItemMeta();
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        data.set(potionkey, PersistentDataType.STRING, potionType.toString());
        arrow.getItemStack().setItemMeta(itemMeta);
    }
}
