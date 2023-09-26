package xyz.wisecraft.smp.modules.heirloom.heirlooms;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xyz.wisecraft.smp.modules.heirloom.HeirloomModule;
import xyz.wisecraft.smp.modules.heirloom.storage.HeirloomStorage;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static xyz.wisecraft.smp.modules.heirloom.util.UtilRandom.checkMainHandForBow;

public abstract class BaseHeirloom {
    private int level;
    private float xp;
    private HeirloomType type;
    private UUID ID;
    private static final NamespacedKey heirloomTypeKey = new NamespacedKey(HeirloomModule.plugin, "heirloomTypeKey");
    private static final NamespacedKey heirloomXPKey = new NamespacedKey(HeirloomModule.plugin, "heirloomXPKey");
    private static final NamespacedKey heirloomLVLKey = new NamespacedKey(HeirloomModule.plugin, "heirloomLVLKey");
    private static final NamespacedKey heirloomIDKey = new NamespacedKey(HeirloomModule.plugin, "heirloomIDKey");
    private Date objectCreated; // Getting todays date for constructor will be a problem when using just Date. Only LocalDate will work for that
    private UUID playedTied;


    public BaseHeirloom(int level, float xp, HeirloomType type, Date created, UUID id, UUID creator) {
        this.level = level;
        this.xp = xp;
        this.type = type;
        this.objectCreated = created;
        this.ID = id;
        this.playedTied = creator;
    }

    /**
     * Takes item in players mainhand and adds the Heirloom info to it
     * @param item item in players mainhand, item MUST be of a type that is supported in Heirlooms
     * @param type HeirloomType from HeirloomType enum class
     */
    public static <T extends BaseHeirloom> void createHeirLoom(ItemStack item, HeirloomType type, Class<T> clazz, UUID playerID) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ItemMeta itemMeta = item.getItemMeta();
        UUID uuid = UUID.randomUUID();
        itemMeta.getPersistentDataContainer().set(BaseHeirloom.getHeirloomTypeKey(), PersistentDataType.STRING, type.toString());
        itemMeta.getPersistentDataContainer().set(BaseHeirloom.getHeirloomXPKey(), PersistentDataType.FLOAT, 0F);
        itemMeta.getPersistentDataContainer().set(BaseHeirloom.getHeirloomLVLKey(), PersistentDataType.INTEGER, 0);
        itemMeta.getPersistentDataContainer().set(BaseHeirloom.getHeirloomIDKey(), PersistentDataType.STRING, uuid.toString());
        item.setItemMeta(itemMeta);
        HeirloomStorage.addHeirloom(uuid, clazz.getConstructor(Integer.TYPE, Float.TYPE, Date.class, UUID.class, UUID.class).newInstance(0, 0F, new Date(), uuid, playerID));
    }

    /**
     * Search for Heirloom object in our Heirloom storage that matches the item,
     * if it doesn't exist create one and put it into storage
     * NOTE: NOT IMPLEMENTED YET
     * @param item in players main hand
     * @return BaseHeirloom if it exists in Storage, else return null
     */
    public static BaseHeirloom getHeirloom(ItemStack item) {
        return null; //Implementation needs to be made
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
    public static NamespacedKey getHeirloomIDKey() {
        return heirloomIDKey;
    }

    public UUID getID() {
        return ID;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Adds XP to the Heirloom
     * @param amount the amount that will be added to the BaseHeirlooms current XP
     */
    public void giveXP(int amount) {
        this.xp += amount;
    }


    public void setType(HeirloomType type) {
        this.type = type;
    }

}
