package com.vladsch.plugin.util.ui;

import org.jetbrains.annotations.NotNull;

public interface SettingsConfigurable<T> {
    void reset(@NotNull T instance);

    @SuppressWarnings("UnusedReturnValue")
    @NotNull T apply(@NotNull T instance);

    boolean isModified(@NotNull T instance);
}
