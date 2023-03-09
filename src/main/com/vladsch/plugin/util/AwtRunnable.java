package com.vladsch.plugin.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static javax.swing.SwingUtilities.isEventDispatchThread;

/**
 * Used to create a task that needs to potentially run on the AwtThread.
 */
public class AwtRunnable implements Runnable {
    final private Runnable myCommand;
    final private boolean myAwtThread;
    final private ModalityState myModalityState;

    public Runnable getCommand() {
        return myCommand;
    }

    public boolean isAwtThread() {
        return myAwtThread;
    }

    public AwtRunnable(Runnable command) {
        this(false, command, null);
    }

    public AwtRunnable(Runnable command, ModalityState modalityState) {
        this(false, command, modalityState);
    }

    public AwtRunnable(boolean awtThread, Runnable command) {
        this(awtThread, command, null);
    }

    public AwtRunnable(boolean awtThread, Runnable command, ModalityState modalityState) {
        myCommand = command;
        myAwtThread = awtThread;
        myModalityState = modalityState;
    }

    @Override
    public void run() {
        if (myAwtThread && !isEventDispatchThread()) {
            if (myModalityState != null) ApplicationManager.getApplication().invokeLater(this, myModalityState);
            else ApplicationManager.getApplication().invokeLater(this);
        } else {
            myCommand.run();
        }
    }

    public static CancellableRunnable schedule(final CancelableJobScheduler scheduler, @NotNull String id, int delay, @NotNull Runnable command) {
        return schedule(scheduler, id, delay, null, command);
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
    public static CancellableRunnable schedule(final CancelableJobScheduler scheduler, @NotNull String id, int delay, @Nullable ModalityState modalityState, @NotNull Runnable command) {
        AwtRunnable runnable = new AwtRunnable(true, command, modalityState);
        return scheduler.schedule(id, delay, runnable);
    }
}
