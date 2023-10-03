package xyz.wisecraft.smp.modulation.storage.storagehelpers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
@SuppressWarnings({"unused"})
public class StorageHelperMaps <T extends Map<K, V>, K, V> extends StorageHelperGeneric<T> implements Map<K, V> {
    private final Class<T> clazz;
    protected final WisecraftSMP plugin;
    public StorageHelperMaps(ModuleClass module, String key, Class<T> clazz) {
        super(module, key);
        this.clazz = clazz;
        this.plugin = module.getPlugin();
    }

    @Override
    public V computeIfAbsent(K key, @NotNull Function<? super K, ? extends V> mappingFunction) {
        return get().computeIfAbsent(key, mappingFunction);
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        get().forEach(action);
    }

    public void addToList(K key, V value) {
        get().put(key, value);
    }

    public void removeFromList(K key) {
        get().remove(key);
    }

    @Override
    public int size() {
        return get().size();
    }

    @Override
    public boolean isEmpty() {
        return get().isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return get().containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return get().containsValue(value);
    }

    @Override
    public V get(Object key) {
        return get().get(key);
    }

    @Nullable
    @Override
    public V put(K key, V value) {
        return get().put(key, value);
    }

    @Override
    public V remove(Object key) {
        return get().remove(key);
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        get().putAll(m);
    }

    @Override
    public void clear() {
        get().clear();
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        return get().keySet();
    }

    @NotNull
    @Override
    public Collection<V> values() {
        return get().values();
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return get().entrySet();
    }

    public T copyList() {
        try {
            return clazz.getConstructor(Map.class).newInstance(get());
        } catch (Exception e) {
            plugin.getLogger().log(java.util.logging.Level.SEVERE, "Could not copy list", e);
            return null;
        }
    }
}
