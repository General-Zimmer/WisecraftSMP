package xyz.wisecraft.smp.modules.cropharvester;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import xyz.wisecraft.smp.modules.cropharvester.listener.CropTrampleListener;
import xyz.wisecraft.smp.modules.cropharvester.listener.HarvestListener;
import xyz.wisecraft.smp.storage.OtherStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * CropHarvesterModule
 */
public class CropHarvesterModule implements xyz.wisecraft.smp.modulation.ModuleClass {
    private final boolean isGriefpreventionEnabled = setupDependency("GriefPrevention");
    private final boolean isWorldGuardEnabled = setupDependency("WorldGuard");
    private final boolean isTownyEnabled = setupDependency("Towny");
    static CropHarvesterModule instance;

    public CropHarvesterModule() {
        instance = this;
    }

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
    public ArrayList<Listener> registerListeners() {
        ArrayList<Listener> listeners = new ArrayList<>();
        listeners.add(new HarvestListener());

        if (plugin.getConfig().getBoolean("SETTINGS.ANTI_TRAMPLE_CROPS", false)) {
            listeners.add(new CropTrampleListener());
        }
        return listeners;
    }

    public static CropHarvesterModule getInstance() {
        return instance;
    }

    public boolean isGriefpreventionEnabled() {
        return isGriefpreventionEnabled;
    }

    public boolean isWorldGuardEnabled() {
        return isWorldGuardEnabled;
    }

    public boolean isTownyEnabled() {
        return isTownyEnabled;
    }
}
