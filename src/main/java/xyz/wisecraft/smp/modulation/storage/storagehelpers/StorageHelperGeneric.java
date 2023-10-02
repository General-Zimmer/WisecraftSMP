package xyz.wisecraft.smp.modulation.storage.storagehelpers;

import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.HashMap;
@SuppressWarnings({"unchecked", "unused"})
public class StorageHelperGeneric<T> {

    protected final String key;
    private final HashMap<String, Object> storage;

    public StorageHelperGeneric(ModuleClass module, String key) {
        this.storage = module.getCollections();
        this.key = key;
    }

    public T add(@NotNull T value) {
        return (T) storage.put(key, value);
    }

    public T get() {
        return (T) storage.get(key);
    }

    public T remove() {
        return (T) storage.remove(key);
    }

    public boolean contains(T value) {
        return storage.containsValue(value);
    }

    public void clear() {
        storage.remove(key);
    }
}
