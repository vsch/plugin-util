package com.vladsch.plugin.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

public class DelayedValueRunner<T> {
    final private LinkedHashMap<Object, ArrayList<Consumer<T>>> myRunnables = new LinkedHashMap<>();
    final private Object myUnnamedKey = new Object();

    public DelayedValueRunner() {

    }

    public void runAll(T value) {
        for (ArrayList<Consumer<T>> runnableList : myRunnables.values()) {
            for (Consumer<T> runnable : runnableList) {
                runnable.accept(value);
            }
        }

        myRunnables.clear();
    }

    public void addRunnable(Object key, Consumer<T> runnable) {
        ArrayList<Consumer<T>> list = myRunnables.computeIfAbsent(key, o -> new ArrayList<>());
        list.add(runnable);
    }

    public void addRunnable(Consumer<T> runnable) {
        ArrayList<Consumer<T>> list = myRunnables.computeIfAbsent(myUnnamedKey, o -> new ArrayList<>());
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
