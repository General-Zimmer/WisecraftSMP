package xyz.wisecraft.smp;

import com.fren_gor.ultimateAdvancementAPI.AdvancementMain;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modulation.UtilModuleCommon;
import xyz.wisecraft.smp.modulation.storage.ModuleSettings;
import xyz.wisecraft.smp.storage.OtherStorage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Main class for WisecraftSMP
 */
public class WisecraftSMP extends JavaPlugin {

    private static WisecraftSMP instance;
    private AdvancementMain advapi;
    private File moduleConfigFile;
    private FileConfiguration moduleConfig;
    private boolean isModulesEnabledByDefault;
    private final ArrayList<ModuleClass> modules = new ArrayList<>();

    /**
     * Production Constructor for WisecraftSMP
     */
    public WisecraftSMP() {
        super();
        isTesting = false;
        instance = this;
    }
    /**
     * test Constructor for WisecraftSMP
     */
    public WisecraftSMP(boolean isTesting) {
        super();
        this.isTesting = isTesting;
        instance = this;
    }

    private final Boolean isTesting;


    @Override
    public void onLoad() {
        if (isTesting) return;
        advapi = new AdvancementMain(this);
        advapi.load();
    }
    @Override
    public void onEnable() {

        // todo implement a way for config variables to only be added if they're enabled. Don't remove config variables
        //  ever. The check will use bit addition.




        // Config stuff
        this.saveDefaultConfig();
        createModuleConfig();
        OtherStorage.setServer_name(this.getConfig().getString("server_name"));

        isModulesEnabledByDefault = moduleConfig.getBoolean("IsModulesEnabledByDefault", false);

        // Fetching modules
        Reflections reflections = new Reflections("xyz.wisecraft.smp.modules");

        // Initialize modules
        setupModules(reflections.getSubTypesOf(ModuleClass.class));

        // setup modules
        setupModulesFromConfig();


        ArrayList<ModuleClass> unsortedModules = getModules();
        ArrayList<ModuleClass> sortedModules = new ArrayList<>();
        for (ModuleClass currentModule : unsortedModules) {
            ArrayList<Class<? extends ModuleClass>> moduleDepends = currentModule.getModuleDepends();
            // If module has no dependencies add it to the sorted modules
            if (moduleDepends == null) {
                sortedModules.add(currentModule);
                continue;
            }

            // todo implement a way for modules to have deeper dependencies
            // Put module dependencies infront of its depender.
            for (Class<? extends ModuleClass> moduleDepend : moduleDepends) {
                for (ModuleClass dependModule : unsortedModules) {
                    if (dependModule.getClass().equals(moduleDepend) && dependModule.getModuleDepends() == null) {
                        sortedModules.add(dependModule);
                    } else {
                        throw new RuntimeException("Module: " + currentModule.getModuleName() +
                                " has one or more level 2+ dependencies. This is not supported yet.");
                    }
                }

            }

        }

        modules.clear();
        for (ModuleClass module : sortedModules)
            if (module.startModule())
                modules.add(module);

        try {
            moduleConfig.save(moduleConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // Me: We don't do that here

        for (ModuleClass module : this.modules)
            module.stopModule();

    }



    private void setupModules(Set<Class<? extends ModuleClass>> modules) {

        for (Class<? extends ModuleClass> moduleClass : modules) {
            try {
                ModuleClass module = moduleClass.getConstructor().newInstance();

                this.modules.add(module);
            } catch (InstantiationException |
                     IllegalAccessException |
                     NoSuchMethodException |
                     InvocationTargetException e) {
                e.printStackTrace();
            }

        }
    }

    private void setupModulesFromConfig() {
        ConfigurationSection moduleSection = moduleConfig.getConfigurationSection(getModulePath());
        Set<String> map = (moduleSection != null) ? moduleSection.getKeys(false) : null;

        if (map == null || map.isEmpty()) {
            setupModuleConfig();
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
        ArrayList<ModuleClass> modulesToBeAdded = new ArrayList<>();
        modules.forEach(module -> {
            if (!modulesInConfig.contains(module.getModuleName())) {
                modulesToBeAdded.add(module);
            }
        });


        // Combining missing IDS and missing modules
        HashMap<Long, ModuleClass> mapModulesToBeAdded = new HashMap<>();
        for (int i = 0, j = 0; i < modulesToBeAdded.size(); i++) {

            if (i < missingIDS.size()) {
                mapModulesToBeAdded.put(missingIDS.get(i), modulesToBeAdded.get(i));
                continue;
            }

            mapModulesToBeAdded.put((long) modulesToBeAdded.size()+j, modulesToBeAdded.get(i));
            j++;

        }
        // Adding modules to config
        for (Map.Entry<Long, ModuleClass> module : mapModulesToBeAdded.entrySet()) {
            moduleConfig.set(UtilModuleCommon.getSetting(module.getValue(), ModuleSettings.ENABLED), isModulesEnabledByDefault);
            moduleConfig.set(UtilModuleCommon.getSetting(module.getValue(), ModuleSettings.ID), module.getKey());
        }

        // Check if unused module configurations should be removed
        if (!moduleConfig.getBoolean("Remove_Old_Module_Settings")) return;

        // Remove modules not present in the code
        ArrayList<String> modulesToBeRemoved = new ArrayList<>(modulesInConfig);
        modules.forEach(module -> {
            if (modulesInConfig.contains(module.getModuleName())) {
                modulesToBeRemoved.remove(module.getModuleName());
            }
        });

        for (String module : modulesToBeRemoved) {
            moduleConfig.set(getModulePath() + "." + module, null);
        }

    }

    private void setupModuleConfig() {
        for (int i = 0; i < modules.size(); i++) {
            ModuleClass module = modules.get(i);

            moduleConfig.set(UtilModuleCommon.getSetting(module, ModuleSettings.ENABLED), isModulesEnabledByDefault);
            moduleConfig.set(UtilModuleCommon.getSetting(module, ModuleSettings.ID), i);
        }
    }

    public FileConfiguration getModuleConfig() {
        return this.moduleConfig;
    }

    private void createModuleConfig() {
        moduleConfigFile = new File(getDataFolder(), "modules.yml");
        if (!moduleConfigFile.exists() ) {
            moduleConfigFile.getParentFile().mkdirs(); // This needs to be exactly here for it to work, idk why
            saveResource("modules.yml", false);
        }
        moduleConfig = YamlConfiguration.loadConfiguration(moduleConfigFile);
    }

    public AdvancementMain getAdv() {
        return advapi;
    }

    /**
     * Returns the module path for config.
     * @return module path for config.
     */
    public String getModulePath() {
        return ModuleSettings.PATH.toString();
    }

    /**
     * Returns the WisecraftSMP instance.
     * @return WisecraftSMP instance.
     */
    public static WisecraftSMP getInstance() {
        return instance;
    }

    public Boolean getIsTesting() {
        return isTesting;
    }

    public ArrayList<ModuleClass> getModules() {
        return new ArrayList<>(modules);
    }
}
