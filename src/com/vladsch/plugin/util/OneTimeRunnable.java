package com.vladsch.plugin.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

import static javax.swing.SwingUtilities.isEventDispatchThread;

/**
 * Used to create a task that can be run at most once and the run can be cancelled before
 * it has run, in which case further attempts to run it will do nothing.
 * <p>
 * Can also specify that it should run on the AWT thread, otherwise it will run on the application thread
 * <p>
 * Useful for triggering actions after a delay that may need to be run before the delay triggers
 */
public class OneTimeRunnable extends AwtRunnable implements CancellableRunnable {
    final public static OneTimeRunnable NULL = new OneTimeRunnable(() -> {
    });

    final private AtomicBoolean myHasRun;
    final private @NotNull String myId;

    public OneTimeRunnable(@NotNull String id, ModalityState modalityState, Runnable command) {
        this(id, false, modalityState, command);
    }

    public OneTimeRunnable(@NotNull Runnable command) {
        this("", false, null, command);
    }

    public OneTimeRunnable(@NotNull String id, @NotNull Runnable command) {
        this(id, false, null, command);
    }

    public OneTimeRunnable(@NotNull String id, boolean awtThread, Runnable command) {
        this(id, awtThread, null, command);
    }

    public OneTimeRunnable(boolean awtThread, Runnable command) {
        this("",  awtThread, null, command);
    }

    public OneTimeRunnable(@NotNull String id, boolean awtThread, @Nullable ModalityState modalityState, Runnable command) {
        super(awtThread, command, modalityState);
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
     * @return false if cancelled or has run, true still can run later
     */
    @Override
    public boolean canRun() {
        return !myHasRun.get();
    }

    @Override
    public void run() {
        if (isAwtThread() && !isEventDispatchThread()) {
            //ApplicationManager.getApplication().invokeLater(this, ModalityState.any());
            ApplicationManager.getApplication().invokeLater(this);
        } else {
            if (!myHasRun.getAndSet(true)) {
                super.run();
            }
        }
    }

    @NotNull
    @Override
    public String getId() {
        return myId;
    }

    /**
     * Creates a one-shot runnable that will run after a delay, can be run early, or cancelled
     * <p>
     * the given command will only be executed once, either by the delayed trigger or by the run method.
     * if you want to execute the task early just invoke #run, it will do nothing if the task has already run.
     *
     * @param scheduler
     * @param delay     the time from now to delay execution
     * @param command   the task to execute
     *
     * @return a {@link OneTimeRunnable} which will run after the given
     *         delay or if {@link #run()} is invoked before {@link #cancel()} is invoked
     *
     * @throws NullPointerException if command is null
     */
    public static OneTimeRunnable schedule(final @NotNull CancelableJobScheduler scheduler, @NotNull String id, int delay, @NotNull Runnable command) {
        OneTimeRunnable runnable = command instanceof OneTimeRunnable ? (OneTimeRunnable) command : new OneTimeRunnable(id, command);
        scheduler.schedule(delay, runnable);
        return runnable;
    }

    public static OneTimeRunnable schedule(final @NotNull CancelableJobScheduler scheduler, int delay, @NotNull Runnable command) {
        OneTimeRunnable runnable = command instanceof OneTimeRunnable ? (OneTimeRunnable) command : new OneTimeRunnable(command);
        scheduler.schedule(delay, runnable);
        return runnable;
    }

    public static OneTimeRunnable schedule(final @NotNull CancelableJobScheduler scheduler, int delay, @NotNull CancellableRunnable command) {
        OneTimeRunnable runnable = command instanceof OneTimeRunnable ? (OneTimeRunnable) command : new OneTimeRunnable(command.getId(), command);
        CancellableRunnable cancellableJob = scheduler.schedule(delay, runnable);
        return runnable;
    }

    public static OneTimeRunnable schedule(final @NotNull CancelableJobScheduler scheduler, int delay, @NotNull OneTimeRunnable command) {
        CancellableRunnable cancellableJob = scheduler.schedule(delay, command);
        return command;
    }

    /**
     * Creates a one-shot runnable that will run after a delay, can be run early, or cancelled
     * <p>
     * the given command will only be executed once, either by the delayed trigger or by the run method. if you want to execute the task early just invoke #run, it will do nothing if the task has already run.
     *
     * @param scheduler
     * @param delay     the time from now to delay execution
     * @param command   the task to execute
     *
     * @return a {@link CancellableRunnable} which will run after the given delay on the AwtThread if {@link #run()} is invoked before {@link CancellableRunnable#cancel()}
     *
     * @throws NullPointerException if command is null
     */
    public static OneTimeRunnable schedule(final CancelableJobScheduler scheduler, @NotNull String id, int delay, @Nullable ModalityState modalityState, @NotNull Runnable command) {
        OneTimeRunnable runnable = command instanceof OneTimeRunnable ? (OneTimeRunnable) command : new OneTimeRunnable(command);
        scheduler.schedule(id, delay, runnable);
        return runnable;
    }
}
