package xyz.wisecraft.smp;

import com.earth2me.essentials.Essentials;
import net.ess3.api.IEssentials;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.wisecraft.core.WisecraftCoreApi;
import xyz.wisecraft.smp.advancements.Command;
import xyz.wisecraft.smp.advancements.events.Ibba;
import xyz.wisecraft.smp.advancements.events.QuestEvents;
import xyz.wisecraft.smp.advancements.events.timberEvents;
import xyz.wisecraft.smp.advancements.threads.gibRoles;
import xyz.wisecraft.smp.angel.Angel;
import xyz.wisecraft.smp.extra.WisecraftCMD;
import xyz.wisecraft.smp.angel.events.AngelEvents;
import xyz.wisecraft.smp.togglepvp.PVPCMD;
import xyz.wisecraft.smp.togglepvp.PlayerJoin;
import xyz.wisecraft.smp.togglepvp.listeners.PlayerChangeWorld;
import xyz.wisecraft.smp.togglepvp.listeners.PlayerLeave;
import xyz.wisecraft.smp.togglepvp.listeners.PvP;
import xyz.wisecraft.smp.togglepvp.utils.PersistentData;
import xyz.wisecraft.smp.togglepvp.utils.PlaceholderAPIHook;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class WisecraftSMP extends JavaPlugin {

    //todo make these fields private
    public static WisecraftSMP instance = null;
    public static IEssentials ess = null;
    public static WisecraftCoreApi core = null;
    public static LuckPerms luck = null;
    public static final HashMap<UUID, Angel> gearMap = new HashMap<>();
    public static String server_name;

    // PVPToggle stuff

    public FileConfiguration config;
    public static List<String> blockedWorlds;
    /**
     * //False is pvp on. True is pvp off
     */
    public HashMap<UUID,Boolean> players = new HashMap<>();
    public HashMap<UUID, Date> cooldowns = new HashMap<>();

    public PersistentData dataUtils;

    public WisecraftSMP() {
        instance = this;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {

        this.config = getConfig();

        // This always first
        setupEssentials();
        setupWisecraftCore();
        setupLuckPerms();
        setupPAPI();

        //Config stuff
        this.saveDefaultConfig();
        this.setServer_name(this.getConfig().getString("server_name"));


        // Then these events
        this.getServer().getPluginManager().registerEvents(new AngelEvents(), this);
        this.getServer().getPluginManager().registerEvents(new QuestEvents(), this);
        this.getServer().getPluginManager().registerEvents(new Ibba(), this);
        if (setupTimber())
            this.getServer().getPluginManager().registerEvents(new timberEvents(), this);


        // Register commands
        WisecraftCMD wiseCMD = new WisecraftCMD();
        this.getCommand("wisecraft").setExecutor(wiseCMD);
        this.getCommand("wshop").setExecutor(wiseCMD);
        this.getCommand("autoroles").setExecutor(new Command());

        // Check for new citizens. This is async right after this step.
        new gibRoles().runTaskTimer(this, 18000, 18000);

        // PVPToggle data
        File PVPData = new File(getDataFolder(), "togglepvp");
        dataUtils = new PersistentData(PVPData);

        // register events PVPToggle
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLeave(), this);
        Bukkit.getPluginManager().registerEvents(new PvP(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChangeWorld(), this);
        // register command
        this.getCommand("pvp").setExecutor(new PVPCMD());

        blockedWorlds = config.getStringList("SETTINGS.BLOCKED_WORLDS");


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
        Bukkit.getConsoleSender().sendMessage("Couldn't get " + name + " provider");
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
        Bukkit.getConsoleSender().sendMessage("Couldn't get " + name + " provider");
    }

    private void setupPAPI() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPIHook(this).register();
        }
    }

    public HashMap<UUID, Angel> getGearmap() {
        return gearMap;
    }

    public String getServer_name() {return server_name;}
    public void setServer_name(String name) { server_name = name;}
}
