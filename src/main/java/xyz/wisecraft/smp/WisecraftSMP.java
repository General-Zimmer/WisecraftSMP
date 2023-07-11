package xyz.wisecraft.smp;

import com.earth2me.essentials.Essentials;
import net.ess3.api.IEssentials;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import xyz.wisecraft.core.WisecraftCoreApi;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modules.togglepvp.utils.PlaceholderAPIHook;
import xyz.wisecraft.smp.storage.OtherStorage;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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

    /**
     * Production Constructor for WisecraftSMP
     */
    public WisecraftSMP() {
        instance = this;
    }

    private final ArrayList<ModuleClass> modules = new ArrayList<>();


    @Override
    public void onEnable() {
        // todo add a sub main for every feature and add a config to disable and enable them.
        // sub-main's needs to inherent from an interface that will have methods that sets themselves up with an Event.

        // todo implement a way for config variables to only be added if they're enabled. Don't remove config variables ever. The check will use bit addition.

        // Dependencies always first
        setupEssentials();
        setupWisecraftCore();
        setupLuckPerms();
        setupPAPI();
        setupTimber();

        //Config stuff
        this.saveDefaultConfig();
        OtherStorage.setServer_name(this.getConfig().getString("server_name"));

        // Creating modules
        Reflections reflections = new Reflections("xyz.wisecraft.smp.modules");

        setupModules(reflections.getSubTypesOf(ModuleClass.class));

        // Enable modules
        for (ModuleClass module : this.modules)
            module.startModule();



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
}
