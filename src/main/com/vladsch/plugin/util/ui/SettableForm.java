package com.vladsch.plugin.util.ui;

import org.jetbrains.annotations.NotNull;

public interface SettableForm<T> {
    void reset(@NotNull T settings);

    void apply(@NotNull T settings);

    boolean isModified(@NotNull T settings);
}
