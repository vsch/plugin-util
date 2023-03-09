package com.vladsch.plugin.util;

/**
 * Entry handled by flag value.
 * entry will be allowed if the passed flag is > than the flag of one already inside
 * flag 0 will enter only if no one is inside
 * <p>
 * Not intended for thread safety but for UI recursion when processing events that modify components
 */
public class RecursionGuard {
    private int myInside = 0;

    public boolean enter(int rank, Runnable runnable) {
        if (rank > myInside || myInside == 0) {
            int saved = myInside;
            myInside = rank > 0 ? rank : 1;

            try {
                runnable.run();
            } finally {
                myInside = saved;
            }
            return true;
        }
        return false;
    }
}
