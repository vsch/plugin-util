package com.vladsch.plugin.util.ui;

public interface SettableForm<T> {
    void reset(T settings);

    void apply(T settings);

    boolean isModified(T settings);
}
