package com.vladsch.plugin.util;

public interface Cancellable {
    boolean cancel();

    boolean canRun();
}
