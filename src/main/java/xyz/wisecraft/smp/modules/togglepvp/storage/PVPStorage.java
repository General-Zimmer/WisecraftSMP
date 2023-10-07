package xyz.wisecraft.smp.modules.togglepvp.storage;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperCollection;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperGeneric;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperMaps;
import xyz.wisecraft.smp.modules.togglepvp.utils.PersistentData;

import java.io.File;
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
    private static StorageHelperGeneric<PersistentData> PVPPersistentData;


    /**
     * Set the PVPDataUtils
     */
    public static void setupTogglePVPData(ModuleClass module) {
        // PVPToggle persistent data
        File PVPData = new File(module.getPlugin().getDataFolder(), "togglepvp");
        PersistentData persistentData = new PersistentData(PVPData);
        PVPStorage.PVPPersistentData = new StorageHelperGeneric<>(module, "PVPPersistentData");
        PVPStorage.PVPPersistentData.set(persistentData);

        // setup PVPPlayers
        HashMap<UUID, Boolean> PVPPlayers = new HashMap<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            PVPPlayers.put(player.getUniqueId(), persistentData.GetPlayerPvPState(player));
        }
        PVPStorage.PVPPlayers = new StorageHelperMaps<>(module, "PVPPlayers", PVPPlayers);

        // setup blocked worlds
        List<String> blockedWorlds = module.getPlugin().getConfig().getStringList("SETTINGS.BLOCKED_WORLDS");
        PVPStorage.blockedWorlds = new StorageHelperCollection<>(module, "blockedWorlds", blockedWorlds);

        // setup cooldowns
        PVPStorage.cooldowns = new StorageHelperMaps<>(module, "cooldowns", new HashMap<>());
    }
}
