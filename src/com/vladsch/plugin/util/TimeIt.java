package com.vladsch.plugin.util;

import com.intellij.openapi.diagnostic.Logger;
import java.util.function.Supplier;

public class TimeIt {
    public static void logTime(Logger logger, String message, Runnable runnable) {
        if (logger.isDebugEnabled()) {
            long start = System.nanoTime();
            runnable.run();
            long end = System.nanoTime();
            String fullMessage = message + String.format(" in %3.3fms", (end - start) / 10000000.0);
            logger.debug(fullMessage);
            System.out.println(fullMessage);
        } else {
            runnable.run();
        }
    }

    public static <T> T logTimedValue(Logger logger, String message, Supplier<T> runnable) {
        T result;

        if (logger.isDebugEnabled()) {
            long start = System.nanoTime();
            result = runnable.get();
            long end = System.nanoTime();
            String fullMessage = message + String.format(" in %3.3fms", (end - start) / 10000000.0);
            logger.debug(fullMessage);
            System.out.println(fullMessage);
        } else {
            result = runnable.get();
        }
        return result;
    }
}
