package com.vladsch.plugin.util;

public interface MappingBag<E, T> extends Bag<E> {
    T map(E element);

    int countMapped(T[] mapped);
}
