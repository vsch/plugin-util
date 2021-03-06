package com.vladsch.plugin.util;

import com.intellij.concurrency.JobScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class CancelableJobScheduler {
    final private SortedArrayList<CancellableJob> myRunnables = new SortedArrayList<>(Comparator.comparingLong(o -> o.myScheduledTickTime));
    private AtomicLong myTickTime;
    final private int myResolution = 25;
    final private TimeUnit myTimeUnit = TimeUnit.MILLISECONDS;

    public CancelableJobScheduler() {
        myTickTime = new AtomicLong(Long.MIN_VALUE);
        JobScheduler.getScheduler().scheduleWithFixedDelay(this::onTimerTick, myResolution, myResolution, myTimeUnit);
    }

    public int getResolution() {
        return myResolution;
    }

    public TimeUnit getTimeUnit() {
        return myTimeUnit;
    }

    private void onTimerTick() {
        long tickTime = myTickTime.addAndGet(myResolution);

        // run all tasks whose tickTime is <= tickTime
        CancellableJob dummy = new CancellableJob("", tickTime, this::onTimerTick);
        final ArrayList<CancellableJob> toRun = new ArrayList<>();
        synchronized (myRunnables) {
            myRunnables.removeIfBefore(dummy, toRun::add);
        }

        while (!toRun.isEmpty()) {
            CancellableJob runnable = toRun.remove(0);

            try {
                runnable.run();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public CancellableRunnable schedule(String id, int delay, Runnable command) {
        CancellableJob runnable;
        delay = Math.max(delay, myResolution);

        synchronized (myRunnables) {
            runnable = new CancellableJob(id, myTickTime.get() + delay, command);
            myRunnables.add(runnable);
        }
        return runnable;
    }

    public CancellableRunnable schedule(int delay, Runnable command) {
        CancellableJob runnable;
        delay = Math.max(delay, myResolution);

        synchronized (myRunnables) {
            runnable = new CancellableJob("", myTickTime.get() + delay, command);
            myRunnables.add(runnable);
        }
        return runnable;
    }

    public CancellableRunnable schedule(int delay, CancellableRunnable command) {
        CancellableJob runnable;
        delay = Math.max(delay, myResolution);

        synchronized (myRunnables) {
            runnable = new CancellableJob(command.getId(), myTickTime.get() + delay, command);
            myRunnables.add(runnable);
        }
        return runnable;
    }

    private static class CancellableJob implements CancellableRunnable {
        private final Runnable myRunnable;
        final long myScheduledTickTime;
        private final AtomicBoolean myHasRun = new AtomicBoolean(false);
        private final String myID;

        CancellableJob(String id, long scheduledTickTime, Runnable runnable) {
            myRunnable = runnable;
            myScheduledTickTime = scheduledTickTime;
            myID = id;
        }

        @Override
        public boolean cancel() {
            boolean result = !myHasRun.getAndSet(true);
            if (myRunnable instanceof CancellableRunnable) ((CancellableRunnable) myRunnable).cancel();
            return result;
        }

        @NotNull
        @Override
        public String getId() {
            return "CancellableJob(" + myID + ")";
        }

        @Override
        public boolean canRun() {
            return !myHasRun.get();
        }

        @Override
        public void run() {
            if (!myHasRun.getAndSet(true)) {
                myRunnable.run();
            }
        }
    }
}
