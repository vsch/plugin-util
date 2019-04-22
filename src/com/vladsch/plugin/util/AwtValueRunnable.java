package com.vladsch.plugin.util;

import com.intellij.openapi.application.ApplicationManager;

import java.util.function.Consumer;

import static javax.swing.SwingUtilities.isEventDispatchThread;

/**
 * Used to create a task that needs to potentially run on the AwtThread.
 */
public class AwtValueRunnable<T> implements Consumer<T> {
    final private Consumer<T> myCommand;
    final private boolean myAwtThread;

    public Consumer<T> getCommand() {
        return myCommand;
    }

    public boolean isAwtThread() {
        return myAwtThread;
    }

    public AwtValueRunnable(Consumer<T> command) {
        this(false, command);
    }

    public AwtValueRunnable(boolean awtThread, Consumer<T> command) {
        myCommand = command;
        myAwtThread = awtThread;
    }

    @Override
    public void accept(final T value) {
        if (myAwtThread && !isEventDispatchThread()) {
            //ApplicationManager.getApplication().invokeLater(() -> { run(value); }, ModalityState.any()); }
            ApplicationManager.getApplication().invokeLater(() -> { accept(value); });
        } else {
            myCommand.accept(value);
        }
    }
}
