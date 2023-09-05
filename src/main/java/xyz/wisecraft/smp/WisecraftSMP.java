package xyz.wisecraft.smp;

import com.fren_gor.ultimateAdvancementAPI.AdvancementMain;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modulation.UtilModuleCommon;
import xyz.wisecraft.smp.modulation.storage.ModuleSettings;
import xyz.wisecraft.smp.storage.OtherStorage;
import xyz.wisecraft.smp.util.UtilRandom;

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
    private final ArrayList<ModuleClass> modules = new ArrayList<>();

    /**
     * Production Constructor for WisecraftSMP
     */
    public WisecraftSMP() {
        isTesting = false;
        instance = this;
    }
    /**
     * test Constructor for WisecraftSMP
     */
    public WisecraftSMP(Boolean isTesting) {
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
        moduleConfig = createModuleConfig(this, this.getDataFolder().toString());
        OtherStorage.setServer_name(this.getConfig().getString("server_name"));

        boolean isModulesEnabledByDefault = moduleConfig.getBoolean("IsModulesEnabledByDefault", false);

        // Fetching modules
        Reflections reflections = new Reflections("xyz.wisecraft.smp.modules");

        // Initialize modules
        setupModules(reflections.getSubTypesOf(ModuleClass.class));

        // setup modules
        UtilRandom.setupModulesFromConfig(moduleConfig, modules, isModulesEnabledByDefault, getModulePath()); // todo prevent comments from being removed


        ArrayList<ModuleClass> sortedModules = UtilModuleCommon.sortDependTrimmed(modules);

        // Start/load modules
        modules.clear();
        for (int i = sortedModules.size(); i > 0; i--) {
            ModuleClass module = sortedModules.get(i-1);

            if (module.startModule())
                modules.add(module);
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



    public FileConfiguration getModuleConfig() {
        return this.moduleConfig;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static FileConfiguration createModuleConfig(Plugin plugin, File moduleConfigFile) {
        if (!moduleConfigFile.exists()) {
            moduleConfigFile.getParentFile().mkdirs(); // This needs to be exactly here for it to work
            plugin.saveResource("modules.yml", false);
        }
        return YamlConfiguration.loadConfiguration(moduleConfigFile);
    }

    public static FileConfiguration createModuleConfig(Plugin plugin, String getDataFolder) {
        return createModuleConfig(plugin, new File(getDataFolder, "modules.yml"));
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

    public File getModuleConfigFile() {
        return moduleConfigFile;
    }

    public ArrayList<ModuleClass> getModules() {
        return new ArrayList<>(modules);
    }
}
