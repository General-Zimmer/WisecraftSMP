package xyz.wisecraft.smp;

import com.earth2me.essentials.Essentials;
import com.fren_gor.ultimateAdvancementAPI.AdvancementMain;
import net.ess3.api.IEssentials;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import xyz.wisecraft.core.WisecraftCoreApi;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modulation.storage.ModulationStorage;
import xyz.wisecraft.smp.modulation.storage.ModuleSettings;
import xyz.wisecraft.smp.modulation.UtilModuleCommon;
import xyz.wisecraft.smp.storage.OtherStorage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

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
        createModuleConfig();
        this.saveDefaultConfig();
        OtherStorage.setServer_name(this.getConfig().getString("server_name"));

        isModulesEnabledByDefault = moduleConfig.getBoolean("IsModulesEnabledByDefault", false);

        // Fetching modules
        Reflections reflections = new Reflections("xyz.wisecraft.smp.modules");

        // Initialize modules
        setupModules(reflections.getSubTypesOf(ModuleClass.class));

        // setup modules
        if (moduleConfig.get(getModulePath()) != null) {
            setupModuleConfig();
        } else {
            setupModulesFromConfig();
        }

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
        MemorySection mem = (MemorySection) moduleConfig.get(getModulePath());
        ArrayList<String> modulesInConfig = new ArrayList<>(mem.getKeys(false));


        // Getting missing IDs
        ArrayList<Long> IDs = new ArrayList<>();
        modulesInConfig.forEach(module -> {
            if (moduleConfig.get(UtilModuleCommon.getSetting(module, ModuleSettings.ID)) != null) {
                IDs.add(moduleConfig.getLong(UtilModuleCommon.getSetting(module, ModuleSettings.ID)));
            }
        });
        ArrayList<Long> missingIDS = new ArrayList<>();
        for (int i = 0; i < IDs.size(); i++) {
            if (i != IDs.get(i)) {
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

            if (missingIDS.get(i) < missingIDS.size()) {
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
        if (moduleConfig.getBoolean("Remove_Old_Module_Settings")) return;

        // Remove modules not present in the code
        ArrayList<String> modulesToBeRemoved = new ArrayList<>();
        modules.forEach(module -> {
            if (!modulesInConfig.contains(module.getModuleName())) {
                modulesToBeRemoved.add(module.getModuleName());
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
            module.startModule();
        }
    }

    public FileConfiguration getModuleConfig() {
        return this.moduleConfig;
    }

    private void createModuleConfig() {
        moduleConfigFile = new File(getDataFolder(), "modules.yml");
        if (!moduleConfigFile.exists()) {
            moduleConfigFile.getParentFile().mkdirs();
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

    public File getModuleConfigFile() {
        return moduleConfigFile;
    }

    public Boolean getIsTesting() {
        return isTesting;
    }

}
