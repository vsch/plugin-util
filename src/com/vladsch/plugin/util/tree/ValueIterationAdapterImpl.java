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

package com.vladsch.plugin.util.tree;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;

public class ValueIterationAdapterImpl<N, T> implements ValueIterationAdapter<N, T> {
    private final @NotNull ValueIterationConsumerAdapter<N, T> myConsumerAdapter;

    static class ConsumerAdapter<P, T> implements ValueIterationConsumerAdapter<P, T> {
        private final @NotNull Function<? super P, ? extends T> myFunction;
        private final @Nullable ValueIterationFilter<? super T> myFilter;

        public ConsumerAdapter(@NotNull final Function<? super P, ? extends T> function, @Nullable ValueIterationFilter<? super T> filter) {
            myFunction = function;
            myFilter = filter;
        }

        @NotNull
        @Override
        public <R> ValueIterationConsumer<? super P, R> getConsumer(final ValueIterationConsumer<? super T, R> valueConsumer) {
            return new MyValueIterationConsumer<>(myFunction, myFilter, valueConsumer);
        }

        @NotNull
        @Override
        public <R> ValueIterationConsumer<? super P, R> getConsumer(final VoidIterationConsumer<? super T> voidConsumer) {
            return new MyValueIterationConsumer<>(myFunction, myFilter, new VoidToValueIConsumerAdapter<>(voidConsumer));
        }
    }

    private static class MyValueIterationConsumer<P, T, R> implements ValueIterationConsumer<P, R> {
        private final ValueIterationConsumer<? super T, R> myConsumer;
        private final Function<? super P, ? extends T> myFunction;
        private final @Nullable ValueIterationFilter<? super T> myFilter;

        public MyValueIterationConsumer(final @NotNull Function<? super P, ? extends T> function, @Nullable ValueIterationFilter<? super T> filter, final ValueIterationConsumer<? super T, R> consumer) {
            myFunction = function;
            myFilter = filter;
            myConsumer = consumer;
        }

        @Override
        public void accept(@NotNull final P it, @NotNull final ValueIteration<R> iteration) {
            T applied = myFunction.apply(it);
            if (applied == null || myFilter != null && !myFilter.filter(applied, iteration)) {
                //loop.Continue();
            } else {
                myConsumer.accept(applied, iteration);
            }
        }

        @Override
        public void beforeStart(@NotNull final ValueIteration<R> iteration) {
            myConsumer.beforeStart(iteration);
        }

        @Override
        public void startRecursion(@NotNull final VoidIteration iteration) {
            myConsumer.startRecursion(iteration);
        }

        @Override
        public void endRecursion(@NotNull final VoidIteration iteration) {
            myConsumer.endRecursion(iteration);

        }

        @Override
        public void afterEnd(@NotNull final ValueIteration<R> iteration) {
            myConsumer.afterEnd(iteration);
        }
    }

    static class ChainedConsumerAdapter<P, T, V> implements ValueIterationConsumerAdapter<P, V> {
        private final ValueIterationConsumerAdapter<? super P, T> myBeforeAdapter;
        private final ValueIterationConsumerAdapter<? super T, V> myAfterAdapter;

        public ChainedConsumerAdapter(final ValueIterationConsumerAdapter<? super P, T> beforeAdapter, final ValueIterationConsumerAdapter<? super T, V> afterAdapter) {
            myBeforeAdapter = beforeAdapter;
            myAfterAdapter = afterAdapter;
        }

        @NotNull
        @Override
        public <R> ValueIterationConsumer<? super P, R> getConsumer(final ValueIterationConsumer<? super V, R> valueConsumer) {
            return myBeforeAdapter.getConsumer(myAfterAdapter.getConsumer(valueConsumer));
        }

        @NotNull
        @Override
        public <R> ValueIterationConsumer<? super P, R> getConsumer(final VoidIterationConsumer<? super V> voidConsumer) {
            return myBeforeAdapter.getConsumer(myAfterAdapter.getConsumer(voidConsumer));
        }
    }

    public ValueIterationAdapterImpl(final @NotNull Function<? super N, T> function) {
        this(function, null);
    }

    public ValueIterationAdapterImpl(final @NotNull Function<? super N, T> function, final @Nullable ValueIterationFilter<? super T> filter) {
        this(new ConsumerAdapter<>(function, filter));
    }

    public ValueIterationAdapterImpl(@NotNull ValueIterationConsumerAdapter<N, T> consumerAdapter) {
        myConsumerAdapter = consumerAdapter;
    }

    @NotNull
    @Override
    public ValueIterationConsumerAdapter<N, T> getConsumerAdapter() {
        return myConsumerAdapter;
    }

    @NotNull
    @Override
    public <V> ValueIterationAdapter<N, V> andThen(final ValueIterationAdapter<? super T, V> after) {
        return new ValueIterationAdapterImpl<N, V>(new ChainedConsumerAdapter<>(myConsumerAdapter, after.getConsumerAdapter()));
    }

    @NotNull
    @Override
    public ValueIterationAdapter<N, T> compose(final ValueIterationAdapter<? super N, N> before) {
        return new ValueIterationAdapterImpl<>(new ChainedConsumerAdapter<>(before.getConsumerAdapter(), myConsumerAdapter));
    }

    public static <N> ValueIterationAdapter<N, N> of() {
        return new ValueIterationAdapterImpl<>(Function.identity());
    }

    public static <N> ValueIterationAdapter<N, N> of(ValueIterationFilter<? super N> filter) {
        return new ValueIterationAdapterImpl<>(Function.identity(), filter);
    }

    public static <N, T> ValueIterationAdapter<N, T> of(Function<? super N, T> function) {
        return new ValueIterationAdapterImpl<>(function);
    }

    public static <N, T> ValueIterationAdapter<N, T> of(Class<T> clazz) {
        return new ValueIterationAdapterImpl<>((it) -> clazz.isInstance(it) ? clazz.cast(it) : null);
    }

    public static <N, T> ValueIterationAdapter<N, T> of(Class<T> clazz, Predicate<? super T> filter) {
        return new ValueIterationAdapterImpl<>(((it) -> clazz.isInstance(it) ? clazz.cast(it) : null), (it, loop) -> filter.test(it));
    }

    public static <N, T> ValueIterationAdapter<N, T> of(Class<T> clazz, ValueIterationFilter<? super T> filter) {
        return new ValueIterationAdapterImpl<>(((it) -> clazz.isInstance(it) ? clazz.cast(it) : null), filter);
    }
}
