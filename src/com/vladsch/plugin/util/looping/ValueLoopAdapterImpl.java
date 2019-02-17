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

public class ValueLoopAdapterImpl<N, T> implements ValueLoopAdapter<N, T> {
    private final @NotNull ValueLoopConsumerAdapter<N, T> myConsumerAdapter;

    static class ConsumerAdapter<P, T> implements ValueLoopConsumerAdapter<P, T> {
        private final @NotNull Function<? super P, ? extends T> myFunction;
        private final @Nullable ValueLoopFilter<? super T> myFilter;

        public ConsumerAdapter(@NotNull final Function<? super P, ? extends T> function, @Nullable ValueLoopFilter<? super T> filter) {
            myFunction = function;
            myFilter = filter;
        }

        @NotNull
        @Override
        public <R> ValueLoopConsumer<? super P, R> getConsumer(final ValueLoopConsumer<? super T, R> valueConsumer) {
            return new MyValueLoopConsumer<>(myFunction, myFilter, valueConsumer);
        }

        @NotNull
        @Override
        public <R> ValueLoopConsumer<? super P, R> getConsumer(final VoidLoopConsumer<? super T> voidConsumer) {
            return new MyValueLoopConsumer<>(myFunction, myFilter, new VoidToValueLoopConsumerAdapter<>(voidConsumer));
        }
    }

    private static class MyValueLoopConsumer<P, T, R> implements ValueLoopConsumer<P, R> {
        private final ValueLoopConsumer<? super T, R> myConsumer;
        private final Function<? super P, ? extends T> myFunction;
        private final @Nullable ValueLoopFilter<? super T> myFilter;

        public MyValueLoopConsumer(final @NotNull Function<? super P, ? extends T> function, @Nullable ValueLoopFilter<? super T> filter, final ValueLoopConsumer<? super T, R> consumer) {
            myFunction = function;
            myFilter = filter;
            myConsumer = consumer;
        }

        @Override
        public void accept(@NotNull final P it, @NotNull final ValueLoop<R> loop) {
            T applied = myFunction.apply(it);
            if (applied == null || myFilter != null && !myFilter.filter(applied, loop)) {
                //loop.Continue();
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
        private final ValueLoopConsumerAdapter<? super P, T> myBeforeAdapter;
        private final ValueLoopConsumerAdapter<? super T, V> myAfterAdapter;

        public ChainedConsumerAdapter(final ValueLoopConsumerAdapter<? super P, T> beforeAdapter, final ValueLoopConsumerAdapter<? super T, V> afterAdapter) {
            myBeforeAdapter = beforeAdapter;
            myAfterAdapter = afterAdapter;
        }

        @NotNull
        @Override
        public <R> ValueLoopConsumer<? super P, R> getConsumer(final ValueLoopConsumer<? super V, R> valueConsumer) {
            return myBeforeAdapter.getConsumer(myAfterAdapter.getConsumer(valueConsumer));
        }

        @NotNull
        @Override
        public <R> ValueLoopConsumer<? super P, R> getConsumer(final VoidLoopConsumer<? super V> voidConsumer) {
            return myBeforeAdapter.getConsumer(myAfterAdapter.getConsumer(voidConsumer));
        }
    }

    public ValueLoopAdapterImpl(final @NotNull Function<? super N, T> function) {
        this(function, null);
    }

    public ValueLoopAdapterImpl(final @NotNull Function<? super N, T> function, final @Nullable ValueLoopFilter<? super T> filter) {
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
    public <V> ValueLoopAdapter<N, V> andThen(final ValueLoopAdapter<? super T, V> after) {
        return new ValueLoopAdapterImpl<N, V>(new ChainedConsumerAdapter<>(myConsumerAdapter, after.getConsumerAdapter()));
    }

    @NotNull
    @Override
    public ValueLoopAdapter<N, T> compose(final ValueLoopAdapter<? super N, N> before) {
        return new ValueLoopAdapterImpl<>(new ChainedConsumerAdapter<>(before.getConsumerAdapter(), myConsumerAdapter));
    }

    public static <N> ValueLoopAdapter<N, N> of() {
        return new ValueLoopAdapterImpl<>(Function.identity());
    }

    public static <N> ValueLoopAdapter<N, N> of(ValueLoopFilter<? super N> filter) {
        return new ValueLoopAdapterImpl<>(Function.identity(), filter);
    }

    public static <N, T> ValueLoopAdapter<N, T> of(Function<? super N, T> function) {
        return new ValueLoopAdapterImpl<>(function);
    }

    public static <N, T> ValueLoopAdapter<N, T> of(Class<T> clazz) {
        return new ValueLoopAdapterImpl<>((it) -> clazz.isInstance(it) ? clazz.cast(it) : null);
    }

    public static <N, T> ValueLoopAdapter<N, T> of(Class<T> clazz, Predicate<? super T> filter) {
        return new ValueLoopAdapterImpl<>(((it) -> clazz.isInstance(it) ? clazz.cast(it) : null), (it, loop) -> filter.test(it));
    }

    public static <N, T> ValueLoopAdapter<N, T> of(Class<T> clazz, ValueLoopFilter<? super T> filter) {
        return new ValueLoopAdapterImpl<>(((it) -> clazz.isInstance(it) ? clazz.cast(it) : null), filter);
    }
}
