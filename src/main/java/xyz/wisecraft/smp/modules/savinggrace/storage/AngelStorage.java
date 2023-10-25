package xyz.wisecraft.smp.modules.savinggrace.storage;

import lombok.Getter;
import xyz.wisecraft.smp.modulation.storage.storagehelpers.StorageHelperMaps;
import xyz.wisecraft.smp.modules.savinggrace.models.Angel;

import java.util.Map;
import java.util.UUID;

public class AngelStorage {

    @Getter
    private static Map<UUID, Angel> angels;

    public static void setAngels(StorageHelperMaps<UUID, Angel> angels) {
        AngelStorage.angels = angels;
    }



}
