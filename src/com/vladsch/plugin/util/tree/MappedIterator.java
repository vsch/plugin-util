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

import java.util.function.Function;
import java.util.function.Predicate;

public class MappedIterator<B, T extends B> {
    protected final @NotNull B myElement;
    protected final @NotNull ValueIterationAdapter<? super B, T> myAdapter;
    protected final @NotNull TreeIterator<B> myTreeIterator;

    public MappedIterator(@NotNull final B element, @NotNull ValueIterationAdapter<? super B, T> adapter, @NotNull TreeIterator<B> treeIterator) {
        myElement = element;
        myAdapter = adapter;
        myTreeIterator = treeIterator;
    }

    // *******************************************************
    //
    // Looping delegated
    //
    // *******************************************************

    @NotNull
    final public TreeIterator<B> getTreeIterator() {
        return myTreeIterator;
    }

    @NotNull
    final public <R> R doLoop(@NotNull final R defaultValue, @NotNull final ValueIterationConsumer<? super T, R> consumer) {
        return myTreeIterator.doLoop(myElement, defaultValue, myAdapter, consumer);
    }

    final public void doLoop(@NotNull final VoidIterationConsumer<? super T> consumer) {
        myTreeIterator.doLoop(myElement, myAdapter, consumer);
    }

    // *******************************************************
    //
    // Need Subclass Constructors 
    //
    // *******************************************************

    @NotNull
    public MappedIterator<B, T> getModifiedCopy(final B element, final ValueIterationAdapter<? super B, T> adapter, final TreeIterator<B> treeIterator) {
        return new MappedIterator<>(element, adapter, treeIterator);
    }

    @NotNull
    public <F extends B> MappedIterator<B, F> getModifiedCopyF(final B element, final ValueIterationAdapter<? super B, F> adapter, final TreeIterator<B> treeIterator) {
        return new MappedIterator<>(element, adapter, treeIterator);
    }

    // *******************************************************
    //
    // Need Overrides with cast to sub-class
    //
    // *******************************************************

    @NotNull
    public MappedIterator<B, T> reversed() {
        return getModifiedCopy(myElement, myAdapter, myTreeIterator.reversed());
    }

    @NotNull

    public MappedIterator<B, T> recursive() {
        return getModifiedCopy(myElement, myAdapter, myTreeIterator.recursive());
    }

    @NotNull
    public MappedIterator<B, T> nonRecursive() {
        return getModifiedCopy(myElement, myAdapter, myTreeIterator.nonRecursive());
    }

    @NotNull
    public MappedIterator<B, T> recursive(boolean recursive) {
        return recursive ? recursive() : nonRecursive();
    }

    @NotNull
    public MappedIterator<B, T> nonRecursive(boolean nonRecursive) {
        return nonRecursive ? nonRecursive() : recursive();
    }

    @NotNull
    public MappedIterator<B, T> recurse(@NotNull final Predicate<? super B> predicate) {
        return getModifiedCopy(myElement, myAdapter, myTreeIterator.recurse(predicate));
    }

    @NotNull
    public MappedIterator<B, T> recurse(@NotNull final Class clazz) {
        return getModifiedCopy(myElement, myAdapter, myTreeIterator.recurse(clazz));
    }

    @NotNull
    public <F extends B> MappedIterator<B, T> recurse(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return getModifiedCopy(myElement, myAdapter, myTreeIterator.recurse(clazz, predicate));
    }

    @NotNull
    public MappedIterator<B, T> noRecurse(@NotNull final Predicate<? super B> predicate) {
        return getModifiedCopy(myElement, myAdapter, myTreeIterator.noRecurse(predicate));
    }

    @NotNull
    public MappedIterator<B, T> noRecurse(@NotNull final Class clazz) {
        return getModifiedCopy(myElement, myAdapter, myTreeIterator.noRecurse(clazz));
    }

    @NotNull
    public <F extends B> MappedIterator<B, T> noRecurse(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return getModifiedCopy(myElement, myAdapter, myTreeIterator.recurse(clazz, predicate));
    }

    @NotNull
    public MappedIterator<B, T> filterFalse() {
        return aborted();
    }

    @NotNull
    public MappedIterator<B, T> aborted() {
        return getModifiedCopy(myElement, myAdapter, myTreeIterator.aborted());
    }

    @NotNull
    public MappedIterator<B, T> filterOut(@NotNull final Predicate<? super B> predicate) {
        return getModifiedCopy(myElement, myAdapter, myTreeIterator.filterOut(predicate));
    }

    @NotNull
    public MappedIterator<B, T> filterOut(@NotNull Class clazz) {
        return getModifiedCopy(myElement, myAdapter, myTreeIterator.filterOut(clazz));
    }

    @NotNull
    public <F extends B> MappedIterator<B, T> filterOut(@NotNull Class<F> clazz, @NotNull Predicate<? super F> predicate) {
        return getModifiedCopy(myElement, myAdapter, myTreeIterator.filterOut(clazz, predicate));
    }

    @NotNull
    public MappedIterator<B, T> filter(@NotNull final Predicate<? super B> predicate) {
        return getModifiedCopy(myElement, myAdapter, myTreeIterator.filter(predicate));
    }

    @NotNull
    public MappedIterator<B, T> acceptFilter(@NotNull ValueIterationFilter<? super T> filter) {
        return getModifiedCopy(myElement, myAdapter.andThen(ValueIterationAdapterImpl.of(filter)), myTreeIterator);
    }

    // *******************************************************
    //
    // Mapping Functions
    //
    // *******************************************************

    @NotNull
    public <F extends B> MappedIterator<B, F> filter(@NotNull Class<F> clazz) {
        return getModifiedCopyF(myElement, myAdapter.andThen(ValueIterationAdapterImpl.of(clazz)), myTreeIterator);
    }

    @NotNull
    public <F extends B> MappedIterator<B, F> filter(@NotNull Class<F> clazz, @NotNull Predicate<? super F> predicate) {
        return getModifiedCopyF(myElement, myAdapter.andThen(ValueIterationAdapterImpl.of(clazz, predicate)), myTreeIterator);
    }

    @NotNull
    public <F extends B> MappedIterator<B, F> adapt(@NotNull Function<? super T, F> adapter) {
        return getModifiedCopyF(myElement, myAdapter.andThen(ValueIterationAdapterImpl.of(adapter)), myTreeIterator);
    }

    @NotNull
    public <F extends B> MappedIterator<B, F> adapt(@NotNull ValueIterationAdapter<? super T, F> adapter) {
        return getModifiedCopyF(myElement, myAdapter.andThen(adapter), myTreeIterator);
    }

    @NotNull
    public MappedIterator<Object, B> toObjectMapped(Class<B> clazz) {
        Function<Object, B> objectToB = it -> clazz.isInstance(it) ? clazz.cast(it) : null;
        Function<B, Object> tToObject = it -> it;
        FixedIterationConditions<Object> constraints = FixedIterationConditions.mapTtoB(myTreeIterator.getConstraints(), objectToB, tToObject);
        return new MappedIterator<>(myElement, new ValueIterationAdapterImpl<>(objectToB), new TreeIterator<>(constraints));
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

    public static <N> MappedIterator<N, N> create(final N element, @NotNull TreeIterator<N> treeIterator) {
        return new MappedIterator<>(element, ValueIterationAdapterImpl.of(), treeIterator);
    }
}
