package xyz.wisecraft.smp.modules.jobsextra;

import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modulation.interfaces.Module;
import xyz.wisecraft.smp.modules.cropharvester.CropHarvesterModule;
import xyz.wisecraft.smp.modules.jobsextra.listeners.ExplorerListener;
import xyz.wisecraft.smp.modules.jobsextra.listeners.JobsFeatureListener;
import xyz.wisecraft.smp.modules.jobsextra.storage.JobsStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is used to create modules.
 */
@SuppressWarnings("unused")
public class JobsExtrasModule extends ModuleClass {
    private final boolean jobs = setupDependency("Jobs");

    public JobsExtrasModule(long id) {
        super(id);
    }

    @Override
    public void onEnable() {

        JobsStorage.init(this);
        this.addModuleDepend(CropHarvesterModule.class);
    }


    @Override
    public @NotNull Set<Listener> registerListeners() {
        HashSet<Listener> listeners = new HashSet<>();
        listeners.add(new JobsFeatureListener(this));
        listeners.add(new ExplorerListener(this));
        return listeners;
    }

    @Override
    public boolean hasAllHardDependencies() {
        return jobs;
    }

}
