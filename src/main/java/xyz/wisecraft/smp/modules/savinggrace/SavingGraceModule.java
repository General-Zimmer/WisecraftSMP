package xyz.wisecraft.smp.modules.savinggrace;

import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperMaps;
import xyz.wisecraft.smp.modules.savinggrace.listeners.AngelListeners;
import xyz.wisecraft.smp.modules.savinggrace.models.Angel;
import xyz.wisecraft.smp.modules.savinggrace.storage.AngelStorage;

import java.util.HashMap;
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
        AngelStorage.setAngels(new StorageHelperMaps<>(this, "angels", new HashMap<>()));

        for (Player p : Bukkit.getOnlinePlayers()) {
            Angel angel = new Angel(p);
            AngelStorage.getAngels().put(p.getUniqueId(), angel);
        }
    }

    @Override
    public @NotNull Set<Listener> registerListeners() {
        HashSet<Listener> listeners = new HashSet<>();
        listeners.add(new AngelListeners(ess));
        return listeners;
    }

}
