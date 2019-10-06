package com.vladsch.plugin.util.ui;

import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComboBox;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ComboBoxAdapterImpl<E extends ComboBoxAdaptable<E>> implements ComboBoxAdapter<E> {
    protected E myDefault;

    public ComboBoxAdapterImpl(E defaultValue) {
        this.myDefault = defaultValue;
    }

    @Override
    public void setDefaultValue(@NotNull E defaultValue) {
        myDefault = defaultValue;
    }

    @Override
    public boolean onFirst(int intValue, OnMap map) {
        OnIt on = map.on(new OnIt());

        for (Pair<ComboBoxAdaptable, Runnable> doRun : on.getList()) {
            if (doRun.getFirst().getIntValue() == intValue && isAdaptable(doRun.getFirst())) {
                doRun.getSecond().run();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onAll(int intValue, OnMap map) {
        boolean ran = false;
        OnIt on = map.on(new OnIt());

        for (Pair<ComboBoxAdaptable, Runnable> doRun : on.getList()) {
            if (doRun.getFirst().getIntValue() == intValue && isAdaptable(doRun.getFirst())) {
                doRun.getSecond().run();
                ran = true;
            }
        }
        return ran;
    }

    @Override
    public void fillComboBox(@NotNull JComboBox<String> comboBox, @NotNull ComboBoxAdaptable... exclude) {
        Set<ComboBoxAdaptable> excluded = new HashSet<>(Arrays.asList(exclude));

        //if (excluded.contains(myDefault)) {
        //    throw new IllegalStateException("Default item cannot be excluded");
        //}

        comboBox.removeAllItems();
        for (E item : myDefault.getValues()) {
            if (!excluded.contains(item)) {
                String displayName = item.getDisplayName();
                comboBox.addItem(displayName);
            }
        }
    }

    @Override
    public boolean setComboBoxSelection(@NotNull final JComboBox<String> comboBox, @NotNull final ComboBoxAdaptable selection) {
        int iMax = comboBox.getItemCount();
        int defaultIndex = 0;
        for (int i = 0; i < iMax; i++) {
            final Object item = comboBox.getItemAt(i);
            if (item.equals(selection.getDisplayName())) {
                comboBox.setSelectedIndex(i);
                return true;
            }
            if (item.equals(myDefault.getDisplayName())) {
                defaultIndex = i;
            }
        }
        comboBox.setSelectedIndex(defaultIndex);
        return false;
    }

    @Override
    public boolean isAdaptable(@NotNull ComboBoxAdaptable type) {
        return myDefault.getClass() == type.getClass();
    }

    @NotNull
    @Override
    public E findEnum(int intValue) {
        for (E item : myDefault.getValues()) {
            if (item.getIntValue() == intValue) {
                return item;
            }
        }
        return myDefault;
    }

    @NotNull
    @Override
    public E get(@NotNull final JComboBox<String> comboBox) {
        return findEnum((String) comboBox.getSelectedItem());
    }

    @NotNull
    @Override
    public E findEnum(String displayName) {
        for (E item : myDefault.getValues()) {
            if (item.getDisplayName().equals(displayName)) {
                return item;
            }
        }
        return myDefault;
    }

    @NotNull
    @Override
    public E findEnumName(String name) {
        for (E item : myDefault.getValues()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return myDefault;
    }

    @Nullable
    @Override
    public E findEnumNameOrNull(String name) {
        if (!name.isEmpty()) {
            for (E item : myDefault.getValues()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
        }
        return null;
    }

    @NotNull
    @Override
    public E valueOf(String name) {
        for (E item : myDefault.getValues()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return myDefault;
    }

    @NotNull
    @Override
    public E getDefault() {
        return myDefault;
    }

    @Override
    public boolean isBoolean() {
        return false;
    }
}
