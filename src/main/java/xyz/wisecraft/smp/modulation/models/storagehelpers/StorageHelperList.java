package xyz.wisecraft.smp.modulation.models.storagehelpers;

import xyz.wisecraft.smp.modulation.ModuleClass;
import xyz.wisecraft.smp.modulation.models.StorageHelperGeneric;

import java.util.Collection;

public class StorageHelperList<T extends Collection<E>, E> extends StorageHelperGeneric<T> {
    public StorageHelperList(ModuleClass module) {
        super(module);
    }



}
