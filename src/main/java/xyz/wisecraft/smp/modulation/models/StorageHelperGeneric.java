package xyz.wisecraft.smp.modulation.models;

import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
@SuppressWarnings({"unchecked", "unused"})
public class StorageHelperGeneric<T> {

    private final Set<String> keys = new HashSet<>();
    private final HashMap<String, Object> storage;

    public StorageHelperGeneric(ModuleClass module) {
        this.storage = module.getCollections();
    }

    public T add(String key, @NotNull T value) {
        if (keys.add(key))
            storage.put(key, value);

        return value;
    }

    public T get(String key) {
        if (!keys.contains(key))
            return null;
        return (T) storage.get(key);
    }

    public T remove(String key) {
        keys.remove(key);
        return (T) storage.remove(key);
    }

    public boolean contains(T value) {
        return storage.containsValue(value);
    }

}
