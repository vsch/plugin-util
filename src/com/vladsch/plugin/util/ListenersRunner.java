package com.vladsch.plugin.util;

import com.vladsch.flexmark.util.ValueRunnable;

import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;

public class ListenersRunner<L> {
    final private LinkedHashSet<WeakReference<L>> myListeners = new LinkedHashSet<>();

    public ListenersRunner() {

    }

    public void fire(ValueRunnable<L> runnable) {
        myListeners.removeIf(reference -> reference.get() == null);

        for (WeakReference<L> listener : myListeners) {
            L l = listener.get();
            if (l != null) {
                runnable.run(l);
            }
        }
    }

    public void addListener(L listener) {
        myListeners.add(new WeakReference<>(listener));
        myListeners.removeIf(reference -> reference.get() == null);
    }

    public void removeListener(L listener) {
        myListeners.removeIf(reference -> reference.get() == null || reference.get() == listener);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListenersRunner)) return false;

        ListenersRunner runner = (ListenersRunner) o;

        return myListeners.equals(runner.myListeners);
    }

    @Override
    public int hashCode() {
        return myListeners.hashCode();
    }
}
