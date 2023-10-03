package xyz.wisecraft.smp.modules.cropharvester;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modules.cropharvester.listener.CropTrampleListener;
import xyz.wisecraft.smp.modules.cropharvester.listener.HarvestListener;
import xyz.wisecraft.smp.modules.cropharvester.storage.HarvestStorage;
import xyz.wisecraft.smp.storage.OtherStorage;

import java.util.*;

/**
 * CropHarvesterModule
 */
public class CropHarvesterModule extends ModuleClass {
    private final boolean isGriefpreventionEnabled = setupDependency("GriefPrevention");
    private final boolean isWorldGuardEnabled = setupDependency("WorldGuard");
    private final boolean isTownyEnabled = setupDependency("Towny");
    @Getter
    static CropHarvesterModule instance;

    public CropHarvesterModule(long ID) {
        super(ID);
        instance = this;
    }

    @Override
    public void onEnable() {

        HarvestStorage.setTools(this);

        // Harvest lists for Harvest logic
        List<String> tools = plugin.getConfig().getStringList("FARM_SETTINGS");

        for (String material: tools) {
            String[] string = material.split(" ");
            Material material1 = Material.getMaterial(string[1]);
            HarvestStorage.addTool(material1, string[0]);
        }
    }

    @Override
    public @NotNull Set<Listener> registerListeners() {
        HashSet<Listener> listeners = new HashSet<>();
        listeners.add(new HarvestListener());

        if (plugin.getConfig().getBoolean("SETTINGS.ANTI_TRAMPLE_CROPS", false)) {
            listeners.add(new CropTrampleListener());
        }
        return listeners;
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
