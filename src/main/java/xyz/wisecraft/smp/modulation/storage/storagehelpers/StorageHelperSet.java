package xyz.wisecraft.smp.modulation.storage.storagehelpers;

import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unused"})
public class StorageHelperSet<E> extends StorageHelperCollection<Set<E>, E> implements Set<E> {
    public StorageHelperSet(ModuleClass module, String key) {
        super(module, key, new HashSet<>());
    }

    public StorageHelperSet(ModuleClass module, String key, Set<E> set) {
        super(module, key, set);
    }
}
