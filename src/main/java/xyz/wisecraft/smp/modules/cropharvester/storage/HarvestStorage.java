package xyz.wisecraft.smp.modules.cropharvester.storage;

import org.bukkit.Material;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperMaps;

import java.util.HashMap;

public abstract class HarvestStorage {

    private static StorageHelperMaps<HashMap<Material, String>, Material, String> tools;

    public static void addTool(Material material, String tool) {
        tools.put(material, tool);
    }

    public static HashMap<Material, String> getTools() {
        return new HashMap<>(tools.get());
    }

    public static void setTools(ModuleClass module) {
        tools = new StorageHelperMaps<>(module, "tools", new HashMap<>());
    }
}
