package xyz.wisecraft.smp.modulation.storage.storagehelpers;

import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

@SuppressWarnings({"unused"})
public class StorageHelperCollection<T extends Collection<E>, E> extends StorageHelperGeneric<T> implements Collection<E> {

    protected final WisecraftSMP plugin;
    public StorageHelperCollection(ModuleClass module, String key) {
        super(module, key);
        this.plugin = module.getPlugin();
    }

    public StorageHelperCollection(ModuleClass module, String key, T list) {
        this(module, key);
        set(list);
    }


    @Override
    public void forEach(java.util.function.Consumer<? super E> action) {
        get().forEach(action);
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
    public boolean contains(Object o) {
        return get().contains(o);
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return get().iterator();
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return get().toArray();
    }

    @NotNull
    @Override
    public <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        return get().toArray(a);
    }

    @Override
    public boolean add(E e) {
        return get().add(e);
    }

    @Override
    public boolean remove(Object o) {
        return get().remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return get().containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        return get().addAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return get().removeAll(c);
    }

    @Override
    public boolean removeIf(java.util.function.Predicate<? super E> filter) {
        return get().removeIf(filter);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return false;
    }

    public void addToList(E value) {
        get().add(value);
    }

    public void removeFromList(E value) {
        get().remove(value);
    }


    /**
     * Clears the list
     */
    @Override
    public void clear() {
        get().clear();
    }

    /**
     * Clears the list instead of removes it from the storage @see {@link StorageHelperGeneric#remove()}
     * @return null
     */
    @Override
    public T remove() {
        get().clear();
        return null;
    }

    public ArrayList<E> copyToArrayList() {
        return new ArrayList<>(get());
    }

    public HashSet<E> copyToHashSet() {
        return new HashSet<>(get());
    }
}
