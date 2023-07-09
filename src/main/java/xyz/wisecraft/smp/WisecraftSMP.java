package xyz.wisecraft.smp;

import com.earth2me.essentials.Essentials;
import net.ess3.api.IEssentials;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.wisecraft.core.WisecraftCoreApi;
import xyz.wisecraft.smp.features.advancements.Command;
import xyz.wisecraft.smp.features.advancements.listeners.Ibba;
import xyz.wisecraft.smp.features.advancements.listeners.QuestListeners;
import xyz.wisecraft.smp.features.advancements.listeners.timberListeners;
import xyz.wisecraft.smp.features.advancements.threads.gibRoles;
import xyz.wisecraft.smp.features.cropharvester.listener.HarvestListener;
import xyz.wisecraft.smp.extra.WisecraftCMD;
import xyz.wisecraft.smp.features.savinggrace.listeners.AngelListeners;
import xyz.wisecraft.smp.storage.OtherStorage;
import xyz.wisecraft.smp.storage.PVPStorage;
import xyz.wisecraft.smp.features.togglepvp.PVPCMD;
import xyz.wisecraft.smp.features.togglepvp.listeners.PVPTimberListener;
import xyz.wisecraft.smp.features.togglepvp.listeners.PlayerListener;
import xyz.wisecraft.smp.features.togglepvp.listeners.PvPListener;
import xyz.wisecraft.smp.features.togglepvp.utils.PersistentData;
import xyz.wisecraft.smp.features.togglepvp.utils.PlaceholderAPIHook;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class WisecraftSMP extends JavaPlugin {

    private static WisecraftSMP instance;
    private IEssentials ess;
    private WisecraftCoreApi core;
    private LuckPerms luck;

    public WisecraftSMP() {
        instance = this;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {

        // This always first
        setupEssentials();
        setupWisecraftCore();
        setupLuckPerms();
        setupPAPI();

        //Config stuff
        this.saveDefaultConfig();
        OtherStorage.setServer_name(this.getConfig().getString("server_name"));


        // Then these events
        this.getServer().getPluginManager().registerEvents(new AngelListeners(), this);
        this.getServer().getPluginManager().registerEvents(new QuestListeners(), this);
        this.getServer().getPluginManager().registerEvents(new Ibba(), this);
        if (setupTimber()) {
            this.getServer().getPluginManager().registerEvents(new timberListeners(), this);
            this.getServer().getPluginManager().registerEvents(new PVPTimberListener(), this);
        }
        this.getServer().getPluginManager().registerEvents(new HarvestListener(), this);

        // register events PVPToggle
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new PvPListener(), this);

        // Register commands
        WisecraftCMD wiseCMD = new WisecraftCMD();
        this.getCommand("wisecraft").setExecutor(wiseCMD);
        this.getCommand("wshop").setExecutor(wiseCMD);
        this.getCommand("autoroles").setExecutor(new Command());
        this.getCommand("pvp").setExecutor(new PVPCMD());

        // Check for new citizens. This is async right after this step.
        new gibRoles().runTaskTimer(this, 18000, 18000);

        // PVPToggle data
        File PVPData = new File(getDataFolder(), "togglepvp");
        PVPStorage.setPVPDataUtils(new PersistentData(PVPData));

        PVPStorage.setBlockedWorlds(this.getConfig().getStringList("SETTINGS.BLOCKED_WORLDS"));

        // Harvest lists for Harvest logic
        List<String> tools = instance.getConfig().getStringList("FARM_SETTINGS");

        for (String material: tools) {
            String[] string = material.split(" ");
            Material material1 = Material.getMaterial(string[1]);
            OtherStorage.addTool(material1, string[0]);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // Me: We don't do that here
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
    private boolean setupTimber() {
        Plugin setupPlugin = getServer().getPluginManager().getPlugin("UltimateTimber");
        if (setupPlugin == null) {return false;}
        return setupPlugin.isEnabled();
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

    public static WisecraftSMP getInstance() {
        return instance;
    }

    public static IEssentials getEss() {
        return instance.ess;
    }

    public static WisecraftCoreApi getCore() {
        return instance.core;
    }

    public static LuckPerms getLuck() {
        return instance.luck;
    }


}
