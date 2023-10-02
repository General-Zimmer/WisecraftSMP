package xyz.wisecraft.smp.modules.jobsextra.threads;

import org.bukkit.scheduler.BukkitRunnable;
import xyz.wisecraft.smp.modules.jobsextra.storage.JobsStorage;
import xyz.wisecraft.smp.util.UtilCommon;

import java.util.*;

public class RemoveOldElytraDrops extends BukkitRunnable {
    @Override
    public void run() {
        HashMap<UUID, Date> elytraDrops = JobsStorage.getElytraDrop();


        int timeToKeep = 60 * 60 * 2; // 2 hours, gives people plenty of time to pick up the elytra
        Iterator<Map.Entry<UUID, Date>> ite = elytraDrops.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<UUID, Date> entry = ite.next();
            Date dropDate = entry.getValue();
            if (UtilCommon.calcCurrentSeconds(dropDate) > timeToKeep) {
                ite.remove();
            }
        }

    }
}
