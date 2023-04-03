package com.vladsch.plugin.util.ui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComboBox;

public interface ComboBoxAdapter<E extends ComboBoxAdaptable<E>> {
    boolean isAdaptable(@NotNull ComboBoxAdaptable<?> type);

    void setDefaultValue(@NotNull E defaultValue);

    boolean onFirst(int intValue, OnMap map);

    boolean onAll(int intValue, OnMap map);

    void fillComboBox(@NotNull JComboBox<String> comboBox, @NotNull ComboBoxAdaptable<?>... exclude);

    @SuppressWarnings("UnusedReturnValue")
    boolean setComboBoxSelection(@NotNull JComboBox<String> comboBox, final @Nullable ComboBoxAdaptable<?> selection);

    @NotNull
    E findEnum(int intValue);

    @NotNull
    E findEnum(String displayName);

    @NotNull
    E findEnumName(String name);

    @Nullable
    E findEnumNameOrNull(String name);

    @NotNull
    E get(@NotNull JComboBox<String> comboBox);

    @NotNull
    E valueOf(String name);

    @NotNull
    E getDefault();

    boolean isBoolean();
}
