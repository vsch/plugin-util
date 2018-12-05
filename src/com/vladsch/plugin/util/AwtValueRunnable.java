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

import static javax.swing.SwingUtilities.isEventDispatchThread;

/**
 * Used to create a task that needs to potentially run on the AwtThread.
 */
public class AwtValueRunnable<T> implements ValueRunnable<T> {
    final private ValueRunnable<T> myCommand;
    final private boolean myAwtThread;

    public ValueRunnable<T> getCommand() {
        return myCommand;
    }

    public boolean isAwtThread() {
        return myAwtThread;
    }

    public AwtValueRunnable(ValueRunnable<T> command) {
        this(false, command);
    }

    public AwtValueRunnable(boolean awtThread, ValueRunnable<T> command) {
        myCommand = command;
        myAwtThread = awtThread;
    }

    @Override
    public void run(final T value) {
        if (myAwtThread && !isEventDispatchThread()) {
            //ApplicationManager.getApplication().invokeLater(() -> { run(value); }, ModalityState.any()); } 
            ApplicationManager.getApplication().invokeLater(() -> { run(value); }); 
        } else {
            myCommand.run(value);
        }
    }
}
