package xyz.wisecraft.smp.modules.tutorialstuff;

import net.ess3.api.IEssentials;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.core.WisecraftCoreApi;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modules.tutorialstuff.cmd.WisecraftCMD;
import xyz.wisecraft.smp.modules.tutorialstuff.listeners.ExtraListener;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is used to create modules.
 */
@SuppressWarnings("unused")
public class TutorialStuffModule extends ModuleClass {

    final IEssentials ess = setupDependency("Essentials", IEssentials.class);
    final WisecraftCoreApi core = setupDependency(WisecraftCoreApi.class);
    final boolean isMultiverseEnabled = setupDependency("Multiverse-Core");

    public TutorialStuffModule(long id) {
        super(id);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public @NotNull Set<Listener> registerListeners() {
        HashSet<Listener> listeners = new HashSet<>();
        listeners.add(new ExtraListener(isMultiverseEnabled));
        return listeners;
    }

    @Override
    public @NotNull Set<BukkitCommand> registerCommands() {
        HashSet<BukkitCommand> commands = new HashSet<>();
        commands.add(new WisecraftCMD("wisecraft", core, ess, isMultiverseEnabled));
        return commands;
    }

}
