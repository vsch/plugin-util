package com.vladsch.plugin.util.ui;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.TextFieldWithHistory;
import com.intellij.ui.TextFieldWithHistoryWithBrowseButton;
import com.vladsch.flexmark.util.sequence.BasedSequence;
import com.vladsch.flexmark.util.sequence.BasedSequenceImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.text.JTextComponent;
import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

@SuppressWarnings("WeakerAccess")
public abstract class SettingsComponents<T> implements SettingsConfigurable<T>, Disposable {
    protected @Nullable Settable<T>[] myComponents;
    protected @Nullable T myCachedSettings;

    public SettingsComponents() {
        myComponents = null;
    }

    public SettingsComponents(@Nullable T settings) {
        myCachedSettings = settings;
        myComponents = null;
    }

    @Override
    public void dispose() {
        myComponents = null;
    }

    final public Settable<T>[] getComponents(@NotNull T i) {
        if (myComponents == null && (myCachedSettings == null || myCachedSettings == i)) {
            myComponents = createComponents(i);
            myCachedSettings = i;
        } else if (i != myCachedSettings) {
            // another settings instance, not the right use case
            return createComponents(i);
        }
        return myComponents;
    }

    protected abstract Settable<T>[] createComponents(@NotNull T i);

    final public Set<Settable<T>> getComponentSet(@NotNull T i) {
        return new LinkedHashSet<>(Arrays.asList(getComponents(i)));
    }

    public void forAllComponents(@NotNull T i, @NotNull Consumer<Settable<T>> runnable) {
        for (Settable<T> settable : getComponents(i)) {
            runnable.accept(settable);
        }
    }

    public void forAllTargets(@NotNull T i, @NotNull Object[] targets, @NotNull Consumer<Settable<T>> runnable) {
        Set<Object> targetSet = new LinkedHashSet<>();
        Collections.addAll(targetSet, targets);

        forAllComponents(i, settable -> {
            if (targetSet.contains(settable.getComponent())) runnable.accept(settable);
        });
    }

    @Override
    public void reset(@NotNull T i) {
        forAllComponents(i, Settable::reset);
    }

    @NotNull
    @Override
    public T apply(@NotNull T i) {
        forAllComponents(i, Settable::apply);
        return i;
    }

    @Override
    public boolean isModified(@NotNull T i) {
        for (Settable<T> settable : getComponents(i)) {
            if (settable.isModified()) return true;
        }
        return false;
    }

    public void apply(@NotNull T i, Object... targets) {
        forAllTargets(i, targets, Settable::apply);
    }

    public void reset(@NotNull T i, Object... targets) {
        forAllTargets(i, targets, Settable::reset);
    }

