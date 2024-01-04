package xyz.wisecraft.smp.modules.heirloom.util;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xyz.wisecraft.smp.modules.heirloom.HeirloomModule;
import xyz.wisecraft.smp.modules.heirloom.heirlooms.BaseHeirloom;
import xyz.wisecraft.smp.modules.heirloom.storage.HeirloomStorage;

import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

import static xyz.wisecraft.smp.modules.heirloom.heirlooms.BaseHeirloom.*;

public abstract class PDCUtil {

    public static <T extends BaseHeirloom> T importHeirloom(PersistentDataContainer data, Class<T> clazz) {

        T heirloom = null;
        try {
            // String heirloomTypeKey = data.get(BaseHeirloom.getHeirloomTypeKey(), PersistentDataType.STRING);
            float XP = data.get(heirloomXPKey, PersistentDataType.FLOAT);
            int LVL = data.get(heirloomLVLKey, PersistentDataType.INTEGER);
            String heirloomID = data.get(heirloomIDKey, PersistentDataType.STRING);
            UUID uuid = UUID.fromString(heirloomID);
            heirloom = clazz.getConstructor(Integer.TYPE, Float.TYPE, Date.class, UUID.class).newInstance(LVL, XP, new Date(), uuid);
            HeirloomStorage.addHeirloom(uuid, heirloom);
        } catch (Exception ex) {
            HeirloomModule.plugin.getLogger().log(Level.SEVERE, "Can not import heirloom", ex);
        }

        return heirloom;
    }
}
