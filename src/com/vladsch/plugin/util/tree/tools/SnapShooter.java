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

package com.vladsch.plugin.util.tree.tools;

import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class SnapShooter<N, R> implements AggregatorFactory<N, Pair<R, R>> {
    private final @NotNull AggregatorFactory<N, R> myFactory;
    private final @NotNull Predicate<? super N> myTrigger;
    private final boolean myInitialState;

    public SnapShooter(@NotNull final AggregatorFactory<N, R> factory, @NotNull final Predicate<? super N> trigger, boolean initialState) {
        myFactory = factory;
        myTrigger = trigger;
        myInitialState = initialState;
    }

    @Override
    public Aggregator<N, Pair<R, R>> create() {
        return new Snapper<N, R>(myFactory.create(), myTrigger, myInitialState);
    }

    private static class Snapper<N, R> implements Aggregator<N, Pair<R, R>> {
        private final @NotNull Aggregator<N, R> myAggregator;
        private final @NotNull Predicate<? super N> myTrigger;
        private final boolean myInitialState;
        private boolean myTriggered;

        public Snapper(@NotNull final Aggregator<N, R> aggregator, @NotNull final Predicate<? super N> trigger, final boolean initialState) {
            myAggregator = aggregator;
            myTrigger = trigger;
            myInitialState = initialState;
            myTriggered = initialState;
        }

        @Override
        public Pair<R, R> apply(final @Nullable Pair<R, R> r, final N n) {
            if (myTriggered == myInitialState && myTrigger.test(n)) {
                myTriggered = !myTriggered;
            }

            R runningTotal = myAggregator.apply(r == null ? null : r.getFirst(), n);
            R snapshot = r == null ? null : r.getSecond();

            if (myTriggered) {
                snapshot = runningTotal;
            }

            return new Pair<>(runningTotal, snapshot);
        }
    }
}
