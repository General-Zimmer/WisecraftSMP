package xyz.wisecraft.smp.modules.jobsextra;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import org.bukkit.scheduler.BukkitTask;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modules.cropharvester.CropHarvesterModule;
import xyz.wisecraft.smp.modules.jobsextra.listeners.ExplorerListener;
import xyz.wisecraft.smp.modules.jobsextra.listeners.JobsFeatureListener;
import xyz.wisecraft.smp.modules.jobsextra.threads.RemoveOldElytraDrops;

import java.util.ArrayList;

/**
 * This class is used to create modules.
 */
public class JobsExtrasModule implements xyz.wisecraft.smp.modulation.ModuleClass {
    private final boolean jobs = setupDependency("Jobs");
    BukkitTask removeOldElytraDrops;

    @Override
    public void onEnable() {

        removeOldElytraDrops = new RemoveOldElytraDrops().runTaskTimerAsynchronously(plugin, 20*60*5, 20*60*5);
    }

    @Override
    public void onDisable() {
        removeOldElytraDrops.cancel();
    }

    @Override
    public void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new JobsFeatureListener(this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ExplorerListener(this), plugin);
    }

    @Override
    public boolean hasAllHardDependencies() {
        return jobs;
    }

    /**
     * Gets the module dependencies of other modules. This method should be overridden if the module has dependencies.
     * @return The module dependencies.
     */
    public ArrayList<Class<? extends ModuleClass>> getModuleDepends() {
        ArrayList<Class<? extends ModuleClass>> depends = new ArrayList<>();
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
