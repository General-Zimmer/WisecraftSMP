package xyz.wisecraft.smp;

import com.earth2me.essentials.Essentials;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.wisecraft.core.wisecraftcore.WisecraftCoreApi;
import xyz.wisecraft.smp.cmds.wisecraft;
import xyz.wisecraft.smp.events.Events;

import java.util.HashMap;
import java.util.UUID;

public final class WisecraftSMP extends JavaPlugin {

    private IEssentials ess;
    private final HashMap<UUID, Angel> gearMap = new HashMap<>();
    private WisecraftCoreApi core;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        WisecraftSMP plugin = this;

        //This always first
        setupEssentials();
        setupWisecraftCore();


        //Then this
        this.getServer().getPluginManager().registerEvents(new Events(plugin, ess), plugin);
        this.getCommand("wisecraft").setExecutor(new wisecraft(ess, core, this));
        this.getCommand("wshop").setExecutor(new wisecraft(ess, core, this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
            this.core = provider.getProvider();
            return;
        }
        Bukkit.getConsoleSender().sendMessage("Couldn't get " + name + " provider");
    }

    public HashMap<UUID, Angel> getGearmap() {
        return this.gearMap;
    }
}
