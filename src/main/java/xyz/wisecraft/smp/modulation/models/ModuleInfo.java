package xyz.wisecraft.smp.modulation.models;

import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public record ModuleInfo(String moduleName, ArrayList<Listener> listeners, ArrayList<BukkitCommand> commands) {
}
