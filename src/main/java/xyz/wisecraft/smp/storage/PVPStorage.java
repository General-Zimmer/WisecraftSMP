package xyz.wisecraft.smp.storage;

import xyz.wisecraft.smp.features.togglepvp.utils.PersistentData;

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


    public static void setPVPDataUtils(PersistentData PVPDataUtils) {
        PVPStorage.PVPDataUtils = PVPDataUtils;
    }

    public static void setBlockedWorlds(List<String> blockedWorlds) {
        PVPStorage.blockedWorlds = blockedWorlds;
    }
    public static List<String> getBlockedWorlds() {
        return blockedWorlds;
    }

    public static HashMap<UUID, Boolean> getPVPPlayers() {
        return PVPPlayers;
    }

    public static HashMap<UUID, Date> getCooldowns() {
        return cooldowns;
    }

    public static PersistentData getPVPDataUtils() {
        return PVPDataUtils;
    }

}
