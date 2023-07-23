package xyz.wisecraft.smp.modules.extra;

import xyz.wisecraft.smp.modules.extra.events.ExtraEvents;

import java.util.Objects;

/**
 * This class is used to create modules.
 */
public class ExtraModule implements xyz.wisecraft.smp.modulation.ModuleClass {
    @Override
    public void onEnable() {

    }

    @Override
    public void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new ExtraEvents(), plugin);
    }

    @Override
    public void registerCommands() {
        WisecraftCMD wiseCMD = new WisecraftCMD();
        Objects.requireNonNull(plugin.getCommand("wisecraft"), "command isn't registered").setExecutor(wiseCMD);
        Objects.requireNonNull(plugin.getCommand("wshop"), "command isn't registered").setExecutor(wiseCMD);
    }

}
