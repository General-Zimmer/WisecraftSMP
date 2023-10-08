package xyz.wisecraft.smp.modules.heirloom.storage;

import xyz.wisecraft.smp.modules.heirloom.heirlooms.BaseHeirloom;

import java.util.HashMap;
import java.util.UUID;

public abstract class HeirloomStorage {
    private final static HashMap<UUID, BaseHeirloom> storedHeirlooms = new HashMap<>();

    public static void addHeirloom(UUID uuid, BaseHeirloom baseHeirloom) {
        storedHeirlooms.put(uuid, baseHeirloom);
    }
    public static HashMap<UUID, BaseHeirloom> getStoredHeirlooms() {
        return new HashMap<>(storedHeirlooms);
    }

    public static BaseHeirloom findHeirloom(UUID uuid) {
        return storedHeirlooms.get(uuid);
    }
}
