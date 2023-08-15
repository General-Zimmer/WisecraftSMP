package xyz.wisecraft.smp.modules.jobsextra;

import xyz.wisecraft.smp.modules.jobsextra.listeners.JobsFeatureListener;

/**
 * This class is used to create modules.
 */
public class JobsExtrasModule implements xyz.wisecraft.smp.modulation.ModuleClass {

    private final boolean jobs = setupDependency("Jobs");


    @Override
    public void onEnable() {

    }

    @Override
    public void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new JobsFeatureListener(), plugin);
    }

    @Override
    public boolean hasAllHardDependencies() {
        return jobs;
    }

    
}
