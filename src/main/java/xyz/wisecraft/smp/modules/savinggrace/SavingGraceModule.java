package xyz.wisecraft.smp.modules.savinggrace;

import net.ess3.api.IEssentials;
import org.bukkit.event.Listener;
import xyz.wisecraft.smp.modules.savinggrace.listeners.AngelListeners;

import java.util.ArrayList;

/**
 * Module class for SavingGrace
 */
public class SavingGraceModule implements xyz.wisecraft.smp.modulation.ModuleClass {

    final IEssentials ess = setupDependency("Essentials", IEssentials.class);

    @Override
    public void onEnable() {
        // todo Take all online players and give them an Angel in case of a reload
    }

    @Override
    public ArrayList<Listener> registerListeners() {
        ArrayList<Listener> listeners = new ArrayList<>();
        listeners.add(new AngelListeners(ess));
        return listeners;
    }

}
