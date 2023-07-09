package xyz.wisecraft.smp.storage;

import org.bukkit.Material;
import xyz.wisecraft.smp.features.savinggrace.Angel;

import java.util.HashMap;
import java.util.UUID;

public abstract class OtherStorage {
    /**
     * Tools used to harvest crops
      */
    private static final HashMap<Material, String> tools = new HashMap<>();
    /**
     * The name of the server
     */
    private static String server_name;
    /**
     * Angels for saving grace
     */
    private static final HashMap<UUID, Angel> angels = new HashMap<>();


    /**
     * Get tools for harvesting crops
     */
    public static HashMap<Material, String> getTools() {
        return new HashMap<>(tools);
    }
    /**
     * Add tools for harvesting crops
     */
    public static void addTool(Material material, String size) {
        tools.put(material, size);
    }

    public static HashMap<UUID, Angel> getAngels() {
        return angels;
    }

    public static String getServer_name() {
        return server_name;
    }

    public static void setServer_name(String server_name) {
        OtherStorage.server_name = server_name;
    }
}
