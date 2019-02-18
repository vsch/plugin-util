/*
 * Copyright (c) 2015-2019 Vladimir Schneider <vladimir.schneider@gmail.com>, all rights reserved.
 *
 * This code is private property of the copyright holder and cannot be used without
 * having obtained a license or prior written permission of the of the copyright holder.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package com.vladsch.plugin.util.looping;

import com.intellij.openapi.diagnostic.Logger;
import com.vladsch.flexmark.util.options.MutableDataHolder;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Stack;
import java.util.function.Predicate;

final public class LoopInstance<N, R> implements ValueLoop<R> {
    final static private Logger LOG = Looping.LOG;
    final static private Logger LOG_INFO = Looping.LOG_INFO;
    final static private Logger LOG_TRACE = Looping.LOG_TRACE;

    private Iteration<N> myIteration;               // current iteration information
    private @Nullable Stack<Iteration<N>> myRecursions;       // recursion frames
    final @NotNull private LoopConstraints<N> myLoopConstraints;
    final @NotNull private Predicate<? super N> myRecursionPredicate;
    final @NotNull private Predicate<? super N> myFilterPredicate;
    private int myTotalLoopCount = 0;                        // total looping count across all nesting levels, including filtered out elements
    private int myTotalAcceptCount = 0;                            // total looping count across all nesting levels, only consumed elements
    private @Nullable N myMatch;
    private @Nullable MutableDataSet myDataSet;
    final private @NotNull Object myDefaultValue;

    // ValueResult
    private @NotNull Object myResult;
    private boolean myBreak = false;
    private boolean myHadRecurse = false;          // true if current iteration had performed recurse
    private boolean myIsDefaultResult = true;
    private int myMaxRecursions = 0;

    public LoopInstance(
            @NotNull LoopConstraints<N> loopConstraints,
            @NotNull Predicate<? super N> filterPredicate,
            @NotNull Predicate<? super N> recursionPredicate,
            @NotNull N element
    ) {
        this(loopConstraints, filterPredicate, recursionPredicate, element, VoidLoop.NULL);
    }

    public LoopInstance(
            @NotNull LoopConstraints<N> loopConstraints,
            @NotNull Predicate<? super N> filterPredicate,
            @NotNull Predicate<? super N> recursionPredicate,
            @NotNull N element,
            @NotNull Object defaultValue
    ) {
        myLoopConstraints = loopConstraints;
        myRecursionPredicate = recursionPredicate;
        myFilterPredicate = filterPredicate;
        myIteration = new Iteration<>(myLoopConstraints.getInitializer().apply(element));
        myDefaultValue = defaultValue;
        myResult = defaultValue;
    }

    public void iterate(@NotNull VoidLoopConsumer<? super N> consumer) {
        iterate(new VoidToValueLoopConsumerAdapter<>(consumer));
    }

    public void iterate(@NotNull ValueLoopConsumer<? super N, R> consumer) {
        consumer.beforeStart(this);
        if (LOG_INFO.isDebugEnabled()) LOG_INFO.debug("Starting looping " + myIteration);

        if (LOG_INFO.isDebugEnabled()) LOG_INFO.debug("Start recursion " + getRecursionLevel());
        
        while (true) {
            if (myIteration.next == null) {
                // see if all done, or just current 
                
                if (LOG_INFO.isDebugEnabled()) LOG_INFO.debug("End recursion " + getRecursionLevel());
                consumer.endRecursion(this);

                if (myRecursions == null || myRecursions.size() == 0) {
                    break;
                }
                
                dropRecursions(1, false);
                
                if (LOG_INFO.isDebugEnabled()) LOG_INFO.debug("Start recursion " + getRecursionLevel());
                consumer.startRecursion(this);
                
                continue;
            }

            myIteration.advance(myLoopConstraints.getIterator().apply(myIteration.next));
            myTotalLoopCount++;

            myMatch = myIteration.current;
            if (myMatch == null) continue;

            if (myFilterPredicate.test(myMatch)) {
                myIteration.acceptCount++;
                myTotalAcceptCount++;

                myHadRecurse = false;
                consumer.accept(myMatch, this);

                if (LOG_TRACE.isDebugEnabled())
                    LOG_TRACE.debug("Consumed, recursion: " + getRecursionLevel() + " isBreak " + myBreak + " recursed: " + myHadRecurse + " " + myIteration);
                if (myBreak) break;
            } else {
                if (LOG_TRACE.isDebugEnabled())
                    LOG_TRACE.debug("Skipping, recursion: " + getRecursionLevel() + " filtered out " + myIteration);
            }

            if (!myHadRecurse && myMatch != null && myRecursionPredicate.test(myMatch)) {
                if (LOG_TRACE.isDebugEnabled()) LOG_TRACE.debug("Recursing " + myIteration);
                Recurse();
                if (LOG_INFO.isDebugEnabled()) LOG_INFO.debug("Start recursion " + getRecursionLevel());
                consumer.startRecursion(this);
            }
        }

        consumer.afterEnd(this);
        if (LOG_INFO.isDebugEnabled()) LOG_INFO.debug("Done looping, totalLoopCount: " + myTotalLoopCount + " totalCount: " + myTotalAcceptCount + " maxRecursions: " + myMaxRecursions + " " + myIteration);
    }

    @Override
    public boolean getHaveNext() {
        return myIteration.next != null;
    }

    @Override
    public boolean getHaveAcceptableNext() {
        return myIteration.next != null && myFilterPredicate.test(myIteration.next);
    }

    @Override
    public void setResult(@NotNull final Object value) {
        myResult = value;
        myIsDefaultResult = false;
    }

    @NotNull
    @Override
    public R getResult() {
        //noinspection unchecked
        return (R) myResult;
    }

    @Override
    public void Return(@NotNull final Object value) {
        myResult = value;
        myBreak = true;
        myMatch = null;
        myIsDefaultResult = false;
    }

    /**
     * Unconditionally recurse into current element
     */
    private void Recurse() {
        if (myMatch != null && !myHadRecurse) {
            if (myRecursions == null) {
                myRecursions = new Stack<>();
            }

            myHadRecurse = true;
            myRecursions.push(myIteration);
            myIteration = new Iteration<>(myLoopConstraints.getInitializer().apply(myMatch));
            myMaxRecursions = Integer.max(myMaxRecursions, myRecursions.size());
            myMatch = null;
        }
    }

    @Override
    public void Return() {
        myBreak = true;
        myMatch = null;
    }

    private void dropRecursions(int iteration, final boolean inclusive) {
        int iterationIndex = iteration < 0 ? getRecursionLevel() + iteration : iteration;
        if (inclusive) iteration++;

        if (iteration > 0) {
            if (myRecursions == null || iteration > myRecursions.size()) {
                throw new IllegalArgumentException("Recursion level " + getRecursionLevel() + " is less than requested break/continue level " + iteration);
            }

            int i = iteration;
            while (i-- > 0) {
                myIteration = myRecursions.pop();
            }
        }
    }

    @Override
    public void Continue(final int recursionLevel) {
        if (myHadRecurse)
            throw new IllegalStateException("Continue(" + recursionLevel + ") called after Recurse() in current iteration");

        dropRecursions(recursionLevel, false);
        myMatch = null;
    }

    @Override
    public void Break(final int recursionLevel) {
        if (myHadRecurse)
            throw new IllegalStateException("Break(" + recursionLevel + ") called after Recurse() in current iteration");

        if (recursionLevel == getRecursionLevel()) {
            // we are breaking out completely of all iterations and returning
            myBreak = true;
        } else {
            dropRecursions(recursionLevel, true);
        }
        myMatch = null;
    }

    @Override
    public MutableDataHolder getData() {
        if (myDataSet == null) {
            myDataSet = new MutableDataSet();
        }
        return myDataSet;
    }

    @Nullable
    public N getMatch() {
        return myMatch;
    }

    @Override
    public boolean isComplete() {
        return myMatch == null;
    }

    @Override
    public boolean isIncomplete() {
        return myMatch != null;
    }

    @Override
    public void Complete() {
        myMatch = null;
    }

    @Override
    public boolean isTerminated() {
        return myBreak;
    }

    @Override
    public int getLoopCount() {
        return myIteration.loopCount;
    }

    @Override
    public int getAcceptCount() {
        return myIteration.acceptCount;
    }

    @Override
    public int getTotalLoopCount() {
        return myTotalLoopCount;
    }

    @Override
    public int getTotalAcceptCount() {
        return myTotalAcceptCount;
    }

    @Override
    public int getRecursionLevel() {
        return myRecursions == null ? 0 : myRecursions.size();
    }

    @Override
    public boolean isDefaultResult() {
        return myIsDefaultResult;
    }

    @NotNull
    @Override
    public R getDefaultValue() {
        //noinspection unchecked
        return (R) myDefaultValue;
    }

    public static class Iteration<V> {
        @Nullable V current; // current looping variable
        @Nullable V next;    // current looping variable
        int acceptCount;           // iteration count of valid filtered elements
        int loopCount;       // iteration count of all advance ops

        Iteration(@Nullable final V next) {
            this.next = next;
            this.current = next;
            acceptCount = 0;
            loopCount = 0;
        }

        void advance(@Nullable final V nextValue) {
            current = next;
            next = nextValue;
            loopCount++;
        }

        @Override
        public String toString() {
            String currentText = current == null ? "null" : current.toString();
            String nextText = next == null ? "null" : next.toString();
            return "Iteration {" +
                    ", count=" + acceptCount +
                    ", current=" + currentText.subSequence(0, Integer.min(currentText.length(), 30)) +
                    ", next=" + nextText.subSequence(0, Integer.min(nextText.length(), 30)) +
                    ", loopCount=" + loopCount +
                    '}';
        }
    }
}
