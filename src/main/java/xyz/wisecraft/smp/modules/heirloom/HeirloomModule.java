package xyz.wisecraft.smp.modules.heirloom;

import org.bukkit.Bukkit;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modules.heirloom.cmd.CmdSetup;
import xyz.wisecraft.smp.modules.heirloom.listeners.BowListener;
import xyz.wisecraft.smp.modules.heirloom.recipes.GeneralRune;
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
        if ()
    }

    private void setup() {
        GBHeirlooms = new GBHeirlooms().runTaskTimerAsynchronously(plugin, 20*60*5, 20*60*5);
        GeneralRune.urMom();
        Bukkit.addRecipe(GeneralRune.getRecipe());
    }

    @Override
    public void onDisable() {
        GBHeirlooms.cancel();
        Bukkit.removeRecipe(GeneralRune.runeKey);
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
