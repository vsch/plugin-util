package com.vladsch.plugin.util;

import com.intellij.openapi.diagnostic.Logger;

import java.util.function.Supplier;

import static com.intellij.openapi.application.ApplicationManager.getApplication;

public class TimeIt {
    public static void logTime(Logger LOG, String message, Runnable runnable) {
        if (LOG.isDebugEnabled()) {
            long start = System.nanoTime();
            runnable.run();
            long end = System.nanoTime();
            String fullMessage = message + String.format(" in %3.3fms", (end - start) / 10000000.0);
            LOG.debug(fullMessage);
            if (getApplication() == null || getApplication().isUnitTestMode()) System.out.println(fullMessage);
        } else {
            runnable.run();
        }
    }

    public static <T> T logTimedValue(Logger LOG, String message, Supplier<T> runnable) {
        T result;

        if (LOG.isDebugEnabled()) {
            long start = System.nanoTime();
            result = runnable.get();
            long end = System.nanoTime();
            String fullMessage = message + String.format(" in %3.3fms", (end - start) / 10000000.0);
            LOG.debug(fullMessage);
            if (getApplication() == null || getApplication().isUnitTestMode()) System.out.println(fullMessage);
        } else {
            result = runnable.get();
        }
        return result;
    }
}
