package xyz.wisecraft.smp.modules.heirloom.util;

import org.bukkit.Effect;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionData;
import xyz.wisecraft.smp.modules.heirloom.HeirloomModule;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.BowHeirloom;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.HeirloomType;
import xyz.wisecraft.smp.modules.heirloom.listeners.BowListener;
import xyz.wisecraft.smp.modules.heirloom.storage.HeirloomStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static xyz.wisecraft.smp.modules.heirloom.heirlooms.BaseHeirloom.heirloomTypeKey;

public abstract class UtilRandom {

    private static NamespacedKey bowHeirloomKey = new NamespacedKey(HeirloomModule.plugin, "arrowHeirloom");

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

    public static boolean checkItemIsHeirloom(ItemStack itemStack, HeirloomType heirloomType) {
        ItemMeta itemMeta = (itemStack != null) ? itemStack.getItemMeta(): null;
        PersistentDataContainer pdc = (itemMeta != null) ? itemMeta.getPersistentDataContainer(): null;
        String pdcTypeString = (pdc != null) ? pdc.get(heirloomTypeKey, PersistentDataType.STRING): null;

        return pdcTypeString != null && pdcTypeString.equals(heirloomType.toString());
    }

    public static void setArrowPDC(Arrow arrow, Material potionType, UUID bowUUID, UUID playerUUID) {
        PersistentDataContainer data = arrow.getPersistentDataContainer();
        data.set(BowListener.potionKey, PersistentDataType.STRING, potionType.toString());
        data.set(BowListener.heirloomArrowKey, PersistentDataType.STRING, bowUUID.toString());
        data.set(BowListener.playerToXPKey, PersistentDataType.STRING, playerUUID.toString());
    }

    public static BowHeirloom getBowHeirloomFromPDC(PersistentDataContainer PDC) {
        String bowHeirloomUUID = PDC.get(bowHeirloomKey, PersistentDataType.STRING);
        if (bowHeirloomUUID == null) {
            return null;
        }

        return (BowHeirloom) HeirloomStorage.findHeirloom(UUID.fromString(bowHeirloomUUID));
    }

    public static boolean isRecipe(List<? extends Keyed> recipes, Keyed recipe) {
        boolean hasRecipe = false;

        for (Keyed r : recipes) {
            if (r.getKey().equals(recipe.getKey())) {
                hasRecipe = true;
                break;
            }
        }

        return hasRecipe;
    }
}
