package xyz.wisecraft.smp.modules.extra;

import xyz.wisecraft.smp.modules.extra.events.ExtraEvents;

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
        plugin.getCommand("wisecraft").setExecutor(wiseCMD);
        plugin.getCommand("wshop").setExecutor(wiseCMD);
    }

}