    // @formatter:off
    @NotNull public CheckBoxSetter component(@NotNull JCheckBox component, @NotNull Getter<Boolean> getter, @NotNull Setter<Boolean> setter) { return new CheckBoxSetter(component, getter, setter); }
    @NotNull public RadioButtonSetter component(@NotNull JRadioButton component, @NotNull Getter<Boolean> getter, @NotNull Setter<Boolean> setter) { return new RadioButtonSetter(component, getter, setter); }
    @NotNull public <V> SpinnerSetter<V> component(@NotNull JSpinner component, @NotNull Getter<V> getter, @NotNull Setter<V> setter) { return new SpinnerSetter<V>(component, getter, setter); }
    @NotNull public TextBoxSetter component(JTextComponent component, Getter<String> getter, @NotNull Setter<String> setter) { return new TextBoxSetter(component, getter, setter); }
    @NotNull public ColorCheckBoxSetter component(@NotNull CheckBoxWithColorChooser component, @NotNull Getter<Color> getter, @NotNull Setter<Color> setter) { return new ColorCheckBoxSetter(component, getter, setter); }
    @NotNull public ColorCheckBoxEnabledSetter componentEnabled(@NotNull CheckBoxWithColorChooser component, @NotNull Getter<Boolean> getter, @NotNull Setter<Boolean> setter) { return new ColorCheckBoxEnabledSetter(component, getter, setter); }
    @NotNull public TextFieldWithHistorySetter component(@NotNull TextFieldWithHistory component, @NotNull Getter<String> getter, @NotNull Setter<String> setter) { return new TextFieldWithHistorySetter(component, getter, setter); }
    @NotNull public TextFieldWithBrowseButtonSetter component(@NotNull TextFieldWithBrowseButton component, @NotNull Getter<String> getter, @NotNull Setter<String> setter) { return new TextFieldWithBrowseButtonSetter(component, getter, setter); }
    @NotNull public TextFieldWithHistoryWithBrowseButtonSetter component(@NotNull TextFieldWithHistoryWithBrowseButton component, @NotNull Getter<String> getter, @NotNull Setter<String> setter) { return new TextFieldWithHistoryWithBrowseButtonSetter(component, getter, setter); }
    @NotNull public <V extends EditorTextField> EditorTextFieldSetter<V, String> component(@NotNull V component, @NotNull JComponentGetter<V, String> componentGetter, @NotNull JComponentSetter<V, String> componentSetter, @NotNull Getter<String> getter, @NotNull Setter<String> setter) { return new EditorTextFieldSetter<>(component, componentGetter, componentSetter, getter, setter,
            (o1, o2) -> {
                BasedSequence b1 = BasedSequenceImpl.of(o1);
                BasedSequence b2 = BasedSequenceImpl.of(o2);
                return b1.trimTailBlankLines().compareTo(b2.trimTailBlankLines());
            }); }
    @NotNull public <V extends EditorTextField> EditorTextFieldSetter<V, String> component(@NotNull V component, @NotNull JComponentGetter<V, String> componentGetter, @NotNull JComponentSetter<V, String> componentSetter, @NotNull Getter<String> getter, @NotNull Setter<String> setter, @NotNull Comparator<String> comparator) { return new EditorTextFieldSetter<>(component, componentGetter, componentSetter, getter, setter, comparator); }
    // @formatter:on

    @NotNull
    public ComboBoxBooleanSetter component(@NotNull ComboBoxBooleanAdapter<?> adapter, @NotNull JComboBox<String> component, @NotNull Getter<Boolean> getter, @NotNull Setter<Boolean> setter) { return new ComboBoxBooleanSetter(component, adapter, getter, setter); }

    @NotNull
    public ComboBoxSetter component(@NotNull ComboBoxAdapter<?> adapter, @NotNull JComboBox<String> component, @NotNull Getter<Integer> getter, @NotNull Setter<Integer> setter) { return new ComboBoxSetter(component, adapter, getter, setter); }

    @NotNull
    public ComboBoxStringSetter componentString(@NotNull ComboBoxAdapter<?> adapter, @NotNull JComboBox<String> component, @NotNull Getter<String> getter, @NotNull Setter<String> setter) { return new ComboBoxStringSetter(component, adapter, getter, setter); }

    @NotNull
    public JComponentSettableForm<T> component(@NotNull SettableForm<T> component, @NotNull T settings) { return new JComponentSettableForm<>(component, settings);}

    public static class CheckBoxSetter extends JComponentSettable<JCheckBox, Boolean> {
        public CheckBoxSetter(@NotNull JCheckBox component, @NotNull Getter<Boolean> getter, @NotNull Setter<Boolean> setter) {
            super(component, component::isSelected, component::setSelected, getter, setter);
        }
    }

    public static class ColorCheckBoxEnabledSetter extends JComponentSettable<CheckBoxWithColorChooser, Boolean> {
        public ColorCheckBoxEnabledSetter(@NotNull CheckBoxWithColorChooser component, @NotNull Getter<Boolean> getter, @NotNull Setter<Boolean> setter) {
            super(component, component::isSelected, component::setSelected, getter, setter);
        }
    }

    public static class RadioButtonSetter extends JComponentSettable<JRadioButton, Boolean> {
        public RadioButtonSetter(@NotNull JRadioButton component, @NotNull Getter<Boolean> getter, @NotNull Setter<Boolean> setter) {
            super(component, component::isSelected, component::setSelected, getter, setter);
        }
    }

