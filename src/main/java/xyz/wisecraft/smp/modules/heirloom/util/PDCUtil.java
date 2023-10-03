package xyz.wisecraft.smp.modules.heirloom.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xyz.wisecraft.smp.modules.heirloom.HeirloomModule;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.BaseHeirloom;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.HeirloomType;
import xyz.wisecraft.smp.modules.heirloom.storage.HeirloomStorage;

import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

public abstract class PDCUtil {

    public static <T extends BaseHeirloom> T importHeirloom(PersistentDataContainer data, Class<T> clazz, Player player) {

        T heirloom = null;
        try {
            // String heirloomTypeKey = data.get(BaseHeirloom.getHeirloomTypeKey(), PersistentDataType.STRING);
            float XP = data.get(BaseHeirloom.getHeirloomXPKey(), PersistentDataType.FLOAT);
            int LVL = data.get(BaseHeirloom.getHeirloomLVLKey(), PersistentDataType.INTEGER);
            String heirloomID = data.get(BaseHeirloom.getHeirloomIDKey(), PersistentDataType.STRING);
            UUID uuid = UUID.fromString(heirloomID);
            UUID playerID = player.getUniqueId();
            heirloom = clazz.getConstructor(Integer.TYPE, Float.TYPE, Date.class, UUID.class, UUID.class).newInstance(LVL, XP, new Date(), uuid, playerID);
            HeirloomStorage.addHeirloom(uuid, heirloom);
        } catch (Exception ex) {
            HeirloomModule.plugin.getLogger().log(Level.SEVERE, "Can not import heirloom", ex);
        }

        return heirloom;
    }
}
