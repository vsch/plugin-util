package com.vladsch.plugin.util.ui;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ListCellRendererWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JList;
import java.util.function.Function;

public interface ComboBoxAdaptable<E extends ComboBoxAdaptable<E>> {
    ComboBoxAdaptable[] EMPTY = new ComboBoxAdaptable[0];

    @NotNull
    String getDisplayName();

    @NotNull
    String name();

    int getIntValue();

    @NotNull
    ComboBoxAdapter<E> getAdapter();

    @NotNull
    E[] getValues();

    // these have default implementations
    default boolean isDefault() {
        return this == getAdapter().getDefault();
    }

    default boolean setComboBoxSelection(@NotNull JComboBox<String> comboBox) {
        return getAdapter().setComboBoxSelection(comboBox, this);
    }

    class IconCellRenderer<E extends ComboBoxAdaptable<E>> extends ListCellRendererWrapper<String> {
        final @NotNull ComboBoxAdapter<E> myAdapter;
        final @NotNull Function<E, Icon> myIconMapper;

        public IconCellRenderer(@NotNull final ComboBoxAdapter<E> myAdapter, @NotNull final Function<E, Icon> iconMapper) {
            this.myAdapter = myAdapter;
            this.myIconMapper = iconMapper;
        }

        @Override
        public void customize(final JList list, final String value, final int index, final boolean selected, final boolean hasFocus) {
            E type = myAdapter.findEnum((String) value);
            this.setText(type.getDisplayName());
            this.setIcon(myIconMapper.apply(type));
        }
    }

    class Static<T extends ComboBoxAdaptable<T>> implements ComboBoxAdapter<T> {
        protected final @NotNull ComboBoxAdapter<T> ADAPTER;

        public Static(@NotNull ComboBoxAdapter<T> ADAPTER) {
            this.ADAPTER = ADAPTER;
        }

        @NotNull
        public T get(@NotNull JComboBox<String> comboBox) {
            return ADAPTER.findEnum((String) comboBox.getSelectedItem());
        }

        @NotNull
        @Override
        public T valueOf(String name) {return ADAPTER.valueOf(name);}

        @NotNull
        @Override
        public T getDefault() {return ADAPTER.getDefault();}

        @Override
        public void setDefaultValue(@NotNull final T defaultValue) {
            ADAPTER.setDefaultValue(defaultValue);
        }

        public T get(int value) {
            return ADAPTER.findEnum(value);
        }

        public int getInt(@NotNull JComboBox<String> comboBox) {
            return ADAPTER.findEnum((String) comboBox.getSelectedItem()).getIntValue();
        }

        public void set(@NotNull JComboBox<String> comboBox, int intValue) {
            comboBox.setSelectedItem(ADAPTER.findEnum(intValue).getDisplayName());
        }

        @NotNull
        public JComboBox<String> createComboBox(@NotNull ComboBoxAdaptable... exclude) {
            JComboBox<String> comboBox = new ComboBox<>();
            ADAPTER.fillComboBox(comboBox, exclude);
            return comboBox;
        }

        public void addComboBoxIcons(@NotNull JComboBox<String> comboBox, @NotNull Function<T, Icon> iconMapper) {
            comboBox.setRenderer(new IconCellRenderer<>(ADAPTER, iconMapper));
        }

        @Override
        public boolean isAdaptable(@NotNull ComboBoxAdaptable type) { return ADAPTER.isAdaptable(type); }

        @Override
        public void fillComboBox(@NotNull JComboBox<String> comboBox, @NotNull ComboBoxAdaptable[] exclude) { ADAPTER.fillComboBox(comboBox, exclude); }

        @Override
        public boolean setComboBoxSelection(@NotNull JComboBox<String> comboBox, @NotNull final ComboBoxAdaptable selection) {
            return ADAPTER.setComboBoxSelection(comboBox, selection);
        }

        @NotNull
        @Override
        public T findEnum(int intValue) { return ADAPTER.findEnum(intValue); }

        @NotNull
        @Override
        public T findEnum(String displayName) { return ADAPTER.findEnum(displayName); }

        @NotNull
        @Override
        public T findEnumName(String name) { return ADAPTER.findEnumName(name); }

        @Nullable
        @Override
        public T findEnumNameOrNull(String name) { return ADAPTER.findEnumNameOrNull(name); }

        @Override
        public boolean isBoolean() { return ADAPTER.isBoolean(); }

        @Override
        public boolean onFirst(int intValue, OnMap map) { return ADAPTER.onFirst(intValue, map); }

        @Override
        public boolean onAll(int intValue, OnMap map) { return ADAPTER.onAll(intValue, map); }
    }

    class StaticBoolean<T extends ComboBoxAdaptable<T>> extends Static<T> implements ComboBoxBooleanAdapter<T> {
        public StaticBoolean(@NotNull ComboBoxBooleanAdapter<T> ADAPTER) {
            super(ADAPTER);
        }

        @Override
        public boolean setComboBoxSelection(@NotNull final JComboBox<String> comboBox, @NotNull final ComboBoxAdaptable selection) {
            return ADAPTER.setComboBoxSelection(comboBox, selection);
        }

        @Override
        public T getNonDefault() {return ((ComboBoxBooleanAdapter<T>) ADAPTER).getNonDefault();}

        @Override
        public T get(final boolean value) {
            return value ? ((ComboBoxBooleanAdapter<T>) ADAPTER).getNonDefault() : ((ComboBoxBooleanAdapter<T>) ADAPTER).getDefault();
        }
    }
}
