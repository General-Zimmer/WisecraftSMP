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
import xyz.wisecraft.smp.modules.togglepvp.utils.PlaceholderAPIHook;
import xyz.wisecraft.smp.storage.OtherStorage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class for WisecraftSMP
 */
public final class WisecraftSMP extends JavaPlugin {

    private static WisecraftSMP instance;
    private IEssentials ess;
    private WisecraftCoreApi core;
    private LuckPerms luck;
    private boolean isTimberEnabled = false;
    private AdvancementMain advapi;
    private File moduleConfigFile;
    private FileConfiguration moduleConfig;
    private final String modulePath = "modules.";
    private boolean isModulesEnabledByDefault;


    /**
     * Production Constructor for WisecraftSMP
     */
    public WisecraftSMP() {
        instance = this;
    }

    private final ArrayList<ModuleClass> modules = new ArrayList<>();

    @Override
    public void onLoad() {
        advapi = new AdvancementMain(this);
        advapi.load();

        // Rest of your code
    }
    @Override
    public void onEnable() {
        // todo add a sub main for every feature and add a config to disable and enable them.

        // todo implement a way for config variables to only be added if they're enabled. Don't remove config variables
        //  ever. The check will use bit addition.

        // Dependencies always first
        setupEssentials();
        setupWisecraftCore();
        setupLuckPerms();
        setupPAPI();
        setupTimber();

        // todo
        createModuleConfig();

        // Config stuff
        this.saveDefaultConfig();
        OtherStorage.setServer_name(this.getConfig().getString("server_name"));

        isModulesEnabledByDefault = moduleConfig.getBoolean("IsModulesEnabledByDefault", false);

        // Fetching modules
        Reflections reflections = new Reflections("xyz.wisecraft.smp.modules");

        // Initialize modules
        setupModules(reflections.getSubTypesOf(ModuleClass.class));

        // setup modules
        if (moduleConfig.get(modulePath.split("\\.")[0]) != null) {
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


    private void setupEssentials() {
        Plugin setupPlugin = getServer().getPluginManager().getPlugin("Essentials");
        if (setupPlugin == null) {return;}
        if(!setupPlugin.isEnabled()) {return;}

        ess = (Essentials) this.getServer().getPluginManager().getPlugin("Essentials");
    }

    private void setupWisecraftCore() {
        String name = "WisecraftCore";

        RegisteredServiceProvider<WisecraftCoreApi> provider = getServer().getServicesManager().getRegistration(WisecraftCoreApi.class);
        if (provider != null) {
            core = provider.getProvider();
            return;
        }
        Logger.getLogger("wisecraft").log(Level.WARNING, "Couldn't get " + name + " provider");
    }
    private void setupTimber() {
        Plugin setupPlugin = getServer().getPluginManager().getPlugin("UltimateTimber");
        if (setupPlugin == null) {return;}
        isTimberEnabled = setupPlugin.isEnabled();
    }
    private void setupLuckPerms() {
        String name = "LuckPerms";


        RegisteredServiceProvider<LuckPerms> provider = getServer().getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luck = provider.getProvider();
            return;
        }
        Logger.getLogger("wisecraft").log(Level.WARNING, "Couldn't get " + name + " provider");
    }

    private void setupPAPI() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPIHook().register();
        }
    }

    private void setupModules(Set<Class<? extends ModuleClass>> modules) {

        for (Class<? extends ModuleClass> module : modules) {
            try {
                this.modules.add(module.getConstructor().newInstance());

            } catch (InstantiationException |
                     IllegalAccessException |
                     NoSuchMethodException |
                     InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupModulesFromConfig() {
        MemorySection mem = (MemorySection) moduleConfig.get(getModulePath().split("\\.")[0]);
        ArrayList<String> modulesInConfig = new ArrayList<>(mem.getKeys(false));


        // Getting missing IDs
        ArrayList<Long> IDs = new ArrayList<>();
        modulesInConfig.forEach(module -> {
            if (moduleConfig.get(getModulePath() + module + ".id") != null) {
                IDs.add(moduleConfig.getLong(getModulePath() + module + ".id"));
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
            moduleConfig.set(modulePath + module + ".enabled", isModulesEnabledByDefault);
            moduleConfig.set(modulePath + module + ".id", module.getKey());
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
            moduleConfig.set(modulePath + module, null);
        }

    }

    private void setupModuleConfig() {
        for (int i = 0; i < modules.size(); i++) {
            String moduleName = modules.get(i).getModuleName();
            moduleConfig.set(modulePath + moduleName + ".enabled", isModulesEnabledByDefault);
            moduleConfig.set(modulePath + moduleName + ".id", i);
            modules.get(i).startModule();
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
        return modulePath;
    }

    /**
     * Returns the WisecraftSMP instance.
     * @return WisecraftSMP instance.
     */
    public static WisecraftSMP getInstance() {
        return instance;
    }

    /**
     * Returns the Essentials plugin instance.
     * @return Essentials plugin instance.
     */
    public static IEssentials getEss() {
        return instance.ess;
    }

    /**
     * Returns the WisecraftCoreApi instance.
     * @return WisecraftCoreApi instance.
     */
    public static WisecraftCoreApi getCore() {
        return instance.core;
    }

    /**
     * Returns the LuckPerms instance.
     * @return LuckPerms instance.
     */
    public static LuckPerms getLuck() {
        return instance.luck;
    }

    /**
     * Returns the modules list.
     * @return modules list.
     */
    public boolean isTimberEnabled() {
        return isTimberEnabled;
    }

    public File getModuleConfigFile() {
        return moduleConfigFile;
    }
}
