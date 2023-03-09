/*
 *
 */
package com.vladsch.plugin.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.HashSet;

public class ListenerNotifier<L> {
    final protected HashSet<WeakReference<L>> listeners = new HashSet<>();

    public HashSet<WeakReference<L>> getListeners() {
        return listeners;
    }

    public interface RunnableNotifier<L> {
        boolean notify(L listener);
    }

    public ListenerNotifier() {

    }

    public void addListener(@NotNull final L listener) {
        addListener(listener, null);
    }

    public void addListener(@NotNull final L listener, @Nullable RunnableNotifier<L> runnableNotifier) {
        synchronized (listeners) {
            removeListener(listener);
            listeners.add(new WeakReference<>(listener));

            // the delegate should check for necessary conditions for listener update
            if (runnableNotifier != null) runnableNotifier.notify(listener);
        }
    }

    public void removeListener(@NotNull final L listener) {
        removeListener(listener, null);
    }

    public void removeListener(@NotNull final L listener, @Nullable RunnableNotifier<L> runnableNotifier) {
        synchronized (listeners) {
            WeakReference[] listenerList = listeners.toArray(new WeakReference[listeners.size()]);

            for (WeakReference listenerRef : listenerList) {
                if (listenerRef.get() == null || listenerRef.get() == listener) {
                    listeners.remove(listenerRef);
                }
            }

            if (runnableNotifier != null) notifyListeners(runnableNotifier);
        }
    }

    public void notifyListeners(@NotNull RunnableNotifier<L> runnableNotifier) {
        synchronized (listeners) {
            WeakReference[] listenerList = listeners.toArray(new WeakReference[listeners.size()]);

            L listener;
            for (WeakReference listenerRef : listenerList) {
                if ((listener = (L) listenerRef.get()) != null && runnableNotifier.notify(listener)) break;
            }
        }
    }
}
