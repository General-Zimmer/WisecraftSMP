package xyz.wisecraft.smp.modules.savinggrace;

import net.ess3.api.IEssentials;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modules.savinggrace.listeners.AngelListeners;

import java.util.HashSet;
import java.util.Set;

/**
 * Module class for SavingGrace
 */
@SuppressWarnings("unused")
public class SavingGraceModule extends ModuleClass {

    final IEssentials ess = setupDependency("Essentials", IEssentials.class);

    public SavingGraceModule(long id) {
        super(id);
    }

    @Override
    public void onEnable() {
        // todo Take all online players and give them an Angel in case of a reload
    }

    @Override
    public @NotNull Set<Listener> registerListeners() {
        HashSet<Listener> listeners = new HashSet<>();
        listeners.add(new AngelListeners(ess));
        return listeners;
    }

}
