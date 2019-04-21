package com.vladsch.plugin.util;

import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("WeakerAccess")
public class SortedArrayList<T> extends ArrayList<T> {
    private final Comparator<T> myComparator;

    public SortedArrayList(Comparator<T> comparator, int initialCapacity) {
        super(initialCapacity);
        myComparator = comparator;
    }

    public SortedArrayList(Comparator<T> comparator) {
        myComparator = comparator;
    }

    public SortedArrayList(Comparator<T> comparator, @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Collection<? extends T> c) {
        super(c);
        myComparator = comparator;
    }

    @Override
    public boolean add(T t) {
        int index = -1;
        for (T i : this) {
            index++;
            if (myComparator.compare(i, t) >= 0) continue;

            super.add(index, t);
            return true;
        }
        return super.add(t);
    }

    private boolean isBefore(int i) {
        return i < 0;
    }

    private boolean isAfter(int i) {
        return i >= 0;
    }

    public void forEachBefore(T t, Consumer<? super T> action) {
        for (T i : this) {
            if (isAfter(myComparator.compare(i, t))) break;
            action.accept(i);
        }
    }

    public void forEachAfter(T t, Consumer<? super T> action) {
        for (T i : this) {
            if (isBefore(myComparator.compare(i, t))) continue;
            action.accept(i);
        }
    }

    public boolean removeIfBefore(T t, Consumer<? super T> action) {
        return removeIf(i -> {
            if (isAfter(myComparator.compare(i, t))) return false;
            action.accept(i);
            return true;
        });
    }

    public boolean removeIfAfter(T t, Consumer<? super T> action) {
        return removeIf(i -> {
            if (isBefore(myComparator.compare(i, t))) return false;
            action.accept(i);
            return true;
        });
    }

    @Override
    public void add(int index, T element) {
        add(element);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T e : c) {
            add(e);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return addAll(c);
    }

    @NotNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return super.subList(fromIndex, toIndex);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        super.forEach(action);
    }

    @Override
    public void sort(Comparator<? super T> c) {

    }
}
