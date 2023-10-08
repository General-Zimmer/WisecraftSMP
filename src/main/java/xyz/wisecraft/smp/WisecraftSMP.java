package xyz.wisecraft.smp;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modulation.UtilModuleCommon;
import xyz.wisecraft.smp.modulation.cmd.ManageModules;
import xyz.wisecraft.smp.modulation.enums.ModuleState;
import xyz.wisecraft.smp.modulation.interfaces.Module;
import xyz.wisecraft.smp.modulation.storage.ModulationStorage;
import xyz.wisecraft.smp.modulation.storage.ModuleSettings;
import xyz.wisecraft.smp.storage.OtherStorage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;

import static xyz.wisecraft.smp.modulation.UtilModuleCommon.*;
import static xyz.wisecraft.smp.util.UtilRandom.setupModulesFromConfig;

/**
 * Main class for WisecraftSMP
 */
public class WisecraftSMP extends JavaPlugin {

    @Getter
    private static WisecraftSMP instance;
    @Getter
    private FileConfiguration moduleConfig;
    @Getter
    private final Boolean isTesting;
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

    @Override
    public void onLoad() {
        if (isTesting) return; // Prevents the plugin from loading if it's in testing mode
    }
    @Override
    public void onEnable() {
        if (getIsTesting()) return; // Prevents the plugin from loading if it's in testing mode


        registerCommand(new ManageModules(ManageModules.class.getSimpleName().toLowerCase()), "WisecraftSMP");

        // Fetching modules classes
        Reflections reflections = new Reflections("xyz.wisecraft.smp.modules");
        Set<Class<? extends ModuleClass>> moduleClazzes = reflections.getSubTypesOf(ModuleClass.class);

        // Config stuff

        this.saveDefaultConfig();

        OtherStorage.setServer_name(this.getConfig().getString("server_name"));

        // Module config stuff
        File moduleConfigFile = new File(this.getDataFolder().toString(), "modules.yml");
        moduleConfig = createModuleConfig(this, moduleConfigFile);
        boolean isModulesEnabledByDefault = moduleConfig.getBoolean("IsModulesEnabledByDefault", false);
        setupModulesFromConfig(moduleConfig, moduleClazzes, isModulesEnabledByDefault, getModulePath()); // todo prevent comments from being removed
        // Save config
        try {
            moduleConfig.save(moduleConfigFile);
        } catch (IOException e) {
            this.getLogger().log(java.util.logging.Level.SEVERE, "Could not save config to " + moduleConfigFile, e);
        }

        // Initialize modules
        ArrayList<ModuleClass> unsortedModules = setupModules(moduleClazzes);

        // setup modules
        ArrayList<ModuleClass> sortedModules = sortDependTrimmed(unsortedModules);

        // Start/load modules
        sortedModules.forEach(module -> {
            try {
                if (module.enableModule())
                    ModulationStorage.addModule(module);
            } catch (Exception e) {
                if (module.isErrorMessage(e.getMessage(), ModuleState.DISABLED)) {
                    this.getLogger().log(Level.INFO, module.getModuleName() + " was not enabled");
                } else if (e.getMessage().equalsIgnoreCase("Module " + module.getModuleName() + " does not have all hard dependencies!")) {
                    this.getLogger().log(Level.WARNING, "Module " + module.getModuleName() + " does not have all hard dependencies!");
                }else {
                    this.getLogger().log(java.util.logging.Level.SEVERE, "Could not enable module " + module.getClass().getName(), e);
                }
            }
        });


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // Me: We don't do that here
        ModulationStorage.getModules().forEach(Module::disableModule);
    }

    private ArrayList<ModuleClass> setupModules(Set<Class<? extends ModuleClass>> moduleClazzes) {

        ArrayList<ModuleClass> modules = new ArrayList<>();

        for (Class<? extends ModuleClass> moduleClass : moduleClazzes) {
            try {
                ModuleClass module = moduleClass.getConstructor(Long.TYPE).newInstance(moduleConfig.getLong(UtilModuleCommon.getSetting(getModuleName(moduleClass), ModuleSettings.ID), -1));

                modules.add(module); // This is on a seperate line so it's easier to debug and
                // to make sure it's not added to the array in case of failure
            } catch (InstantiationException |
                     IllegalAccessException |
                     NoSuchMethodException |
                     InvocationTargetException e) {
                this.getLogger().log(java.util.logging.Level.SEVERE, "Could not instantiate module " + moduleClass.getSimpleName(), e);
            }
        }
        return modules;
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static FileConfiguration createModuleConfig(Plugin plugin, File moduleConfigFile) {
        if (!moduleConfigFile.exists()) {
            moduleConfigFile.getParentFile().mkdirs(); // This needs to be exactly here for it to work
            plugin.saveResource("modules.yml", false);
        }
        return YamlConfiguration.loadConfiguration(moduleConfigFile);
    }

    /**
     * Returns the module path for config.
     * @return module path for config.
     */
    public String getModulePath() {
        return ModuleSettings.PATH.toString();
    }

}
