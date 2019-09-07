package com.vladsch.plugin.util.ui;

import org.jetbrains.annotations.NotNull;

public interface Settable<T> {
    void reset();

    void apply();

    boolean isModified();

    Object getComponent();

    class Configurable<T> implements Settable {
        private final SettingsConfigurable<T> myComponents;
        private final @NotNull T myInstance;

        public Configurable(SettingsConfigurable<T> components, @NotNull T instance) {
            myComponents = components;
            myInstance = instance;
        }

        @Override
        public void reset() {
            myComponents.reset(myInstance);
        }

        @Override
        public void apply() {
            myComponents.apply(myInstance);
        }

        @Override
        public boolean isModified() {
            return myComponents.isModified(myInstance);
        }

        @Override
        public Object getComponent() {
            return myInstance;
        }
    }
}
