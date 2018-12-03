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

import com.vladsch.flexmark.util.ValueRunnable;
import org.jetbrains.annotations.NotNull;

public interface CancellableValueRunnable<T> extends Cancellable, ValueRunnable<T> {
    CancellableValueRunnable NULL = new CancellableValueRunnable() {
        @Override
        public boolean cancel() {
            return false;
        }

        @Override
        public boolean canRun() {
            return false;
        }

        @Override
        public void run(final Object value) {

        }

        @Override
        @NotNull
        public String getId() {
            return "NULL";
        }
    };

    default boolean isNull() {
        return this == CancellableValueRunnable.NULL;
    }

    default boolean isNotNull() {
        return this != CancellableValueRunnable.NULL;
    }

    boolean cancel();
    boolean canRun();

    @NotNull
    String getId();
}
