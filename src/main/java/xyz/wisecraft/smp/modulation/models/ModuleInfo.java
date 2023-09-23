package xyz.wisecraft.smp.modulation.models;

import lombok.Data;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * This record is used to store information about a module.
 */
@Data
public class ModuleInfo {

    private final String moduleName;

    private final Set<Listener> listeners;
    private final Set<BukkitCommand> commands;


    public ModuleInfo(String moduleName, @NotNull Set<Listener> listeners, @NotNull Set<BukkitCommand> commands) {
        this.moduleName = moduleName;
        this.listeners = listeners;
        this.commands = commands;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void addCommand(BukkitCommand command) {
        commands.add(command);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public void removeCommand(BukkitCommand command) {
        commands.remove(command);
    }
}
