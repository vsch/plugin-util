package com.vladsch.plugin.util.ui;

public interface SettingsConfigurable<T> {
    void reset(T instance);
    T apply(T instance);
    boolean isModified(T instance);
}
