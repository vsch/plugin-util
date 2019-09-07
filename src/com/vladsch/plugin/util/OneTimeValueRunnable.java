package com.vladsch.plugin.util;

import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static javax.swing.SwingUtilities.isEventDispatchThread;

/**
 * Used to create a task that can be run at most once and the run can be cancelled before
 * it has run, in which case further attempts to run it will do nothing.
 * <p>
 * Can also specify that it should run on the AWT thread, otherwise it will run on the application thread
 * <p>
 * Useful for triggering actions after a delay that may need to be run before the delay triggers
 */
public class OneTimeValueRunnable<T> extends AwtValueRunnable<T> implements CancellableValueRunnable<T> {
    final private AtomicBoolean myHasRun;
    final private @NotNull String myId;

    public OneTimeValueRunnable(@NotNull Consumer<T> command) {
        this("", false, command);
    }

    public OneTimeValueRunnable(@NotNull String id, @NotNull Consumer<T> command) {
        this(id, false, command);
    }

    public OneTimeValueRunnable(@NotNull String id, boolean awtThread, Consumer<T> command) {
        super(awtThread, command);
        myHasRun = new AtomicBoolean(false);
        myId = id;
    }

    /**
     * Cancels the scheduled task run if it has not run yet
     *
     * @return true if cancelled, false if it has already run
     */
    @Override
    public boolean cancel() {
        boolean result = !myHasRun.getAndSet(true);
        if (getCommand() instanceof CancellableRunnable) ((CancellableRunnable) getCommand()).cancel();
        return result;
    }

    /**
     * Tests whether it has run or been cancelled
     *
     * @return true if cancelled, false if it has already run
     */
    @Override
    public boolean canRun() {
        return !myHasRun.get();
    }

    @Override
    public void accept(final T value) {
        if (isAwtThread() && !isEventDispatchThread()) {
            //ApplicationManager.getApplication().invokeLater(() -> { run(value); }, ModalityState.any());
            ApplicationManager.getApplication().invokeLater(() -> {
                accept(value);
            });
        } else {
            if (!myHasRun.getAndSet(true)) {
                super.accept(value);
            }
        }
    }

    @NotNull
    @Override
    public String getId() {
        return myId;
    }
}
