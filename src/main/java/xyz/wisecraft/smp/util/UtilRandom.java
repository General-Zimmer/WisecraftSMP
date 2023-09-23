package xyz.wisecraft.smp.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.wisecraft.smp.modulation.UtilModuleCommon;
import xyz.wisecraft.smp.modulation.models.ModuleClass;
import xyz.wisecraft.smp.modulation.storage.ModuleSettings;

import java.util.*;

import static xyz.wisecraft.smp.modulation.UtilModuleCommon.getModuleName;

public class UtilRandom {

    public static void setupModulesFromConfig(FileConfiguration moduleConfig, Set<Class<? extends ModuleClass>> modules, boolean isModulesEnabledByDefault, String getModulePath) {
        ConfigurationSection moduleSection = moduleConfig.getConfigurationSection(getModulePath);
        Set<String> map = (moduleSection != null) ? moduleSection.getKeys(false) : null;

        if (map == null || map.isEmpty()) {
            setupModuleConfig(modules, moduleConfig, isModulesEnabledByDefault);
            return;
        }

        ArrayList<String> modulesInConfig = new ArrayList<>(map);


        // Getting missing IDs
        ArrayList<Long> IDs = new ArrayList<>();
        modulesInConfig.forEach(module -> {
            if (!(moduleConfig.getLong(UtilModuleCommon.getSetting(module, ModuleSettings.ID), -1) == -1)) {
                IDs.add(moduleConfig.getLong(UtilModuleCommon.getSetting(module, ModuleSettings.ID)));
            }
        });
        ArrayList<Long> missingIDS = new ArrayList<>();
        for (int i = 0; i < modules.size(); i++) {
            if (!IDs.contains((long) i)) {
                missingIDS.add((long) i);
            }

        }

        // Getting missing modules
        ArrayList<Class<? extends ModuleClass>> modulesToBeAdded = new ArrayList<>();
        modules.forEach(module -> {
            if (!modulesInConfig.contains(getModuleName(module))) {
                modulesToBeAdded.add(module);
            }
        });


        // Combining missing IDS and missing modules
        HashMap<Long, Class<? extends ModuleClass>> mapModulesToBeAdded = new HashMap<>();
        for (int i = 0, j = 0; i < modulesToBeAdded.size(); i++) {

            if (i < missingIDS.size()) {
                mapModulesToBeAdded.put(missingIDS.get(i), modulesToBeAdded.get(i));
                continue;
            }

            mapModulesToBeAdded.put((long) modulesToBeAdded.size()+j, modulesToBeAdded.get(i));
            j++;

        }
        // Adding modules to config
        for (Map.Entry<Long, Class<? extends ModuleClass>> module : mapModulesToBeAdded.entrySet()) {
            moduleConfig.set(UtilModuleCommon.getSetting(getModuleName(module.getValue()), ModuleSettings.ENABLED), isModulesEnabledByDefault);
            moduleConfig.set(UtilModuleCommon.getSetting(getModuleName(module.getValue()), ModuleSettings.ID), module.getKey());
        }

        // Check if unused module configurations should be removed
        if (!moduleConfig.getBoolean("Remove_Old_Module_Settings")) return;

        // Remove modules not present in the code
        ArrayList<String> modulesToBeRemoved = new ArrayList<>(modulesInConfig);
        modules.forEach(module -> {
            if (modulesInConfig.contains(getModuleName(module))) {
                modulesToBeRemoved.remove(getModuleName(module));
            }
        });

        for (String module : modulesToBeRemoved) {
            moduleConfig.set(getModulePath + "." + module, null);
        }

    }

    private static void setupModuleConfig(Set<Class<? extends ModuleClass>> modules, FileConfiguration moduleConfig, boolean isModulesEnabledByDefault) {
        Iterator<Class<? extends ModuleClass>> iterator = modules.iterator();
        for (int i = 0; i < modules.size() && iterator.hasNext(); i++) {
            Class<? extends ModuleClass> module = iterator.next();

            moduleConfig.set(UtilModuleCommon.getSetting(getModuleName(module), ModuleSettings.ENABLED), isModulesEnabledByDefault);
            moduleConfig.set(UtilModuleCommon.getSetting(getModuleName(module), ModuleSettings.ID), i);
        }
    }
}
