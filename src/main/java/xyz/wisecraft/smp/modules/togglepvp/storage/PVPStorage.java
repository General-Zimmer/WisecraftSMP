package xyz.wisecraft.smp.modules.togglepvp.storage;

import com.google.inject.TypeLiteral;
import lombok.Getter;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperGeneric;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperMaps;
import xyz.wisecraft.smp.modules.togglepvp.utils.PersistentData;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Storage for the plugin to store data in RAM.
 */
public abstract class PVPStorage {


    /**
     * -- GETTER --
     *  Get the blocked worlds
     */
    @Getter
    private static List<String> blockedWorlds;
    /**
     * False is pvp on. True is pvp off
     * -- GETTER --
     *  Get the PVPPlayers
     */
    @Getter
    private static final HashMap<UUID,Boolean> PVPPlayers = new HashMap<>();
    /**
     * -- GETTER --
     *  Get the cooldowns
     */
    @Getter
    private static StorageHelperMaps<HashMap<UUID, Date>, UUID, Date> cooldowns = null;
    /**
     * -- GETTER --
     *  Get the PVPDataUtils
     */
    @Getter
    private static StorageHelperGeneric<PersistentData> PVPDataUtils;


    /**
     * Set the PVPDataUtils
     * @param PVPDataUtils PersistentData
     */
    public static void setPVPDataUtils(ModuleClass module, PersistentData PVPDataUtils) {
        PVPStorage.PVPDataUtils = new StorageHelperGeneric<>(module, "PVPDataUtils");
        getPVPDataUtils().set(PVPDataUtils);
    }

    /**
     * Set the blocked worlds
     * @param blockedWorlds List of blocked worlds
     */
    public static void setBlockedWorlds(List<String> blockedWorlds) {
        PVPStorage.blockedWorlds = blockedWorlds;
    }

    /**
     * Set the cooldowns
     * @param cooldowns HashMap of cooldowns
     */
    public static void setCooldowns(ModuleClass module, HashMap<UUID, Date> cooldowns) {

        TypeLiteral<HashMap<UUID, Date>> yeet = new TypeLiteral<>() {
        };

        PVPStorage.cooldowns = new StorageHelperMaps<HashMap<UUID, Date>, UUID, Date>(module, "cooldowns", HashMap.class);
        PVPStorage.cooldowns.set(new HashMap<>(cooldowns));
    }
}
