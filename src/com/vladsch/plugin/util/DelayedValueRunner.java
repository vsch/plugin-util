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
