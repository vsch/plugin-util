package com.vladsch.plugin.util;

import com.vladsch.flexmark.util.ValueRunnable;
import org.jetbrains.annotations.NotNull;

public interface CancellableValueRunnable<T> extends Cancellable, ValueRunnable<T> {
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
        public void run(final Object value) {

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
