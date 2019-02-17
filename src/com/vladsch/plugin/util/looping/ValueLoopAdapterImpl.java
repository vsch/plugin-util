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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;

public class ValueLoopAdapterImpl<N, T, R> implements ValueLoopAdapter<N, T> {
    private final @NotNull ValueLoopConsumerAdapter<N, T> myConsumerAdapter;

    static class ConsumerAdapter<P, T> implements ValueLoopConsumerAdapter<P, T> {
        private final @NotNull Function<? super P, ? extends T> myFunction;
        private final @Nullable ValueLoopFilter<T> myFilter;

        public ConsumerAdapter(@NotNull final Function<? super P, ? extends T> function, @Nullable ValueLoopFilter<T> filter) {
            myFunction = function;
            myFilter = filter;
        }

        @NotNull
        @Override
        public <R> ValueLoopConsumer<P, R> getConsumer(final ValueLoopConsumer<T, R> valueConsumer) {
            return new MyValueLoopConsumer<>(myFunction, myFilter, valueConsumer);
        }

        @NotNull
        @Override
        public <R> ValueLoopConsumer<P, R> getConsumer(final VoidLoopConsumer<T> voidConsumer) {
            return new MyValueLoopConsumer<>(myFunction, myFilter, new VoidToValueLoopConsumerAdapter<>(voidConsumer));
        }
    }

    private static class MyValueLoopConsumer<P, T, R> implements ValueLoopConsumer<P, R> {
        private final ValueLoopConsumer<T, R> myConsumer;
        private final Function<? super P, ? extends T> myFunction;
        private final @Nullable ValueLoopFilter<T> myFilter;

        public MyValueLoopConsumer(final @NotNull Function<? super P, ? extends T> function, @Nullable ValueLoopFilter<T> filter, final ValueLoopConsumer<T, R> consumer) {
            myFunction = function;
            myFilter = filter;
            myConsumer = consumer;
        }

        @Override
        public void accept(@NotNull final P it, @NotNull final ValueLoop<R> loop) {
            T applied = myFunction.apply(it);
            if (applied == null || myFilter != null && !myFilter.filter(applied, loop)) {
                loop.Continue();
            } else {
                myConsumer.accept(applied, loop);
            }
        }

        @Override
        public void afterEnd(@NotNull final ValueLoop<R> loop) {
            myConsumer.afterEnd(loop);
        }

        @Override
        public void beforeStart(@NotNull final ValueLoop<R> loop) {
            myConsumer.beforeStart(loop);
        }
    }

    static class ChainedConsumerAdapter<P, T, V> implements ValueLoopConsumerAdapter<P, V> {
        private final ValueLoopConsumerAdapter<P, T> myBeforeAdapter;
        private final ValueLoopConsumerAdapter<T, V> myAfterAdapter;

        public ChainedConsumerAdapter(final ValueLoopConsumerAdapter<P, T> beforeAdapter, final ValueLoopConsumerAdapter<T, V> afterAdapter) {
            myBeforeAdapter = beforeAdapter;
            myAfterAdapter = afterAdapter;
        }

        @NotNull
        @Override
        public <R> ValueLoopConsumer<P, R> getConsumer(final ValueLoopConsumer<V, R> valueConsumer) {
            return myBeforeAdapter.getConsumer(myAfterAdapter.getConsumer(valueConsumer));
        }

        @NotNull
        @Override
        public <R> ValueLoopConsumer<P, R> getConsumer(final VoidLoopConsumer<V> voidConsumer) {
            return myBeforeAdapter.getConsumer(myAfterAdapter.getConsumer(voidConsumer));
        }
    }

    public ValueLoopAdapterImpl(final @NotNull Function<N, T> function) {
        this(function, null);
    }

    public ValueLoopAdapterImpl(final @NotNull Function<N, T> function, final @Nullable ValueLoopFilter<T> filter) {
        this(new ConsumerAdapter<>(function, filter));
    }

    public ValueLoopAdapterImpl(@NotNull ValueLoopConsumerAdapter<N, T> consumerAdapter) {
        myConsumerAdapter = consumerAdapter;
    }

    @NotNull
    @Override
    public ValueLoopConsumerAdapter<N, T> getConsumerAdapter() {
        return myConsumerAdapter;
    }

    @NotNull
    @Override
    public <V> ValueLoopAdapter<N, V> andThen(final ValueLoopAdapter<T, V> after) {
        return new ValueLoopAdapterImpl<N, V, R>(new ChainedConsumerAdapter<>(myConsumerAdapter, after.getConsumerAdapter()));
    }

    @NotNull
    @Override
    public ValueLoopAdapter<N, T> compose(final ValueLoopAdapter<N, N> before) {
        return new ValueLoopAdapterImpl<>(new ChainedConsumerAdapter<>(before.getConsumerAdapter(), myConsumerAdapter));
    }

    public static <N, R> ValueLoopAdapter<N, N> of() {
        return new ValueLoopAdapterImpl<>(Function.identity());
    }

    public static <N, R> ValueLoopAdapter<N, N> of(ValueLoopFilter<N> filter) {
        return new ValueLoopAdapterImpl<>(Function.identity(), filter);
    }

    public static <N, T, R> ValueLoopAdapter<N, T> of(Function<N, T> function) {
        return new ValueLoopAdapterImpl<>(function);
    }

    public static <N, T, R> ValueLoopAdapter<N, T> of(Class<T> clazz) {
        return new ValueLoopAdapterImpl<>((it) -> clazz.isInstance(it) ? clazz.cast(it) : null);
    }

    public static <N, T, R> ValueLoopAdapter<N, T> of(Class<T> clazz, Predicate<T> filter) {
        return new ValueLoopAdapterImpl<>(((it) -> clazz.isInstance(it) ? clazz.cast(it) : null), (it, loop) -> filter.test(it));
    }

    public static <N, T, R> ValueLoopAdapter<N, T> of(Class<T> clazz, ValueLoopFilter<T> filter) {
        return new ValueLoopAdapterImpl<>(((it) -> clazz.isInstance(it) ? clazz.cast(it) : null), filter);
    }
}
