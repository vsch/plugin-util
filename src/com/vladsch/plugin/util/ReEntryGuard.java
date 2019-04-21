package com.vladsch.plugin.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

public class ReEntryGuard extends AtomicInteger {
    private @Nullable HashSet<Runnable> myOnExitRunnables = null;

    public ReEntryGuard(int initialValue) {
        super(initialValue);
    }

    public ReEntryGuard() {
    }

    private void decrementAndCheckExit() {
        int exited = decrementAndGet();

        if (exited == 0 && myOnExitRunnables != null && !myOnExitRunnables.isEmpty()) {
            for (Runnable runnable : myOnExitRunnables) {
                runnable.run();
            }

            myOnExitRunnables.clear();
        }
    }

    public void ifUnguarded(@NotNull Runnable runnable) {
        ifUnguarded(runnable, null);
    }

    public void ifUnguarded(boolean ifGuardedRunOnExit, @NotNull Runnable runnable) {
        ifUnguarded(runnable, ifGuardedRunOnExit ? runnable : null);
    }

    public void ifUnguarded(@NotNull Runnable runnable, @Nullable Runnable runOnGuardExit) {
        if (getAndIncrement() == 0) {
            runnable.run();
        } else if (runOnGuardExit != null) {
            synchronized (this) {
                if (myOnExitRunnables == null) myOnExitRunnables = new HashSet<>();
                myOnExitRunnables.add(runOnGuardExit);
            }
        }
        decrementAndGet();
    }

    public void guard(@NotNull Runnable runnable) {
        incrementAndGet();
        runnable.run();
        decrementAndCheckExit();
    }

    public boolean unguarded() {
        return get() == 0;
    }
}
