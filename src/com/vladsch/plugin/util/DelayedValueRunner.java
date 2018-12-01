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

import com.vladsch.flexmark.util.ValueRunnable;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DelayedValueRunner<T> {
    final private LinkedHashMap<Object, ArrayList<ValueRunnable<T>>> myRunnables = new LinkedHashMap<>();
    final private Object myUnnamedKey = new Object();

    public DelayedValueRunner() {

    }

    public void runAll(T value) {
        for (ArrayList<ValueRunnable<T>> runnableList : myRunnables.values()) {
            for (ValueRunnable<T> runnable : runnableList) {
                runnable.run(value);
            }
        }

        myRunnables.clear();
    }

    public void addRunnable(Object key, ValueRunnable<T> runnable) {
        ArrayList<ValueRunnable<T>> list = myRunnables.computeIfAbsent(key, o -> new ArrayList<>());
        list.add(runnable);
    }

    public void addRunnable(ValueRunnable<T> runnable) {
        ArrayList<ValueRunnable<T>> list = myRunnables.computeIfAbsent(myUnnamedKey, o -> new ArrayList<>());
        list.add(runnable);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DelayedValueRunner)) return false;

        DelayedValueRunner runner = (DelayedValueRunner) o;

        return myRunnables.equals(runner.myRunnables);
    }

    @Override
    public int hashCode() {
        return myRunnables.hashCode();
    }
}
