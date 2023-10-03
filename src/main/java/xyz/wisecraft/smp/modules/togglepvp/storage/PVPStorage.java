package xyz.wisecraft.smp.modules.togglepvp.storage;

import lombok.Getter;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperCollection;
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
    private static StorageHelperCollection<List<String>, String> blockedWorlds;
    /**
     * False is pvp on. True is pvp off
     * -- GETTER --
     *  Get the PVPPlayers
     */
    @Getter
    private static StorageHelperMaps<HashMap<UUID,Boolean>, UUID, Boolean> PVPPlayers;
    /**
     * -- GETTER --
     *  Get the cooldowns
     */
    @Getter
    private static StorageHelperMaps<HashMap<UUID, Date>, UUID, Date> cooldowns;
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

    public static void setPVPPlayers(ModuleClass module, HashMap<UUID,Boolean> PVPPlayers) {
        PVPStorage.PVPPlayers = new StorageHelperMaps<>(module, "PVPPlayers", PVPPlayers);
    }

    /**
     * Set the blocked worlds
     * @param blockedWorlds List of blocked worlds
     */
    public static void setBlockedWorlds(ModuleClass module, List<String> blockedWorlds) {

        PVPStorage.blockedWorlds = new StorageHelperCollection<>(module, "blockedWorlds", blockedWorlds);
    }

    /**
     * Set cooldowns
     * @param module ModuleClass
     */
    public static void setCooldowns(ModuleClass module) {

        PVPStorage.cooldowns = new StorageHelperMaps<>(module, "cooldowns", new HashMap<>());
    }
}
