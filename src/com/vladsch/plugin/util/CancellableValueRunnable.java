package com.vladsch.plugin.util;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface CancellableValueRunnable<T> extends Cancellable, Consumer<T> {
    CancellableValueRunnable NULL = new CancellableValueRunnable() {
        @Override
        public boolean cancel() {
            return false;
        }

        @Override
        public boolean canRun() {
            return false;
        }

        @Override
        public void accept(final Object value) {

        }

        @Override
        @NotNull
        public String getId() {
            return "NULL";
        }
    };

    default boolean isNull() {
        return this == CancellableValueRunnable.NULL;
    }

    default boolean isNotNull() {
        return this != CancellableValueRunnable.NULL;
    }

    boolean cancel();

    boolean canRun();

    @NotNull
    String getId();
}
