package xyz.wisecraft.smp.modules.jobsextra.storage;

import org.bukkit.Material;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperSet;

import java.util.*;

public abstract class JobsStorage {

    private static final HashMap<UUID, Date> elytraDrop = new HashMap<>(); // This doesn't have a helper since we want
                                                                            // to keep the data after the module is reloaded

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

    private static Set<Material> blacksmithCrafts;

    public static void setBlacksmithCrafts(ModuleClass module) {
        blacksmithCrafts = new StorageHelperSet<>(module, "blacksmithCrafts");
    }

    public static Set<Material> getBlacksmithCrafts() {
        return new HashSet<>(blacksmithCrafts);
    }

    public static void addBlacksmithCraft(Material material) {
        blacksmithCrafts.add(material);
    }
}
