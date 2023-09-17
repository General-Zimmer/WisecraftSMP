package xyz.wisecraft.smp.modules.heirloom.heirlooms;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xyz.wisecraft.smp.modules.heirloom.HeirloomModule;

import static xyz.wisecraft.smp.modules.heirloom.util.UtilRandom.checkMainHandForBow;

public abstract class BaseHeirloom {
    private int level;
    private float xp;
    private HeirloomType type;
    private static final NamespacedKey heirloomTypeKey = new NamespacedKey(HeirloomModule.plugin, "heirloomTypeKey");
    private static final NamespacedKey heirloomXPKey = new NamespacedKey(HeirloomModule.plugin, "heirloomXPKey");
    private static final NamespacedKey heirloomLVLKey = new NamespacedKey(HeirloomModule.plugin, "heirloomLVLKey");


    public BaseHeirloom(int level, float xp, HeirloomType type) {
        this.level = level;
        this.xp = xp;
        this.type = type;
    }
    public static void createHeirLoom(ItemStack item, HeirloomType type) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.getPersistentDataContainer().set(BaseHeirloom.getHeirloomTypeKey(), PersistentDataType.STRING, type.toString());
        itemMeta.getPersistentDataContainer().set(BaseHeirloom.getHeirloomXPKey(), PersistentDataType.FLOAT, 0F);
        itemMeta.getPersistentDataContainer().set(BaseHeirloom.getHeirloomLVLKey(), PersistentDataType.INTEGER, 0);
        item.setItemMeta(itemMeta);
    }

    public boolean equals(ItemStack itemstack) {
        ItemMeta itemMeta = (itemstack != null) ? itemstack.getItemMeta(): null;
        PersistentDataContainer pdc = (itemMeta != null) ? itemMeta.getPersistentDataContainer(): null;
        String pdcString = (pdc != null) ? pdc.get(BaseHeirloom.getHeirloomTypeKey(), PersistentDataType.STRING): null;
        return pdcString != null && pdcString.equals(type.toString());
    }

    public int getLevel() {
        return level;
    }

    public float getXp() {
        return xp;
    }

    public HeirloomType getType() {
        return type;
    }

    public static NamespacedKey getHeirloomTypeKey() {
        return heirloomTypeKey;
    }

    public static NamespacedKey getHeirloomXPKey() {
        return heirloomXPKey;
    }

    public static NamespacedKey getHeirloomLVLKey() {
        return heirloomLVLKey;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setXp(float xp) {
        this.xp = xp;
    }


    public void setType(HeirloomType type) {
        this.type = type;
    }

}
