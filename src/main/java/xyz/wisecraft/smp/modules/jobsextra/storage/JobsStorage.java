package xyz.wisecraft.smp.modules.jobsextra.storage;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public abstract class JobsStorage {

    private static final HashMap<UUID, Date> elytraDrop = new HashMap<>();

    public static HashMap<UUID, Date> getElytraDrop() {
        return new HashMap<>(elytraDrop);
    }

    public static void addElytraDrop(UUID uuid, Date date) {
        elytraDrop.put(uuid, date);
    }

    public static void removeElytraDrop(UUID uuid) {
        elytraDrop.remove(uuid);
    }
}
