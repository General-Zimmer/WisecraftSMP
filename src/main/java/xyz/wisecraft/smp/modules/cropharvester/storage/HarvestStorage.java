package xyz.wisecraft.smp.modules.cropharvester.storage;

import org.bukkit.Material;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperMaps;

import java.util.HashMap;
import java.util.Map;

public abstract class HarvestStorage {

    private static Map<Material, String> tools;

    public static void addTool(Material material, String tool) {
        tools.put(material, tool);
    }

    public static HashMap<Material, String> getTools() {
        return new HashMap<>(tools);
    }

    public static void setTools(ModuleClass module) {
        tools = new StorageHelperMaps<>(module, "tools");
    }
}
