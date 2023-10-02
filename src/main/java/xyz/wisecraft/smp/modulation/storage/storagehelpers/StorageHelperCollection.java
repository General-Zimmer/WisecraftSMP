package xyz.wisecraft.smp.modulation.storage.storagehelpers;

import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.Collection;

public class StorageHelperCollection<T extends Collection<E>, E> extends StorageHelperGeneric<T> {
    Class<T> clazz;
    WisecraftSMP plugin;
    public StorageHelperCollection(ModuleClass module, String listKey, Class<T> clazz) {
        super(module, listKey);
        this.clazz = clazz;
        this.plugin = module.getPlugin();
    }

    public void addAll(T values) {
        get().addAll(values);
    }

    public void removeAll(T values) {
        get().removeAll(values);
    }

    public void addToList(E value) {
        get().add(value);
    }

    public void removeFromList(E value) {
        get().remove(value);
    }

    public void clearList() {
        get().clear();
    }

    public T copyList() {
        try {
            return clazz.getConstructor(Collection.class).newInstance(get());
        } catch (Exception e) {
            plugin.getLogger().log(java.util.logging.Level.SEVERE, "Could not copy list", e);
            return null;
        }
    }



}
