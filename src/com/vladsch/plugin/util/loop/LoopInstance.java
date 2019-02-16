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

package com.vladsch.plugin.util.loop;

import com.vladsch.flexmark.util.options.MutableDataHolder;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Stack;
import java.util.function.Predicate;

final public class LoopInstance<N, R> implements ValueLoop<R> {
    private Iteration<N> myIteration;               // current iteration information
    private @Nullable Stack<Iteration<N>> myRecursions;       // recursion frames
    final @NotNull private LoopConstraints<N> myConditions;
    final @NotNull private Predicate<N> myRecursion;
    final @NotNull private Predicate<N> myFilter;
    private int myTotalLoopCount;                        // total looping count across all nesting levels, including filtered out elements
    private int myTotalCount;                            // total looping count across all nesting levels, only consumed elements
    private @Nullable N myMatch;
    private @Nullable MutableDataSet myDataSet;

    // ValueResult
    private Object myResult;
    private boolean myBreak;
    private boolean myRecursed;
    private boolean myIsDefaultResult;

    public LoopInstance(@NotNull LoopConstraints<N> conditions, @NotNull Predicate<N> filter, @NotNull Predicate<N> recursion, @NotNull N element) {
        this(conditions, filter, recursion, element, VoidLoop.NULL);
    }

    public LoopInstance(@NotNull LoopConstraints<N> conditions, @NotNull Predicate<N> filter, @NotNull Predicate<N> recursion, @NotNull N element, @NotNull Object defaultValue) {
        myConditions = conditions;
        myRecursion = recursion;
        myFilter = filter;
        myIteration = new Iteration<>(myConditions.getInitializer().apply(element));
        myTotalLoopCount = 0;
        myTotalCount = 0;
        myResult = defaultValue;
        myBreak = false;
        myRecursed = false;
        myIsDefaultResult = true;
    }

    static class Iteration<V> {
        @Nullable V current; // current looping variable
        @Nullable V next;    // current looping variable
        int count;           // iteration count of valid filtered elements
        int loopCount;       // iteration count of all advance ops

        Iteration(@Nullable final V next) {
            this.next = next;
            this.current = next;
            count = 0;
            loopCount = 0;
        }

        void advance(@Nullable final V nextValue) {
            current = next;
            next = nextValue;
            loopCount++;
        }
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
    public boolean haveNext() {
        return myIteration.next != null;
    }

    @Override
    public boolean isTerminated() {
        return myMatch == null;
    }

    @Override
    public boolean isActive() {
        return myMatch != null;
    }

    @Override
    public boolean isBreak() {
        return myBreak;
    }

    @Override
    public int getLoopCount() {
        return myIteration.loopCount;
    }

    @Override
    public int getCount() {
        return myIteration.count;
    }

    @Override
    public int getTotalLoopCount() {
        return myTotalLoopCount;
    }

    @Override
    public int getTotalCount() {
        return myTotalCount;
    }

    @Override
    public int getRecursionCount() {
        return myRecursions == null ? 0 : myRecursions.size();
    }

    @Override
    public boolean isDefaultResult() {
        return myIsDefaultResult;
    }

    public void iterate(@NotNull VoidLoopConsumer<N> consumer) {
        iterate(new VoidToValueLoopConsumerAdapter<>(consumer));
    }

    public void iterate(@NotNull ValueLoopConsumer<N, R> consumer) {
        consumer.beforeStart(this);

        while (true) {
            if (myIteration.next == null) {
                // see if all done, or just current 
                if (myRecursions == null || myRecursions.size() == 0) break;
                dropRecursions(1);
                continue;
            }
            
            myIteration.advance(myConditions.getIterator().apply(myIteration.next));
            myTotalLoopCount++;

            myMatch = myIteration.current;
            if (myMatch == null) continue;

            if (myFilter.test(myMatch)) {
                myIteration.count++;
                myTotalCount++;

                myRecursed = false;
                consumer.accept(myMatch, this);

                if (myBreak) break;
            }

            if (!myRecursed && myMatch != null && myRecursion.test(myMatch)) {
                Recurse();
            }
        }

        consumer.afterEnd(this);
    }

    @Override
    public void setResult(@NotNull final Object value) {
        myResult = value;
        myIsDefaultResult = false;
    }

    @NotNull
    @Override
    public R getResult() {
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
    @Override
    public void Recurse() {
        if (myMatch != null && !myRecursed) {
            if (myRecursions == null) {
                myRecursions = new Stack<>();
            }

            myRecursed = true;
            myRecursions.add(myIteration);
            myIteration = new Iteration<>(myConditions.getInitializer().apply(myMatch));
        }
    }

    @Override
    public void Return() {
        myBreak = true;
        myMatch = null;
    }

    private void dropRecursions(int outerLevels) {
        if (outerLevels > 0) {
            if (myRecursions == null || outerLevels > myRecursions.size()) {
                throw new IllegalArgumentException("Recursion level " + getRecursionCount() + " is less than requested break/continue level " + outerLevels);
            }

            int i = outerLevels;
            while (i-- > 0) {
                myIteration = myRecursions.pop();
            }
        }
    }

    @Override
    public void Continue(final int outerLevels) {
        if (outerLevels > 0) dropRecursions(outerLevels);
        myMatch = null;
    }

    @Override
    public void Break(final int outerLevels) {
        if (outerLevels == getRecursionCount()) {
            // we are breaking out completely of all iterations and returning
            myBreak = true;
        } else {
            dropRecursions(outerLevels + 1);
        }
        myMatch = null;
    }
}
