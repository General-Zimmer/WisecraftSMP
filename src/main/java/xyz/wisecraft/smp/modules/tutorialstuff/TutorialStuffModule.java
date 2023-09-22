package xyz.wisecraft.smp.modules.tutorialstuff;

import net.ess3.api.IEssentials;
import org.bukkit.event.Listener;
import xyz.wisecraft.core.WisecraftCoreApi;
import xyz.wisecraft.smp.modules.tutorialstuff.listeners.ExtraListener;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class is used to create modules.
 */
public class TutorialStuffModule implements xyz.wisecraft.smp.modulation.ModuleClass {

    IEssentials ess = setupDependency("Essentials", IEssentials.class);
    WisecraftCoreApi core = setupDependency(WisecraftCoreApi.class);
    boolean isMultiverseEnabled = setupDependency("Multiverse-Core");
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
    public void registerCommands() {
        WisecraftCMD wiseCMD = new WisecraftCMD(core, ess, isMultiverseEnabled);
        Objects.requireNonNull(plugin.getCommand("wisecraft"), "command isn't registered").setExecutor(wiseCMD);
        Objects.requireNonNull(plugin.getCommand("wshop"), "command isn't registered").setExecutor(wiseCMD);
    }

}
