/*
 * Copyright (c) 2016-2018 Vladimir Schneider <vladimir.schneider@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.vladsch.plugin.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.vladsch.flexmark.util.ValueRunnable;
import org.jetbrains.annotations.NotNull;

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
public class OneTimeValueRunnable<T> extends AwtValueRunnable<T> implements CancellableValueRunnable<T> {
    final private AtomicBoolean myHasRun;
    final private @NotNull String myId;

    public OneTimeValueRunnable(@NotNull ValueRunnable<T> command) {
        this("", false, command);
    }

    public OneTimeValueRunnable(@NotNull String id, @NotNull ValueRunnable<T> command) {
        this(id, false, command);
    }

    public OneTimeValueRunnable(@NotNull String id, boolean awtThread, ValueRunnable<T> command) {
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
    public void run(final T value) {
        if (isAwtThread() && !isEventDispatchThread()) {
            //ApplicationManager.getApplication().invokeLater(() -> { run(value); }, ModalityState.any());
            ApplicationManager.getApplication().invokeLater(() -> { run(value); });
        } else {
            if (!myHasRun.getAndSet(true)) {
                super.run(value);
            }
        }
    }

    @NotNull
    @Override
    public String getId() {
        return myId;
    }
}
