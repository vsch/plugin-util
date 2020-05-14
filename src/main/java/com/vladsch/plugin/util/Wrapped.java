package com.vladsch.plugin.util;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Wrapped<T> {
    private final T myValue;

    private Wrapped(final T value) {
        myValue = value;
    }

    public T unwrap() {
        return myValue;
    }

    private static HashMap<Class, Wrapped> ourWrappedNulls = new HashMap<>();

    public static <T> Wrapped<T> nullOf(@NotNull Class<T> clazz) {
        //noinspection unchecked
        return ourWrappedNulls.computeIfAbsent(clazz, (it) -> new Wrapped<T>(null));
    }

    public static <T> Wrapped<T> of(@NotNull T value) {
        return new Wrapped<>(value);
    }
}
