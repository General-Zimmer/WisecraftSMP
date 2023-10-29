package xyz.wisecraft.smp.modules.heirloom;

import org.bukkit.Bukkit;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Recipe;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modules.heirloom.cmd.CmdSetup;
import xyz.wisecraft.smp.modules.heirloom.listeners.BowListener;
import xyz.wisecraft.smp.modules.heirloom.listeners.SmithingListener;
import xyz.wisecraft.smp.modules.heirloom.recipes.HeirloomRunes;
import xyz.wisecraft.smp.modules.heirloom.recipes.SmithRecipes;
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
        if (plugin.getServer().isPrimaryThread())
            setup();
        else
            plugin.getServer().getScheduler().runTask(plugin, this::setup);
    }

    private void setup() {
        GBHeirlooms = new GBHeirlooms().runTaskTimerAsynchronously(plugin, 20*60*5, 20*60*5);
        for (Recipe recipe : HeirloomRunes.setupRune()) {
            if (!Bukkit.addRecipe(recipe))
                plugin.getLogger().warning("Could not add smithing recipe for heirloom");
        }
        for (Recipe recipe : SmithRecipes.setupSmithRecipes()) {
            if (!Bukkit.addRecipe(recipe))
                plugin.getLogger().warning("Could not add smithing recipe for heirloom");
        }
    }

    @Override
    public void onDisable() {
        GBHeirlooms.cancel();
        Bukkit.removeRecipe(HeirloomRunes.runeKey);
        Bukkit.removeRecipe(SmithRecipes.smithKey);
    }

    @Override
    public @NotNull Set<Listener> registerListeners() {
        Set<Listener> listeners = new HashSet<>();
        listeners.add(new BowListener());
        listeners.add(new SmithingListener());
        return listeners;
    }

    @Override
    public @NotNull Set<BukkitCommand> registerCommands() {
        Set<BukkitCommand> commands = new HashSet<>();
        commands.add(new CmdSetup("heirloom"));
        return commands;
    }
}
