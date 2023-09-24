package xyz.wisecraft.smp.modules.jobsextra;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modulation.interfaces.Module;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modules.cropharvester.CropHarvesterModule;
import xyz.wisecraft.smp.modules.jobsextra.listeners.ExplorerListener;
import xyz.wisecraft.smp.modules.jobsextra.listeners.JobsFeatureListener;
import xyz.wisecraft.smp.modules.jobsextra.storage.JobsStorage;
import xyz.wisecraft.smp.modules.jobsextra.threads.RemoveOldElytraDrops;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is used to create modules.
 */
@SuppressWarnings("unused")
public class JobsExtrasModule extends ModuleClass {
    private final boolean jobs = setupDependency("Jobs");
    BukkitTask removeOldElytraDrops;

    public JobsExtrasModule(long id) {
        super(id);
    }

    @Override
    public void onEnable() {
        JobsStorage.clearBlacksmithCrafts();

        removeOldElytraDrops = new RemoveOldElytraDrops().runTaskTimerAsynchronously(plugin, 20*60*5, 20*60*5);
    }

    @Override
    public void onDisable() {
        removeOldElytraDrops.cancel();
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

    /**
     * Gets the module dependencies of other modules. This method should be overridden if the module has dependencies.
     * @return The module dependencies.
     */
    public ArrayList<Class<? extends Module>> getModuleDepends() {
        ArrayList<Class<? extends Module>> depends = new ArrayList<>();
        depends.add(CropHarvesterModule.class);
        return depends;
    }

    public Job getSpecificJob(String name) {
        for (Job job : Jobs.getJobs()) {
            if (job.getName().equalsIgnoreCase(name))
                return job;
        }
        return null;
    }
}
