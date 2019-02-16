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

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

public class MappedLoopingImpl<N, T> implements MappedLooping<N, T> {
    private final @NotNull N myElement;
    private final @NotNull ValueLoopAdapter<N, T> myAdapter;
    private final @NotNull LoopingImpl<N> myLooping;

    public MappedLoopingImpl(@NotNull final N element, @NotNull ValueLoopAdapter<N, T> adapter, @NotNull LoopingImpl<N> looping) {
        myElement = element;
        myAdapter = adapter;
        myLooping = looping;
    }

    @Override
    @NotNull
    public LoopingImpl<N> getLooping() {
        return myLooping;
    }

    @NotNull
    @Override
    public MappedLooping<N, T> reversed() {
        return new MappedLoopingImpl<>(myElement, myAdapter, myLooping.reversed());
    }

    @NotNull
    @Override
    public MappedLooping<N, T> recursive() {
        return new MappedLoopingImpl<>(myElement, myAdapter, myLooping.recursive());
    }

    @NotNull
    @Override
    public MappedLooping<N, T> nonRecursive() {
        return new MappedLoopingImpl<>(myElement, myAdapter, myLooping.nonRecursive());
    }

    @NotNull
    @Override
    public MappedLooping<N, T> recurse(@NotNull final Predicate<N> predicate) {
        return new MappedLoopingImpl<>(myElement, myAdapter, myLooping.recurse(predicate));
    }

    @NotNull
    @Override
    public MappedLooping<N, T> recurse(@NotNull final Class clazz) {
        return new MappedLoopingImpl<>(myElement, myAdapter, myLooping.recurse(clazz));
    }

    @NotNull
    @Override
    public <F> MappedLooping<N, T> recurse(@NotNull final Class<F> clazz, @NotNull final Predicate<F> predicate) {
        return new MappedLoopingImpl<>(myElement, myAdapter, myLooping.recurse(clazz, predicate));
    }

    @NotNull
    @Override
    public MappedLooping<N, T> filterFalse() {
        return aborted();
    }

    @NotNull
    @Override
    public MappedLooping<N, T> aborted() {
        return new MappedLoopingImpl<>(myElement, myAdapter, myLooping.aborted());
    }

    @NotNull
    @Override
    public MappedLooping<N, T> filterOut(@NotNull final Predicate<N> predicate) {
        return new MappedLoopingImpl<>(myElement, myAdapter, myLooping.filterOut(predicate));
    }

    @NotNull
    @Override
    public MappedLooping<N, T> filterOut(@NotNull Class clazz) {
        return new MappedLoopingImpl<>(myElement, myAdapter, myLooping.filterOut(clazz));
    }

    @NotNull
    @Override
    public <F> MappedLooping<N, T> filterOut(@NotNull Class<F> clazz, @NotNull Predicate<F> predicate) {
        return new MappedLoopingImpl<>(myElement, myAdapter, myLooping.filterOut(clazz, predicate));
    }

    @NotNull
    @Override
    public MappedLooping<N, T> filter(@NotNull final Predicate<N> predicate) {
        return new MappedLoopingImpl<>(myElement, myAdapter, myLooping.filter(predicate));
    }

    @NotNull
    @Override
    public <F> MappedLooping<N, F> filter(@NotNull Class<F> clazz) {
        return new MappedLoopingImpl<>(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(clazz)), myLooping);
    }

    @NotNull
    @Override
    public <F> MappedLooping<N, F> filter(@NotNull Class<F> clazz, @NotNull Predicate<F> predicate) {
        return new MappedLoopingImpl<>(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(clazz, predicate)), myLooping);
    }

    @NotNull
    @Override
    public <F> MappedLooping<N, F> filter(@NotNull Function<T, F> adapter) {
        return new MappedLoopingImpl<>(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(adapter)), myLooping);
    }

    @NotNull
    @Override
    public <F> MappedLooping<N, F> filter(@NotNull ValueLoopAdapter<T, F> adapter) {
        return new MappedLoopingImpl<>(myElement, myAdapter.andThen(adapter), myLooping);
    }

    @NotNull
    @Override
    public MappedLooping<N, T> filter(@NotNull ValueLoopFilter<T> filter) {
        return new MappedLoopingImpl<>(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(filter)), myLooping);
    }

    @Override
    @NotNull
    public <R> R doLoop(@NotNull final R defaultValue, @NotNull final ValueLoopConsumer<T, R> consumer) {
        return myLooping.doLoop(myElement, defaultValue, myAdapter, consumer);
    }

    @Override
    public void doLoop(@NotNull final VoidLoopConsumer<T> consumer) {
        myLooping.doLoop(myElement, myAdapter, consumer);
    }

    public static <N> MappedLooping<N, N> create(final N element, @NotNull LoopingImpl<N> looping) {
        return new MappedLoopingImpl<>(element, ValueLoopAdapterImpl.of(), looping);
    }
}
