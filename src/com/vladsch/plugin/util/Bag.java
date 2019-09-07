package com.vladsch.plugin.util;

import java.util.Collection;

public interface Bag<T> {
    int add(T element);

    void addAll(Collection<T> elements);

    boolean remove(T element);

    void removeAll(Collection<T> elements);

    void clear();

    int count(T... elements);

    int get(T element);

    int size();
}
