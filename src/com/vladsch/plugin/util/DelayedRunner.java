package com.vladsch.plugin.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DelayedRunner {
    final private LinkedHashMap<Object, ArrayList<Runnable>> myRunnables = new LinkedHashMap<>();
    final private Object myUnnamedKey = new Object();

    public DelayedRunner() {

    }

    public void clear() {
        myRunnables.clear();
    }

    public void runAll() {
        final Object[] keys = myRunnables.keySet().toArray();
        for (Object key : keys) {
            runAllFor(key);
        }
    }

    public boolean containsRunnableFor(Object key) {
        return myRunnables.containsKey(key);
    }

    public void runAllFor() {
        runAllFor(myUnnamedKey);
    }

    public void runAllFor(Object key) {
        ArrayList<Runnable> runnableList = myRunnables.remove(key);
        if (runnableList != null) {
            for (Runnable runnable : runnableList) {
                runnable.run();
            }
        }
    }

    public void addRunnable(Object key, Runnable runnable) {
        ArrayList<Runnable> list = myRunnables.computeIfAbsent(key, o -> new ArrayList<>());
        list.add(runnable);
    }

    public void addRunnable(Runnable runnable) {
        ArrayList<Runnable> list = myRunnables.computeIfAbsent(myUnnamedKey, o -> new ArrayList<>());
        list.add(runnable);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DelayedRunner)) return false;

        DelayedRunner runner = (DelayedRunner) o;

        return myRunnables.equals(runner.myRunnables);
    }

    @Override
    public int hashCode() {
        return myRunnables.hashCode();
    }
}
