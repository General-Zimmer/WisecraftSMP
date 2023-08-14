package xyz.wisecraft.smp.modules.extra;

import net.ess3.api.IEssentials;
import xyz.wisecraft.core.WisecraftCoreApi;
import xyz.wisecraft.smp.modules.extra.listeners.ExtraListener;

import java.util.Objects;

/**
 * This class is used to create modules.
 */
public class ExtraModule implements xyz.wisecraft.smp.modulation.ModuleClass {

    IEssentials ess = setupDependency("Essentials", IEssentials.class);
    WisecraftCoreApi core = setupDependency(WisecraftCoreApi.class);
    @Override
    public void onEnable() {

    }

    @Override
    public void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new ExtraListener(), plugin);
    }

    @Override
    public void registerCommands() {
        WisecraftCMD wiseCMD = new WisecraftCMD(core, ess);
        Objects.requireNonNull(plugin.getCommand("wisecraft"), "command isn't registered").setExecutor(wiseCMD);
        Objects.requireNonNull(plugin.getCommand("wshop"), "command isn't registered").setExecutor(wiseCMD);
    }

}
