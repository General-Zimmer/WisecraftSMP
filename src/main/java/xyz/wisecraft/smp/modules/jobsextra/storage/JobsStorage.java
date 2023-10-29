package xyz.wisecraft.smp.modules.jobsextra.storage;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import org.bukkit.Material;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperCollection;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperMaps;
import xyz.wisecraft.smp.modules.jobsextra.JobsExtrasModule;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public abstract class JobsStorage {

    private static StorageHelperCollection<ArrayList<Material>, Material> blacksmithCrafts;
    private static StorageHelperMaps<HashMap<Job, Integer>, Job, Integer> jobLevels;

    public static void init(ModuleClass module) {
        blacksmithCrafts = new StorageHelperCollection<>(module, "blacksmithCrafts", new ArrayList<>());
        jobLevels = new StorageHelperMaps<>(module, "jobsLevel", new HashMap<>());
    }

    public static Job setJobLevel(ModuleClass module, String jobName) {
        int defaultLevel = module.plugin.getConfig().getInt("JOBS_SETTINGS.DEFAULT_ABILITY_LEVEL");
        Job job = getSpecificJob(jobName);
        jobLevels.put(job, module.plugin.getConfig().getInt("JOBS_SETTINGS."+jobName.toUpperCase()+"_ABILITY_LEVEL", defaultLevel));
        return job;
    }

    public static int getJobLevel(Job job) {
        return jobLevels.get(job);
    }



    public static ArrayList<Material> getBlacksmithCrafts() {
        return new ArrayList<>(blacksmithCrafts);
    }

    public static void addBlacksmithCraft(Material material) {
        blacksmithCrafts.add(material);
    }

    private static Job getSpecificJob(String name) {
        for (Job job : Jobs.getJobs()) {
            if (job.getName().equalsIgnoreCase(name))
                return job;
        }
        return null;
    }
}
