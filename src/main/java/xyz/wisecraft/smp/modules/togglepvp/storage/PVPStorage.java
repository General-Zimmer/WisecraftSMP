package xyz.wisecraft.smp.modules.togglepvp.storage;

import xyz.wisecraft.smp.modules.togglepvp.utils.PersistentData;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Storage for the plugin to store data in RAM.
 */
public abstract class PVPStorage {


    private static List<String> blockedWorlds;
    /**
     * False is pvp on. True is pvp off
     */
    private static final HashMap<UUID,Boolean> PVPPlayers = new HashMap<>();
    private static final HashMap<UUID, Date> cooldowns = new HashMap<>();
    private static PersistentData PVPDataUtils;


    /**
     * Set the PVPDataUtils
     * @param PVPDataUtils PersistentData
     */
    public static void setPVPDataUtils(PersistentData PVPDataUtils) {
        PVPStorage.PVPDataUtils = PVPDataUtils;
    }

    /**
     * Set the blocked worlds
     * @param blockedWorlds List of blocked worlds
     */
    public static void setBlockedWorlds(List<String> blockedWorlds) {
        PVPStorage.blockedWorlds = blockedWorlds;
    }

    /**
     * Get the blocked worlds
     * @return List of blocked worlds
     */
    public static List<String> getBlockedWorlds() {
        return blockedWorlds;
    }

    /**
     * Get the PVPPlayers
     * @return HashMap of PVPPlayers
     */
    public static HashMap<UUID, Boolean> getPVPPlayers() {
        return PVPPlayers;
    }

    /**
     * Get the cooldowns
     * @return HashMap of cooldowns
     */
    public static HashMap<UUID, Date> getCooldowns() {
        return cooldowns;
    }

    /**
     * Get the PVPDataUtils
     * @return PersistentData
     */
    public static PersistentData getPVPDataUtils() {
        return PVPDataUtils;
    }

}
