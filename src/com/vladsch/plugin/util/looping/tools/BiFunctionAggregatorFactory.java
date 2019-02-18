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

package com.vladsch.plugin.util.looping.tools;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class BiFunctionAggregatorFactory<N, R> implements AggregatorFactory<N, R> {
    private final @NotNull AggregatorImpl<N, R> myAggregator;

    public BiFunctionAggregatorFactory(@NotNull final BiFunction<R, N, R> aggregator) {
        myAggregator = new AggregatorImpl<>(aggregator);
    }

    @Override
    public Aggregator<N, R> create() {
        return myAggregator;
    }

    public static class AggregatorImpl<N, R> implements Aggregator<N, R> {
        private final @NotNull BiFunction<R, N, R> myFunction;

        public AggregatorImpl(@NotNull final BiFunction<R, N, R> function) {
            myFunction = function;
        }

        @Override
        public R apply(final R r, final N n) {
            return myFunction.apply(r, n);
        }
    }
}
