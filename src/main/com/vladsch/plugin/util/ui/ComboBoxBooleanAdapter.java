package com.vladsch.plugin.util.ui;

public interface ComboBoxBooleanAdapter<E extends ComboBoxAdaptable<E>> extends ComboBoxAdapter<E> {
    E getNonDefault();

    E get(boolean value);
}
