package xyz.wisecraft.smp;

import com.earth2me.essentials.Essentials;
import net.ess3.api.IEssentials;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.wisecraft.core.WisecraftCoreApi;
import xyz.wisecraft.smp.advancements.Command;
import xyz.wisecraft.smp.advancements.listeners.Ibba;
import xyz.wisecraft.smp.advancements.listeners.QuestListeners;
import xyz.wisecraft.smp.advancements.listeners.timberListeners;
import xyz.wisecraft.smp.advancements.threads.gibRoles;
import xyz.wisecraft.smp.cropharvester.listener.HarvestListener;
import xyz.wisecraft.smp.extra.WisecraftCMD;
import xyz.wisecraft.smp.savinggrace.Angel;
import xyz.wisecraft.smp.savinggrace.listeners.AngelListeners;
import xyz.wisecraft.smp.togglepvp.PVPCMD;
import xyz.wisecraft.smp.togglepvp.listeners.PVPTimberListener;
import xyz.wisecraft.smp.togglepvp.listeners.PlayerListener;
import xyz.wisecraft.smp.togglepvp.listeners.PvPListener;
import xyz.wisecraft.smp.togglepvp.utils.PersistentData;
import xyz.wisecraft.smp.togglepvp.utils.PlaceholderAPIHook;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class WisecraftSMP extends JavaPlugin {

    //todo make these fields private
    public static WisecraftSMP instance;
    public static IEssentials ess = null;
    public static WisecraftCoreApi core = null;
    public static LuckPerms luck = null;
    public static String server_name;
    // SavingGrace angels
    public static final HashMap<UUID, Angel> gearMap = new HashMap<>();

    // PVPToggle stuff
    public static List<String> blockedWorlds;
    /**
     * //False is pvp on. True is pvp off
     */
    public HashMap<UUID,Boolean> PVPPlayers = new HashMap<>();
    public HashMap<UUID, Date> cooldowns = new HashMap<>();

    public PersistentData PVPDataUtils;

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
        this.setServer_name(this.getConfig().getString("server_name"));


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
        PVPDataUtils = new PersistentData(PVPData);

        blockedWorlds = this.getConfig().getStringList("SETTINGS.BLOCKED_WORLDS");

        // Harvest lists for Harvest logic
        List<String> tools = instance.getConfig().getStringList("FARM_SETTINGS");

        for (String material: tools) {
            String[] string = material.split(" ");
            Material material1 = Material.getMaterial(string[1]);
            Storage.addTool(material1, string[0]);
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
            new PlaceholderAPIHook(this).register();
        }
    }

    public HashMap<UUID, Angel> getGearmap() {
        return gearMap;
    }

    public String getServer_name() {return server_name;}
    public void setServer_name(String name) { server_name = name;}
}
