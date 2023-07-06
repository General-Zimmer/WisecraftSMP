package xyz.wisecraft.smp;

import org.bukkit.Material;

import java.util.HashMap;

/**
 * Storage for the plugin to store data in RAM.
 */
public abstract class Storage {

    private static final HashMap<Material, String> tools = new HashMap<>();

    public static HashMap<Material, String> getTools() {
        return new HashMap<Material, String>(tools);
    }

    public static void addTool(Material material, String size) {
        tools.put(material, size);
    }
}
