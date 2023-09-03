package xyz.wisecraft.smp.modules.jobsextra.threads;

import org.bukkit.scheduler.BukkitRunnable;
import xyz.wisecraft.smp.modules.jobsextra.storage.JobsStorage;
import xyz.wisecraft.smp.util.UtilCommon;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RemoveOldElytraDrops extends BukkitRunnable {
    @Override
    public void run() {
        HashMap<UUID, Date> elytraDrops = JobsStorage.getElytraDrop();


        for (Map.Entry<UUID, Date> entry : elytraDrops.entrySet()) {
            UUID uuid = entry.getKey();
            Date dropDate = entry.getValue();
            if (UtilCommon.calcCurrentSeconds(dropDate) < 60 * 5) {
                elytraDrops.remove(uuid);
            }
        }

    }
}
