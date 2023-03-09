package com.vladsch.plugin.util;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface CancellableConsumerRunnable<T> extends Cancellable, Consumer<T> {
    CancellableConsumerRunnable<Object> NULL = new CancellableConsumerRunnable<Object>() {
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

    static <V> CancellableConsumerRunnable<V> nullRunnable() {
        //noinspection unchecked
        return (CancellableConsumerRunnable<V>) NULL;
    }

    default boolean isNull() {
        return this == CancellableConsumerRunnable.NULL;
    }

    default boolean isNotNull() {
        return this != CancellableConsumerRunnable.NULL;
    }


    boolean cancel();

    boolean canRun();

    @NotNull
    String getId();
}
