package xyz.wisecraft.smp.modules.jobsextra.storage;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public abstract class JobsStorage {

    private static final ArrayList<Material> blacksmithCrafts = new ArrayList<>();

    public static ArrayList<Material> getBlacksmithCrafts() {
        return new ArrayList<>(blacksmithCrafts);
    }

    public static void clearBlacksmithCrafts() {
        blacksmithCrafts.clear();
    }
    public static void addBlacksmithCraft(Material material) {
        blacksmithCrafts.add(material);
    }

    private static final HashMap<UUID, Date> elytraDrop = new HashMap<>();

    public static HashMap<UUID, Date> getElytraDrop() {
        return new HashMap<>(elytraDrop);
    }

    public static void addElytraDrop(UUID uuid, Date date) {
        elytraDrop.put(uuid, date);
    }

    @SuppressWarnings("unused")
    public static void removeElytraDrop(UUID uuid) {
        elytraDrop.remove(uuid);
    }
}
