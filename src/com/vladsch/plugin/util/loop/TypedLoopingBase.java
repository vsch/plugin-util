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

import java.util.function.Predicate;

public abstract class TypedLoopingBase<N, T, D extends TypedLoopingBase<N, T, D>> {
    protected final @NotNull N myElement;
    protected final @NotNull ValueLoopAdapter<N, T> myAdapter;
    protected final @NotNull LoopingImpl<N> myLooping;

    public TypedLoopingBase(@NotNull final N element, @NotNull ValueLoopAdapter<N, T> adapter, @NotNull LoopingImpl<N> looping) {
        myElement = element;
        myAdapter = adapter;
        myLooping = looping;
    }

    @NotNull
    public LoopingImpl<N> getLooping() {
        return myLooping;
    }

    @NotNull
    public abstract D getModifiedCopy(final N element, final ValueLoopAdapter<N, T> adapter, final LoopingImpl<N> looping);

    @NotNull

    public D reversed() {
        return getModifiedCopy(myElement, myAdapter, myLooping.reversed());
    }

    @NotNull

    public D recursive() {
        return getModifiedCopy(myElement, myAdapter, myLooping.recursive());
    }

    @NotNull

    public D nonRecursive() {
        return getModifiedCopy(myElement, myAdapter, myLooping.nonRecursive());
    }

    @NotNull
    public D recursive(boolean recursive) {
        return recursive ? recursive() : nonRecursive();
    }

    @NotNull
    public D nonRecursive(boolean nonRecursive) {
        return nonRecursive ? nonRecursive() : recursive();
    }

    @NotNull

    public D recurse(@NotNull final Predicate<N> predicate) {
        return getModifiedCopy(myElement, myAdapter, myLooping.recurse(predicate));
    }

    @NotNull

    public D recurse(@NotNull final Class clazz) {
        return getModifiedCopy(myElement, myAdapter, myLooping.recurse(clazz));
    }

    @NotNull

    public <F> D recurse(@NotNull final Class<F> clazz, @NotNull final Predicate<F> predicate) {
        return getModifiedCopy(myElement, myAdapter, myLooping.recurse(clazz, predicate));
    }

    @NotNull

    public D noRecurse(@NotNull final Predicate<N> predicate) {
        return getModifiedCopy(myElement, myAdapter, myLooping.noRecurse(predicate));
    }

    @NotNull

    public D noRecurse(@NotNull final Class clazz) {
        return getModifiedCopy(myElement, myAdapter, myLooping.noRecurse(clazz));
    }

    @NotNull

    public <F> D noRecurse(@NotNull final Class<F> clazz, @NotNull final Predicate<F> predicate) {
        return getModifiedCopy(myElement, myAdapter, myLooping.recurse(clazz, predicate));
    }

    @NotNull

    public D filterFalse() {
        return aborted();
    }

    @NotNull

    public D aborted() {
        return getModifiedCopy(myElement, myAdapter, myLooping.aborted());
    }

    @NotNull

    public D filterOut(@NotNull final Predicate<N> predicate) {
        return getModifiedCopy(myElement, myAdapter, myLooping.filterOut(predicate));
    }

    @NotNull

    public D filterOut(@NotNull Class clazz) {
        return getModifiedCopy(myElement, myAdapter, myLooping.filterOut(clazz));
    }

    @NotNull

    public <F> D filterOut(@NotNull Class<F> clazz, @NotNull Predicate<F> predicate) {
        return getModifiedCopy(myElement, myAdapter, myLooping.filterOut(clazz, predicate));
    }

    @NotNull

    public D filter(@NotNull final Predicate<N> predicate) {
        return getModifiedCopy(myElement, myAdapter, myLooping.filter(predicate));
    }

    @NotNull

    public D filter(@NotNull ValueLoopFilter<T> filter) {
        return getModifiedCopy(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(filter)), myLooping);
    }

    @NotNull
    public <R> R doLoop(@NotNull final R defaultValue, @NotNull final ValueLoopConsumer<T, R> consumer) {
        return myLooping.doLoop(myElement, defaultValue, myAdapter, consumer);
    }

    public void doLoop(@NotNull final VoidLoopConsumer<T> consumer) {
        myLooping.doLoop(myElement, myAdapter, consumer);
    }
}
