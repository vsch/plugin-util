package com.vladsch.plugin.util.ui;

import org.jetbrains.annotations.NotNull;

import javax.swing.JComboBox;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ComboBoxBooleanAdapterImpl<E extends ComboBoxAdaptable<E>> extends ComboBoxAdapterImpl<E> implements ComboBoxBooleanAdapter<E> {
    protected final E myNonDefault;

    public ComboBoxBooleanAdapterImpl(E falseValue, E trueValue) {
        super(falseValue);
        this.myNonDefault = trueValue;
    }

    @Override
    public void fillComboBox(@NotNull JComboBox<String> comboBox, @NotNull ComboBoxAdaptable... exclude) {
        Set<ComboBoxAdaptable> excluded = new HashSet<>(Arrays.asList(exclude));

        comboBox.removeAllItems();
        for (E item : myDefault.getValues()) {
            if (item == myDefault || item == myNonDefault) {
                //noinspection unchecked
                comboBox.addItem(item.getDisplayName());
            }
        }
    }

    @Override
    public E getNonDefault() {
        return myNonDefault;
    }

    @Override
    public E get(final boolean value) {
        return value ? myNonDefault : myDefault;
    }

    @Override
    public boolean isBoolean() {
        return true;
    }
}
