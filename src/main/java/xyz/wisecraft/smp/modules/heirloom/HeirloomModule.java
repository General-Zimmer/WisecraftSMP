package xyz.wisecraft.smp.modules.heirloom;

import org.bukkit.Bukkit;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modules.heirloom.cmd.CmdSetup;
import xyz.wisecraft.smp.modules.heirloom.listeners.BowListener;

import java.util.ArrayList;

public class HeirloomModule implements ModuleClass {

    // Coming soon
    // private BukkitRunnable GBHeirlooms

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        ModuleClass.super.onDisable();
    }

    @Override
    public void registerEvents() {
       plugin.getServer().getPluginManager().registerEvents(new BowListener(), plugin);
    }

    @Override
    public void registerCommands() {
        Bukkit.getCommandMap().register("heirloom", new CmdSetup("heirloom"));
    }
}
