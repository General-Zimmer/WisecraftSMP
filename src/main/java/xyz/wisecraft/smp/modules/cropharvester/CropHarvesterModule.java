package xyz.wisecraft.smp.modules.cropharvester;

import org.bukkit.Material;
import xyz.wisecraft.smp.modules.cropharvester.listener.CropTrampleListener;
import xyz.wisecraft.smp.modules.cropharvester.listener.HarvestListener;
import xyz.wisecraft.smp.storage.OtherStorage;

import java.util.List;

/**
 * CropHarvesterModule
 */
public class CropHarvesterModule implements xyz.wisecraft.smp.modulation.ModuleClass {
    @Override
    public void onEnable() {

        // Harvest lists for Harvest logic
        List<String> tools = plugin.getConfig().getStringList("FARM_SETTINGS");

        for (String material: tools) {
            String[] string = material.split(" ");
            Material material1 = Material.getMaterial(string[1]);
            OtherStorage.addTool(material1, string[0]);
        }
    }

    @Override
    public void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new HarvestListener(), plugin);
        if (plugin.getConfig().getString("ANTI_TRAMPLE_CROPS").equals(true)) {
            plugin.getServer().getPluginManager().registerEvents(new CropTrampleListener(), plugin);
        }
    }

}
