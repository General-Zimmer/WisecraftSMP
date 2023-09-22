package xyz.wisecraft.smp;

import com.fren_gor.ultimateAdvancementAPI.AdvancementMain;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modulation.models.ModuleInfo;
import xyz.wisecraft.smp.modulation.storage.ModulationStorage;
import xyz.wisecraft.smp.modulation.storage.ModuleSettings;
import xyz.wisecraft.smp.storage.OtherStorage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

import static xyz.wisecraft.smp.modulation.UtilModuleCommon.sortDependTrimmed;
import static xyz.wisecraft.smp.util.UtilRandom.setupModulesFromConfig;

/**
 * Main class for WisecraftSMP
 */
public class WisecraftSMP extends JavaPlugin {

    private static WisecraftSMP instance;
    private AdvancementMain advapi;
    private FileConfiguration moduleConfig;

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

        File moduleConfigFile = new File(this.getDataFolder().toString(), "modules.yml");
        moduleConfig = createModuleConfig(this, moduleConfigFile);
        OtherStorage.setServer_name(this.getConfig().getString("server_name"));

        boolean isModulesEnabledByDefault = moduleConfig.getBoolean("IsModulesEnabledByDefault", false);

        // Fetching modules
        Reflections reflections = new Reflections("xyz.wisecraft.smp.modules");

        // Initialize modules
        ArrayList<ModuleClass> unsortedModules = setupModules(reflections.getSubTypesOf(ModuleClass.class));

        // setup modules
        ArrayList<ModuleClass> sortedModules = sortDependTrimmed(unsortedModules);
        setupModulesFromConfig(moduleConfig, sortedModules, isModulesEnabledByDefault, getModulePath()); // todo prevent comments from being removed


        // Start/load modules
        sortedModules.forEach(module -> {
            ModuleInfo moduleInfo = module.enableModule();
            if (moduleInfo != null)
                ModulationStorage.addModule(module, moduleInfo);
        });

        try {
            moduleConfig.save(moduleConfigFile);
        } catch (IOException e) {
            this.getLogger().log(java.util.logging.Level.SEVERE, "Could not save config to " + moduleConfigFile, e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // Me: We don't do that here
        ModulationStorage.getModules().keySet().forEach(ModuleClass::disableModule);
    }



    private ArrayList<ModuleClass> setupModules(Set<Class<? extends ModuleClass>> moduleClazzes) {

        ArrayList<ModuleClass> modules = new ArrayList<>();

        for (Class<? extends ModuleClass> moduleClass : moduleClazzes) {
            try {
                ModuleClass module = moduleClass.getConstructor().newInstance();

                modules.add(module); // This is on a seperate line so it's easier to debug and
                // to make sure it's not added to the array in case of failure
            } catch (InstantiationException |
                     IllegalAccessException |
                     NoSuchMethodException |
                     InvocationTargetException e) {
                this.getLogger().log(java.util.logging.Level.SEVERE, "Could not instantiate module " + moduleClass.getName(), e);
            }
        }
        return modules;
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
}
