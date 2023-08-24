package xyz.wisecraft.smp.modules.jobsextra;

import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modules.cropharvester.CropHarvesterModule;
import xyz.wisecraft.smp.modules.jobsextra.listeners.JobsFeatureListener;

import java.util.ArrayList;

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

    /**
     * Gets the module dependencies of other modules. This method should be overridden if the module has dependencies.
     * @return The module dependencies.
     */
    public ArrayList<Class<? extends ModuleClass>> getModuleDepends() {
        ArrayList<Class<? extends ModuleClass>> depends = new ArrayList<>();
        depends.add(CropHarvesterModule.class);
        return depends;
    }

    
}
