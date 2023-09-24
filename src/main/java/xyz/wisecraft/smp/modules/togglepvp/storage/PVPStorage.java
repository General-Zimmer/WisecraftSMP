package xyz.wisecraft.smp.modules.togglepvp.storage;

import lombok.Getter;
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
     *
     */
    @Getter
    private static List<String> blockedWorlds;
    /**
     * False is pvp on. True is pvp off
     * -- GETTER --
     *  Get the PVPPlayers
     *

     */
    @Getter
    private static final HashMap<UUID,Boolean> PVPPlayers = new HashMap<>();
    /**
     * -- GETTER --
     *  Get the cooldowns
     *
     */
    @Getter
    private static final HashMap<UUID, Date> cooldowns = new HashMap<>();
    /**
     * -- GETTER --
     *  Get the PVPDataUtils
     *
     */
    @Getter
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

}
