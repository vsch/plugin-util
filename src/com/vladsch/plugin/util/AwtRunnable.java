/*
 * Copyright (c) 2016-2018 Vladimir Schneider <vladimir.schneider@gmail.com>
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package com.vladsch.plugin.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import org.jetbrains.annotations.NotNull;

import static javax.swing.SwingUtilities.isEventDispatchThread;

/**
 * Used to create a task that needs to potentially run on the AwtThread.
 */
public class AwtRunnable implements Runnable {
    final private Runnable myCommand;
    final private boolean myAwtThread;

    public Runnable getCommand() {
        return myCommand;
    }

    public boolean isAwtThread() {
        return myAwtThread;
    }

    public AwtRunnable(Runnable command) {
        this(false, command);
    }

    public AwtRunnable(boolean awtThread, Runnable command) {
        myCommand = command;
        myAwtThread = awtThread;
    }

    @Override
    public void run() {
        if (myAwtThread && !isEventDispatchThread()) {
            ApplicationManager.getApplication().invokeLater(this, ModalityState.any());
        } else {
            myCommand.run();
        }
    }

    /**
     * Creates a one-shot runnable that will run after a delay, can be run early, or cancelled
     * <p>
     * the given command will only be executed once, either by the delayed trigger or by the run method.
     * if you want to execute the task early just invoke #run, it will do nothing if the task has already run.
     *
     * @param command the task to execute
     * @param delay   the time from now to delay execution
     * @return a {@link CancellableRunnable} which will run after the given
     * delay on the AwtThread if {@link #run()} is invoked before {@link CancellableRunnable#cancel()}
     * @throws NullPointerException if command is null
     */
    public static CancellableRunnable schedule(@NotNull String id, int delay, @NotNull Runnable command) {
        AwtRunnable runnable = new AwtRunnable(true, command);
        return CancelableJobScheduler.getScheduler().schedule(id, delay, runnable);
    }
}
