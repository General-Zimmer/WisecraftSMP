package xyz.wisecraft.smp.modules.togglepvp.storage;

import lombok.Getter;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperGeneric;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperMaps;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperSet;
import xyz.wisecraft.smp.modules.togglepvp.utils.PersistentData;

import java.io.File;
import java.util.*;

/**
 * Storage for the plugin to store data in RAM.
 */
public abstract class PVPStorage {


    /**
     * -- GETTER --
     *  Get the blocked worlds
     */
    @Getter
    private static Set<String> blockedWorlds;
    /**
     * False is pvp on. True is pvp off
     * -- GETTER --
     *  Get the PVPPlayers
     */
    @Getter
    public static StorageHelperMaps<UUID, Boolean> PVPPlayers;
    /**
     * -- GETTER --
     *  Get the cooldowns
     */
    @Getter
    private static Map<UUID, Date> cooldowns;
    /**
     * -- GETTER --
     *  Get the PVPDataUtils
     */
    @Getter
    private static StorageHelperGeneric<PersistentData> PVPPersistentData;


    /**
     * Set the PVPDataUtils
     */
    public static void setupTogglePVPData(ModuleClass module) {
        // PVPToggle persistent data
        File PVPData = new File(module.getPlugin().getDataFolder(), "togglepvp");
        PersistentData persistentData = new PersistentData(PVPData, module);
        PVPStorage.PVPPersistentData = new StorageHelperGeneric<>(module, "PVPPersistentData");
        PVPStorage.PVPPersistentData.set(persistentData);

        // setup blocked worlds
        List<String> blockedWorlds = module.getPlugin().getConfig().getStringList("SETTINGS.BLOCKED_WORLDS");
        PVPStorage.blockedWorlds = new StorageHelperSet<>(module, "blockedWorlds", new HashSet<>(blockedWorlds));

        // setup cooldowns
        PVPStorage.cooldowns = new StorageHelperMaps<>(module, "cooldowns");
    }
}
