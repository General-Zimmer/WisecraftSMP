package xyz.wisecraft.smp.modules.togglepvp;

import xyz.wisecraft.smp.modules.togglepvp.listeners.PVPTimberListener;
import xyz.wisecraft.smp.modules.togglepvp.listeners.PlayerListener;
import xyz.wisecraft.smp.modules.togglepvp.listeners.PvPListener;
import xyz.wisecraft.smp.modules.togglepvp.utils.PersistentData;
import xyz.wisecraft.smp.modules.togglepvp.storage.PVPStorage;

import java.io.File;

/**
 * Module class for PVPToggle
 */
public class PvPToggleModule implements xyz.wisecraft.smp.modulation.ModuleClass {
    @Override
    public void onEnable() {

        // PVPToggle data
        File PVPData = new File(plugin.getDataFolder(), "togglepvp");
        PVPStorage.setPVPDataUtils(new PersistentData(PVPData));

        PVPStorage.setBlockedWorlds(plugin.getConfig().getStringList("SETTINGS.BLOCKED_WORLDS"));

    }

    @Override
    public void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new PlayerListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PvPListener(), plugin);
        if (plugin.isTimberEnabled()) {
            plugin.getServer().getPluginManager().registerEvents(new PVPTimberListener(), plugin);
        }
    }

    @Override
    public void registerCommands() {

        plugin.getCommand("pvp").setExecutor(new PVPCMD());
    }
}
