package xyz.wisecraft.smp.storage;

import lombok.Getter;
import org.bukkit.Material;
import xyz.wisecraft.smp.modules.savinggrace.Angel;

import java.util.ArrayList;
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
     * -- GETTER --
     * Get the server name
     */
    @Getter
    private static String server_name;
    /**
     * Angels for saving grace
     * -- GETTER --
     * Get the angels
     */
    @Getter
    private static final HashMap<UUID, Angel> angels = new HashMap<>();
    private static final ArrayList<Material> blacksmithCrafts = new ArrayList<>();

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

    public static ArrayList<Material> getBlacksmithCrafts() {
        return new ArrayList<>(blacksmithCrafts);
    }

    public static void addBlacksmithCraft(Material material) {
        blacksmithCrafts.add(material);
    }

    /**
     * Set the server name
     * @param server_name The name of the server
     */
    public static void setServer_name(String server_name) {
        OtherStorage.server_name = server_name;
    }
}
