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

import com.vladsch.plugin.util.looping.Utils;
import com.vladsch.plugin.util.looping.ValueLoop;
import com.vladsch.plugin.util.looping.ValueLoopConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MultiAggregatorFactory<N, K> {
    public static final IntermediateResult[] EMPTY_ARRAY = new IntermediateResult[0];

    private final ArrayList<AggregatorFactoryInstance> myAggregatorFactoryList = new ArrayList<>();

    public <R> void addAggregator(@NotNull K key, @NotNull AggregatorFactory<N, R> aggregatorFactory) {
        myAggregatorFactoryList.add(new AggregatorFactoryInstance<>(key, aggregatorFactory));
    }

    public ValueLoopConsumer<N, Map<K, Result>> getConsumer() {
        return new MultiCastingConsumer<N, K>((AggregatorFactoryInstance[]) myAggregatorFactoryList.toArray());
    }

    static class AggregatorFactoryInstance<N, K, R> {
        private final @NotNull K myKey;
        private final @NotNull AggregatorFactory<N, R> myAggregatorFactory;

        public AggregatorFactoryInstance(@NotNull final K key, @NotNull final AggregatorFactory<N, R> aggregatorFactory) {
            myKey = key;
            myAggregatorFactory = aggregatorFactory;
        }

        public IntermediateResult<N, K, R> create() {
            return new IntermediateResult<>(myKey, myAggregatorFactory.create());
        }
    }

    static class MultiCastingConsumer<N, K> implements ValueLoopConsumer<N, Map<K, Result>> {
        private final IntermediateResult[] myIntermediateResults;

        public MultiCastingConsumer(final AggregatorFactoryInstance[] aggregatorFactoryList) {
            myIntermediateResults = new IntermediateResult[aggregatorFactoryList.length];
            int i = aggregatorFactoryList.length;
            while (i-- > 0) {
                myIntermediateResults[i] = aggregatorFactoryList[i].create();
            }
        }

        @Override
        public void accept(@NotNull final N it, @NotNull final ValueLoop<Map<K, Result>> loop) {
            for (IntermediateResult value : myIntermediateResults) {
                //noinspection unchecked
                value.accept(it);
            }
        }

        @Override
        public void afterEnd(@NotNull final ValueLoop<Map<K, Result>> loop) {
            HashMap<K, Result> results = new HashMap<>();

            for (IntermediateResult result : myIntermediateResults) {
                //noinspection unchecked
                result.putResult(results);
            }

            loop.Return(results);
        }
    }

    static class IntermediateResult<N, K, R> {
        private final @NotNull K myKey;
        private final @NotNull Aggregator<N, R> myConsumer;
        private @Nullable R myValue;

        public IntermediateResult(@NotNull K key, @NotNull Aggregator<N, R> consumer) {
            myKey = key;
            myConsumer = consumer;
        }

        public void putResult(HashMap<K, Result> map) {
            map.put(myKey, new Result<>(myValue));
        }

        public void accept(N value) {
            myValue = myConsumer.apply(myValue, value);
        }
    }

    static class Result<R> {
        private final @Nullable R myValue;

        public Result(final @Nullable R value) {
            myValue = value;
        }

        @Nullable
        public R getValue() {
            return myValue;
        }
    }
}