    public static class ComboBoxBooleanSetter extends JComponentSettable<JComboBox<String>, Boolean> {
        public ComboBoxBooleanSetter(@NotNull JComboBox<String> component, @NotNull ComboBoxBooleanAdapter<?> adapter, @NotNull Getter<Boolean> getter, @NotNull Setter<Boolean> setter) {
            super(component, () -> adapter.findEnum((String) component.getSelectedItem()) == adapter.getNonDefault(),
                    (value) -> component.setSelectedItem(value ? adapter.getNonDefault().getDisplayName() : adapter.getDefault().getDisplayName()),
                    getter, setter);
        }
    }

    public static class ComboBoxSetter extends JComponentSettable<JComboBox<String>, Integer> {
        public ComboBoxSetter(@NotNull JComboBox<String> component, @NotNull ComboBoxAdapter<?> adapter, @NotNull Getter<Integer> getter, @NotNull Setter<Integer> setter) {
            super(component, () -> adapter.findEnum((String) component.getSelectedItem()).getIntValue(),
                    (value) -> component.setSelectedItem(adapter.findEnum(value).getDisplayName()),
                    getter, setter);
        }
    }

    public static class ComboBoxStringSetter extends JComponentSettable<JComboBox<String>, String> {
        public ComboBoxStringSetter(@NotNull JComboBox<String> component, @NotNull ComboBoxAdapter<?> adapter, @NotNull Getter<String> getter, @NotNull Setter<String> setter) {
            super(component, () -> (String) component.getSelectedItem(),
                    component::setSelectedItem,
                    getter, setter);
        }
    }

    public static class SpinnerSetter<V> extends JComponentSettable<JSpinner, V> {
        public SpinnerSetter(@NotNull JSpinner component, @NotNull Getter<V> getter, @NotNull Setter<V> setter) {
            //noinspection unchecked
            super(component, () -> (V) component.getValue(), component::setValue, getter, setter);
        }
    }

    public static class TextBoxSetter extends JComponentSettable<JTextComponent, String> {
        public TextBoxSetter(@NotNull JTextComponent component, @NotNull Getter<String> getter, @NotNull Setter<String> setter) {
            super(component, component::getText, component::setText, getter, setter);
        }
    }

    public static class ColorCheckBoxSetter extends JComponentSettable<CheckBoxWithColorChooser, Color> {
        public ColorCheckBoxSetter(@NotNull CheckBoxWithColorChooser component, @NotNull Getter<Color> getter, @NotNull Setter<Color> setter) {
            super(component, component::getColor, component::setColor, getter, setter);
        }
    }

    public static class TextFieldWithHistorySetter extends JComponentSettable<TextFieldWithHistory, String> {
        public TextFieldWithHistorySetter(@NotNull TextFieldWithHistory component, @NotNull Getter<String> getter, @NotNull Setter<String> setter) {
            super(component, component::getText, component::setTextAndAddToHistory, getter, setter);
        }
    }

    public static class TextFieldWithBrowseButtonSetter extends JComponentSettable<TextFieldWithBrowseButton, String> {
        public TextFieldWithBrowseButtonSetter(@NotNull TextFieldWithBrowseButton component, @NotNull Getter<String> getter, @NotNull Setter<String> setter) {
            super(component, component::getText, component::setText, getter, setter);
        }
    }

    public static class TextFieldWithHistoryWithBrowseButtonSetter extends JComponentSettable<TextFieldWithHistoryWithBrowseButton, String> {
        public TextFieldWithHistoryWithBrowseButtonSetter(@NotNull TextFieldWithHistoryWithBrowseButton component, @NotNull Getter<String> getter, @NotNull Setter<String> setter) {
            super(component, component::getText, component::setTextAndAddToHistory, getter, setter);
        }
    }

    public static class EditorTextFieldSetter<V extends EditorTextField, T extends Comparable<T>> implements Settable<V> {
        private final @NotNull V myInstance;
        private final @NotNull JComponentGetter<V, T> myComponentGetter;
        private final @NotNull JComponentSetter<V, T> myComponentSetter;
        private final @NotNull Getter<T> myGetter;
        private final @NotNull Setter<T> mySetter;
        private final @NotNull Comparator<T> myComparator;

