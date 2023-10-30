package xyz.wisecraft.smp.modules.heirloom.heirlooms;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modules.heirloom.HeirloomModule;
import xyz.wisecraft.smp.modules.heirloom.storage.HeirloomStorage;
import xyz.wisecraft.smp.modules.heirloom.util.PDCUtil;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

import static xyz.wisecraft.smp.modules.heirloom.util.UtilRandom.checkMainHandForBow;

public abstract class BaseHeirloom {
    @Getter
    private int level;
    @Getter
    private float xp;
    @Getter
    private HeirloomType type;
    @Getter
    private UUID ID;
    public static final NamespacedKey heirloomTypeKey = new NamespacedKey(HeirloomModule.plugin, "heirloomTypeKey");
    public static final NamespacedKey heirloomXPKey = new NamespacedKey(HeirloomModule.plugin, "heirloomXPKey");
    public static final NamespacedKey heirloomLVLKey = new NamespacedKey(HeirloomModule.plugin, "heirloomLVLKey");
    public static final NamespacedKey heirloomIDKey = new NamespacedKey(HeirloomModule.plugin, "heirloomIDKey");
    @Getter
    private final Date objectCreated; // Getting todays date for constructor will be a problem when using just Date. Only LocalDate will work for that


    public BaseHeirloom(int level, float xp, HeirloomType type, Date created, UUID id) {
        this.level = level;
        this.xp = xp;
        this.type = type;
        this.objectCreated = created;
        this.ID = id;
    }

    /**
     * Takes item in players mainhand and adds the Heirloom info to it
     * @param item item in players mainhand, item MUST be of a type that is supported in Heirlooms
     * @param type HeirloomType from HeirloomType enum class
     */
    public static <T extends BaseHeirloom> void createHeirLoom(ItemStack item, HeirloomType type, Class<T> clazz) {
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        UUID uuid = UUID.randomUUID();
        data.set(heirloomTypeKey, PersistentDataType.STRING, type.toString());
        data.set(heirloomXPKey, PersistentDataType.FLOAT, 0F);
        data.set(heirloomLVLKey, PersistentDataType.INTEGER, 1);
        data.set(heirloomIDKey, PersistentDataType.STRING, uuid.toString());
        item.setItemMeta(itemMeta);
        try {
            HeirloomStorage.addHeirloom(uuid, clazz.getConstructor(Integer.TYPE, Float.TYPE, Date.class, UUID.class).newInstance(0, 0F, new Date(), uuid));
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            HeirloomModule.plugin.getLogger().log(Level.SEVERE, "Could not add heirloom to storage, clazz constructor error", e);
        }

    }

    /**
     * Search for Heirloom object in our Heirloom storage that matches the item,
     * if it doesn't exist create one and put it into storage
     * NOTE: NOT IMPLEMENTED YET
     * @param item in players main hand
     * @return BaseHeirloom if it exists in Storage, else return null
     */
    public static @NotNull <T extends BaseHeirloom> T getHeirloom(ItemStack item, Class<T> clazz) {
        PersistentDataContainer nbtContainer = item.getItemMeta().getPersistentDataContainer();
        String uuidString = item.getItemMeta().getPersistentDataContainer().get(heirloomIDKey, PersistentDataType.STRING);

        String heirloomTypeValue = item.getItemMeta().getPersistentDataContainer().get(heirloomTypeKey,PersistentDataType.STRING);
        if (!clazz.getSimpleName().equalsIgnoreCase(heirloomTypeValue)) {
            throw new IllegalArgumentException("ItemStack is not of heirloom type " + clazz.getSimpleName());
        }

        UUID uuid = null;
        try {
            uuid = UUID.fromString(uuidString);
        } catch (NullPointerException ex) {
            HeirloomModule.plugin.getLogger().log(Level.SEVERE, "This bow does not have an ID key even though it's an Heirloom - ZIMMER FIX");
        }

        if (HeirloomStorage.findHeirloom(uuid) != null) {
            return (T) HeirloomStorage.findHeirloom(uuid);
        } else {
            return PDCUtil.importHeirloom(nbtContainer,clazz);
        }

    }

    public boolean equals(ItemStack itemstack) {
        ItemMeta itemMeta = (itemstack != null) ? itemstack.getItemMeta(): null;
        PersistentDataContainer pdc = (itemMeta != null) ? itemMeta.getPersistentDataContainer(): null;
        String pdcString = (pdc != null) ? pdc.get(heirloomTypeKey, PersistentDataType.STRING): null;
        return pdcString != null && pdcString.equals(type.toString());
    }

    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Adds 10 XP to the Heirloom
     */
    public void giveXP(Player p) {
        this.xp += 10;

        if (this.xp >= 100*(level)*1.7) {
            this.xp = 0;
            this.level += 1;
            p.sendMessage("Your Heirloom has leveled up!");
        }

        for (int i = 0; i < p.getInventory().getSize(); i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item == null) continue;

            ItemMeta itemMeta = item.getItemMeta();
            PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
            String heirloomID = pdc.get(heirloomIDKey, PersistentDataType.STRING);

            if (heirloomID != null && heirloomID.equals(ID.toString())) {


            }
        }
    }

    private void updateItem(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add("Epic BowHeirloom");
        lore.add("Level: " + level);
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(heirloomLVLKey, PersistentDataType.INTEGER, level);
        pdc.set(heirloomXPKey, PersistentDataType.FLOAT, xp);
        item.setItemMeta(meta);
    }


    public void setType(HeirloomType type) {
        this.type = type;
    }

}
