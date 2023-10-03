package xyz.wisecraft.smp.modules.jobsextra.storage;

import org.bukkit.Material;
import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperCollection;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

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

    private static StorageHelperCollection<ArrayList<Material>, Material> blacksmithCrafts;

    public static void setBlacksmithCrafts(ModuleClass module) {
        blacksmithCrafts = new StorageHelperCollection<>(module, "blacksmithCrafts", new ArrayList<>());
    }

    public static ArrayList<Material> getBlacksmithCrafts() {
        return new ArrayList<>(blacksmithCrafts);
    }

    public static void addBlacksmithCraft(Material material) {
        blacksmithCrafts.add(material);
    }
}
