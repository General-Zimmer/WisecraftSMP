package xyz.wisecraft.smp.modules.heirloom;

import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modules.heirloom.cmd.CmdSetup;
import xyz.wisecraft.smp.modules.heirloom.listeners.BowListener;
import xyz.wisecraft.smp.modules.heirloom.threads.GBHeirlooms;

import java.util.HashSet;
import java.util.Set;

public class HeirloomModule extends ModuleClass {

    // Coming soon
    // private BukkitRunnable GBHeirlooms
    BukkitTask GBHeirlooms;

    public HeirloomModule(long id) {
        super(id);
    }

    @Override
    public void onEnable() {
        GBHeirlooms = new GBHeirlooms().runTaskTimerAsynchronously(plugin, 20*60*5, 20*60*5);
    }

    @Override
    public void onDisable() {
        GBHeirlooms.cancel();
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
