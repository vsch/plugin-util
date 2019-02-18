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

import com.vladsch.flexmark.util.options.MutableDataHolder;

public interface VoidLoop {
    Object NULL = new Object();

    /**
     * Complete current iteration, ie. isComplete() will report true and isIncomplete() false
     * <p>
     * Purely a convenience feature to use without needing to track if break or continue have been executed
     */
    void Complete();

    /**
     * Continue with the next iteration of the given recursion level
     *
     * @param recursionLevel 0 current, <0 previous recursion levels, >0 actual recursion level
     */
    void Continue(int recursionLevel);

    /**
     * Break out of given recursion level
     *
     * @param recursionLevel 0 current, <0 previous recursion levels, >0 actual recursion level
     */
    void Break(int recursionLevel);

    /**
     * Break out of all recursion levels and return current result value for the loop if value loop
     */
    void Return();

    /**
     * Continue with next iteration of current recursion level
     */
    default void Continue() { Continue(0); }

    /**
     * Break the current recursion level, if last level then same as {@link #Return()}
     */
    default void Break() { Break(0); }

    /**
     * @return true if have next element, does not mean it matches filters, just raw next from loop iterator, fast
     *         check
     */
    boolean getHaveNext();

    /**
     * @return true if have next element and it passes element filters.
     *         <p>
     *         NOTE: {@link ValueLoopConsumerAdapter} not invoked. It is part of the consumer.accept() call
     *         hierarchy and can have code side-effects. Only predicate filters are tested. This does not mean the final
     *         consumer will see this value.
     */
    boolean getHaveAcceptableNext();

    /**
     * @return true if looping terminated by {@link #Return()}, or {@link #Break()} of the last recursion level.
     */
    boolean isTerminated();

    /**
     * @return true if current iteration is complete ie. had ( Break(), Continue(), Return(), Complete())
     */
    boolean isComplete();

    /**
     * @return true if current iteration is not complete, ie. need to continue processing
     */
    boolean isIncomplete();

    /**
     * @return times through the loop of the current recursion level, includes skipped elements due to filtering
     */
    int getLoopCount();

    /**
     * @return total times consumer was invoked, ie. valid elements
     */
    int getAcceptCount();

    /**
     * @return current loop count across all recursions
     */
    int getTotalLoopCount();

    /**
     * @return accept count across all recursions
     */
    int getTotalAcceptCount();

    /**
     * @return count of recursion level, can use in Break(recursionLevel) or Continue(recursionLevel) to break/continue
     *         a particular recursion
     */
    int getRecursionLevel();

    /**
     * Per loop instance data instance can be used to store context information
     * <p>
     * Exists between {@link VoidLoopConsumer#beforeStart(VoidLoop)} and {@link ValueLoopConsumer#afterEnd(ValueLoop) }
     * for consumer and
     * <p>
     * lifetime of {@link LoopInstance} for caller of {@link Loop#iterate}
     *
     * @return per loop instance mutable data holder
     */
    MutableDataHolder getData();
}
