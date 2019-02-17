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

import java.util.function.Function;
import java.util.function.Predicate;

public class MappedLooping<B, N extends B, T extends B> {
    protected final @NotNull N myElement;
    protected final @NotNull ValueLoopAdapter<? super N, T> myAdapter;
    protected final @NotNull Looping<N> myLooping;

    public MappedLooping(@NotNull final N element, @NotNull ValueLoopAdapter<? super N, T> adapter, @NotNull Looping<N> looping) {
        myElement = element;
        myAdapter = adapter;
        myLooping = looping;
    }

    // *******************************************************
    //
    // Looping delegated
    //
    // *******************************************************

    @NotNull
    final public Looping<N> getLooping() {
        return myLooping;
    }

    @NotNull
    final public <R> R doLoop(@NotNull final R defaultValue, @NotNull final ValueLoopConsumer<? super T, R> consumer) {
        return myLooping.doLoop(myElement, defaultValue, myAdapter, consumer);
    }

    final public void doLoop(@NotNull final VoidLoopConsumer<? super T> consumer) {
        myLooping.doLoop(myElement, myAdapter, consumer);
    }

    // *******************************************************
    //
    // Need Subclass Constructors 
    //
    // *******************************************************

    @NotNull
    public MappedLooping<B, N, T> getModifiedCopy(final N element, final ValueLoopAdapter<? super N, T> adapter, final Looping<N> looping) {
        return new MappedLooping<>(element, adapter, looping);
    }

    @NotNull
    public <F extends B> MappedLooping<B, N, F> getModifiedCopyF(final N element, final ValueLoopAdapter<? super N, F> adapter, final Looping<N> looping) {
        return new MappedLooping<>(element, adapter, looping);
    }

    // *******************************************************
    //
    // Need Overrides with cast to sub-class
    //
    // *******************************************************

    @NotNull
    public MappedLooping<B, N, T> reversed() {
        return getModifiedCopy(myElement, myAdapter, myLooping.reversed());
    }

    @NotNull

    public MappedLooping<B, N, T> recursive() {
        return getModifiedCopy(myElement, myAdapter, myLooping.recursive());
    }

    @NotNull
    public MappedLooping<B, N, T> nonRecursive() {
        return getModifiedCopy(myElement, myAdapter, myLooping.nonRecursive());
    }

    @NotNull
    public MappedLooping<B, N, T> recursive(boolean recursive) {
        return recursive ? recursive() : nonRecursive();
    }

    @NotNull
    public MappedLooping<B, N, T> nonRecursive(boolean nonRecursive) {
        return nonRecursive ? nonRecursive() : recursive();
    }

    @NotNull
    public MappedLooping<B, N, T> recurse(@NotNull final Predicate<? super N> predicate) {
        return getModifiedCopy(myElement, myAdapter, myLooping.recurse(predicate));
    }

    @NotNull
    public MappedLooping<B, N, T> recurse(@NotNull final Class clazz) {
        return getModifiedCopy(myElement, myAdapter, myLooping.recurse(clazz));
    }

    @NotNull
    public <F extends B> MappedLooping<B, N, T> recurse(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return getModifiedCopy(myElement, myAdapter, myLooping.recurse(clazz, predicate));
    }

    @NotNull
    public MappedLooping<B, N, T> noRecurse(@NotNull final Predicate<? super N> predicate) {
        return getModifiedCopy(myElement, myAdapter, myLooping.noRecurse(predicate));
    }

    @NotNull
    public MappedLooping<B, N, T> noRecurse(@NotNull final Class clazz) {
        return getModifiedCopy(myElement, myAdapter, myLooping.noRecurse(clazz));
    }

    @NotNull
    public <F extends B> MappedLooping<B, N, T> noRecurse(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return getModifiedCopy(myElement, myAdapter, myLooping.recurse(clazz, predicate));
    }

    @NotNull
    public MappedLooping<B, N, T> filterFalse() {
        return aborted();
    }

    @NotNull
    public MappedLooping<B, N, T> aborted() {
        return getModifiedCopy(myElement, myAdapter, myLooping.aborted());
    }

    @NotNull
    public MappedLooping<B, N, T> filterOut(@NotNull final Predicate<? super N> predicate) {
        return getModifiedCopy(myElement, myAdapter, myLooping.filterOut(predicate));
    }

    @NotNull
    public MappedLooping<B, N, T> filterOut(@NotNull Class clazz) {
        return getModifiedCopy(myElement, myAdapter, myLooping.filterOut(clazz));
    }

    @NotNull
    public <F extends B> MappedLooping<B, N, T> filterOut(@NotNull Class<F> clazz, @NotNull Predicate<? super F> predicate) {
        return getModifiedCopy(myElement, myAdapter, myLooping.filterOut(clazz, predicate));
    }

    @NotNull
    public MappedLooping<B, N, T> filter(@NotNull final Predicate<? super N> predicate) {
        return getModifiedCopy(myElement, myAdapter, myLooping.filter(predicate));
    }

    @NotNull
    public MappedLooping<B, N, T> preAccept(@NotNull ValueLoopFilter<? super T> filter) {
        return getModifiedCopy(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(filter)), myLooping);
    }

    // *******************************************************
    //
    // Mapping Functions
    //
    // *******************************************************

    @NotNull
    public <F extends B> MappedLooping<B, N, F> filter(@NotNull Class<F> clazz) {
        return getModifiedCopyF(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(clazz)), myLooping);
    }

    @NotNull
    public <F extends B> MappedLooping<B, N, F> filter(@NotNull Class<F> clazz, @NotNull Predicate<? super F> predicate) {
        return getModifiedCopyF(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(clazz, predicate)), myLooping);
    }

    @NotNull
    public <F extends B> MappedLooping<B, N, F> map(@NotNull Function<? super T, F> adapter) {
        return getModifiedCopyF(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(adapter)), myLooping);
    }

    @NotNull
    public <F extends B> MappedLooping<B, N, F> map(@NotNull ValueLoopAdapter<? super T, F> adapter) {
        return getModifiedCopyF(myElement, myAdapter.andThen(adapter), myLooping);
    }

    // *******************************************************
    //
    // Subclass specific
    //
    // *******************************************************

    // *******************************************************
    //
    // Static Factories
    //
    // *******************************************************

    public static <N> MappedLooping<N, N, N> create(final N element, @NotNull Looping<N> looping) {
        return new MappedLooping<>(element, ValueLoopAdapterImpl.of(), looping);
    }
}
