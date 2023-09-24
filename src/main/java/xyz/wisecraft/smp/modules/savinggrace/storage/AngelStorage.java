package xyz.wisecraft.smp.modules.savinggrace.storage;

import lombok.Getter;
import org.bukkit.Material;
import xyz.wisecraft.smp.modules.savinggrace.models.Angel;

import java.util.HashMap;
import java.util.UUID;

public abstract class AngelStorage {
    public static void clearAngelStorage() {
        angels.clear();
        tools.clear();
    }

    @Getter
    private static final HashMap<UUID, Angel> angels = new HashMap<>();

    /**
     * Tools used to harvest crops
     */
    private static final HashMap<Material, String> tools = new HashMap<>();
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
}
