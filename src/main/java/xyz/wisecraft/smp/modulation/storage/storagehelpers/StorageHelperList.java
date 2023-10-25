package xyz.wisecraft.smp.modulation.storage.storagehelpers;

import org.jetbrains.annotations.NotNull;
import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.*;
import java.util.function.UnaryOperator;

@SuppressWarnings({"unused"})
public class StorageHelperList<E> extends StorageHelperCollection<List<E>, E> implements List<E> {


    public StorageHelperList(ModuleClass module, String key) {
        super(module, key, new ArrayList<>());
    }

    public StorageHelperList(ModuleClass module, String key, List<E> list) {
        super(module, key, list);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        return super.get().addAll(index, c);
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        get().replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super E> c) {
        super.get().sort(c);
    }

    @Override
    public E get(int index) {
        return get().get(index);
    }

    @Override
    public E set(int index, E element) {
        return get().set(index, element);
    }

    @Override
    public void add(int index, E element) {
        get().add(index, element);
    }

    @Override
    public E remove(int index) {
        return get().remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return get().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return get().lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator() {
        return get().listIterator();
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator(int index) {
        return get().listIterator(index);
    }

    @NotNull
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return get().subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<E> spliterator() {
        return get().spliterator();
    }
}