        public EditorTextFieldSetter(@NotNull V component, @NotNull JComponentGetter<V, T> componentGetter, @NotNull JComponentSetter<V, T> componentSetter, @NotNull Getter<T> getter, @NotNull Setter<T> setter) {
            this(component, componentGetter, componentSetter, getter, setter, new Comparator<T>() {
                @Override
                public int compare(final T o1, final T o2) {
                    return o1.compareTo(o2);
                }
            });
        }

        public EditorTextFieldSetter(@NotNull V component, @NotNull JComponentGetter<V, T> componentGetter, @NotNull JComponentSetter<V, T> componentSetter, @NotNull Getter<T> getter, @NotNull Setter<T> setter, @NotNull Comparator<T> comparator) {
            myInstance = component;
            myComponentGetter = componentGetter;
            myComponentSetter = componentSetter;
            myGetter = getter;
            mySetter = setter;
            myComparator = comparator;
        }

        @Override
        public void reset() {
            if (!myGetter.get().equals(myComponentGetter.get(myInstance))) {
                myComponentSetter.set(myInstance, myGetter.get());
            }
        }

        @Override
        public void apply() {
            if (myInstance.isVisible()) {
                if (!myGetter.get().equals(myComponentGetter.get(myInstance))) {
                    mySetter.set(myComponentGetter.get(myInstance));
                }
            }
        }

        @Override
        public boolean isModified() {
            if (myInstance.isVisible()) {
                return myComparator.compare(myGetter.get(), myComponentGetter.get(myInstance)) != 0;
            }
            return false;
        }

        @Override
        public V getComponent() {
            return myInstance;
        }
    }

    public interface JComponentGetter<V, T> {
        T get(V component);
    }

    public interface JComponentSetter<V, T> {
        void set(V component, T value);
    }

    public static class JComponentSettable<C extends JComponent, V> implements Settable<C> {
        private final @NotNull C myInstance;
        private final @NotNull Getter<V> myComponentGetter;
        private final @NotNull Setter<V> myComponentSetter;
        private final @NotNull Getter<V> myGetter;
        private final @NotNull Setter<V> mySetter;

        public JComponentSettable(@NotNull final C instance, @NotNull Getter<V> componentGetter, @NotNull Setter<V> componentSetter, @NotNull Getter<V> getter, @NotNull Setter<V> setter) {
            myInstance = instance;
            myComponentGetter = componentGetter;
            myComponentSetter = componentSetter;
            myGetter = getter;
            mySetter = setter;
        }

        @NotNull
        @Override
        public C getComponent() {
            return myInstance;
        }

        @Override
        public void reset() {
            if (!myGetter.get().equals(myComponentGetter.get())) {
                myComponentSetter.set(myGetter.get());
            }
        }

        @Override
        public void apply() {
            if (myInstance.isVisible()) {
                if (!myGetter.get().equals(myComponentGetter.get())) {
                    mySetter.set(myComponentGetter.get());
                }
            }
        }

        @Override
        public boolean isModified() {
            if (myInstance.isVisible()) {
                return !Objects.equals(myGetter.get(), myComponentGetter.get());
            }
            return false;
        }
    }

    public static class JComponentSettableForm<S> implements Settable<SettableForm<S>> {
        private final @NotNull SettableForm<S> myInstance;
        private final @NotNull Runnable myApplier;
        private final @NotNull Runnable myResetter;
        private final @NotNull Getter<Boolean> myIsModified;

        public JComponentSettableForm(@NotNull final SettableForm<S> instance, S settings) {
            myInstance = instance;
            myApplier = () -> instance.apply(settings);
            myResetter = () -> instance.reset(settings);
            myIsModified = () -> instance.isModified(settings);
        }

        @Override
        public void reset() {
            myResetter.run();
        }

        @Override
        public void apply() {
            myApplier.run();
        }

        @Override
        public boolean isModified() {
            return myIsModified.get();
        }

        @Override
        public SettableForm<S> getComponent() {
            return myInstance;
        }
    }
}
