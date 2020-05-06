package com.vladsch.plugin.util;

import org.jetbrains.annotations.NotNull;

public interface CancellableRunnable extends Cancellable, Runnable {
    CancellableRunnable NULL = new CancellableRunnable() {
        @Override
        public boolean cancel() {
            return false;
        }

        @Override
        public boolean canRun() {
            return false;
        }

        @Override
        public void run() {

        }

        @Override
        @NotNull
        public String getId() {
            return "NULL";
        }
    };

    default boolean isNull() {
        return this == CancellableRunnable.NULL;
    }

    default boolean isNotNull() {
        return this != CancellableRunnable.NULL;
    }

    @NotNull
    String getId();
}
