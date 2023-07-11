package xyz.wisecraft.smp.storage;

import org.bukkit.Material;
import xyz.wisecraft.smp.modules.savinggrace.Angel;

import java.util.HashMap;
import java.util.UUID;

/**
 * Storage for the plugin to store data in RAM.
 */
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
     * @return HashMap of tools
     */
    public static HashMap<Material, String> getTools() {
        return new HashMap<>(tools);
    }

    /**
     * Add tools for harvesting crops
     * @param material The material of the tool
     * @param size The size of the tool
     */
    public static void addTool(Material material, String size) {
        tools.put(material, size);
    }

    /**
     * Get the angels
     * @return HashMap of angels
     */
    public static HashMap<UUID, Angel> getAngels() {
        return angels;
    }

    /**
     * Get the server name
     * @return The name of the server
     */
    public static String getServer_name() {
        return server_name;
    }

    /**
     * Set the server name
     * @param server_name The name of the server
     */
    public static void setServer_name(String server_name) {
        OtherStorage.server_name = server_name;
    }
}
