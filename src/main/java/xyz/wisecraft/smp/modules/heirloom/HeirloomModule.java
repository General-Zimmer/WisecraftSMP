package xyz.wisecraft.smp.modules.heirloom;

import org.bukkit.Bukkit;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modules.heirloom.cmd.CmdSetup;
import xyz.wisecraft.smp.modules.heirloom.listeners.BowListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HeirloomModule extends ModuleClass {

    // Coming soon
    // private BukkitRunnable GBHeirlooms

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public @NotNull Set<Listener> registerListeners() {
        Set<Listener> listeners = new HashSet<>();
        listeners.add(new BowListener());
        return listeners;
    }

    @Override
    public @NotNull Set<BukkitCommand> registerCommands() {
        Set<BukkitCommand> commands = new HashSet<>();
        commands.add(new CmdSetup("heirloom"));
        return commands;
    }
}
