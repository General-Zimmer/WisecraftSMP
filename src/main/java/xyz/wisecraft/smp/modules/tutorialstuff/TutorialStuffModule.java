package xyz.wisecraft.smp.modules.tutorialstuff;

import net.ess3.api.IEssentials;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Listener;
import xyz.wisecraft.core.WisecraftCoreApi;
import xyz.wisecraft.smp.modules.tutorialstuff.listeners.ExtraListener;

import java.util.ArrayList;

/**
 * This class is used to create modules.
 */
public class TutorialStuffModule implements xyz.wisecraft.smp.modulation.ModuleClass {

    final IEssentials ess = setupDependency("Essentials", IEssentials.class);
    final WisecraftCoreApi core = setupDependency(WisecraftCoreApi.class);
    final boolean isMultiverseEnabled = setupDependency("Multiverse-Core");
    @Override
    public void onEnable() {

    }

    @Override
    public ArrayList<Listener> registerListeners() {
        ArrayList<Listener> listeners = new ArrayList<>();
        listeners.add(new ExtraListener(isMultiverseEnabled));
        return listeners;
    }

    @Override
    public ArrayList<BukkitCommand> registerCommands() {
        ArrayList<BukkitCommand> commands = new ArrayList<>();
        commands.add(new WisecraftCMD("wisecraft", core, ess, isMultiverseEnabled));
        return commands;
    }

}
