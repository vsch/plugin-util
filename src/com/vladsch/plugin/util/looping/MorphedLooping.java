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

public class MorphedLooping<N, T extends N> extends MappedLooping<N, N, T> {
    public MorphedLooping(@NotNull final N element, @NotNull ValueLoopAdapter<? super N, T> adapter, @NotNull Looping<N> looping) {
        super(element, adapter, looping);
    }

    // *******************************************************
    //
    // Need Subclass Constructors 
    //
    // *******************************************************

    @NotNull
    public MorphedLooping<N, T> getModifiedCopy(final N element, final ValueLoopAdapter<? super N, T> adapter, final Looping<N> looping) {
        return new MorphedLooping<>(element, adapter, looping);
    }

    @NotNull
    public <F extends N> MorphedLooping<N, F> getModifiedCopyF(final N element, final ValueLoopAdapter<? super N, F> adapter, final Looping<N> looping) {
        return new MorphedLooping<>(element, adapter, looping);
    }

    // *******************************************************
    //
    // Need Overrides with cast to sub-class
    //
    // *******************************************************

    @NotNull
    @Override
    public MorphedLooping<N, T> reversed() {
        return (MorphedLooping<N, T>) super.reversed();
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> recursive() {
        return (MorphedLooping<N, T>) super.recursive();
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> nonRecursive() {
        return (MorphedLooping<N, T>) super.nonRecursive();
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> recursive(final boolean recursive) {
        return (MorphedLooping<N, T>) super.recursive(recursive);
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> nonRecursive(final boolean nonRecursive) {
        return (MorphedLooping<N, T>) super.nonRecursive(nonRecursive);
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> recurse(@NotNull final Predicate<? super N> predicate) {
        return (MorphedLooping<N, T>) super.recurse(predicate);
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> recurse(@NotNull final Class clazz) {
        return (MorphedLooping<N, T>) super.recurse(clazz);
    }

    @NotNull
    @Override
    public <F extends N> MorphedLooping<N, T> recurse(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (MorphedLooping<N, T>) super.recurse(clazz, predicate);
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> noRecurse(@NotNull final Predicate<? super N> predicate) {
        return (MorphedLooping<N, T>) super.noRecurse(predicate);
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> noRecurse(@NotNull final Class clazz) {
        return (MorphedLooping<N, T>) super.noRecurse(clazz);
    }

    @NotNull
    @Override
    public <F extends N> MorphedLooping<N, T> noRecurse(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (MorphedLooping<N, T>) super.noRecurse(clazz, predicate);
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> filterFalse() {
        return (MorphedLooping<N, T>) super.filterFalse();
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> aborted() {
        return (MorphedLooping<N, T>) super.aborted();
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> filterOut(@NotNull final Predicate<? super N> predicate) {
        return (MorphedLooping<N, T>) super.filterOut(predicate);
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> filterOut(@NotNull final Class clazz) {
        return (MorphedLooping<N, T>) super.filterOut(clazz);
    }

    @NotNull
    @Override
    public <F extends N> MorphedLooping<N, T> filterOut(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (MorphedLooping<N, T>) super.filterOut(clazz, predicate);
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> filter(@NotNull final Predicate<? super N> predicate) {
        return (MorphedLooping<N, T>) super.filter(predicate);
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> preAccept(@NotNull final ValueLoopFilter<? super T> filter) {
        return (MorphedLooping<N, T>) super.preAccept(filter);
    }

    // *******************************************************
    //
    // Mapping Functions
    //
    // *******************************************************

    @NotNull
    @Override
    public <F extends N> MorphedLooping<N, F> filter(@NotNull final Class<F> clazz) {
        return (MorphedLooping<N, F>) super.filter(clazz);
    }

    @NotNull
    @Override
    public <F extends N> MorphedLooping<N, F> filter(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (MorphedLooping<N, F>) super.filter(clazz, predicate);
    }

    @NotNull
    @Override
    public <F extends N> MorphedLooping<N, F> map(@NotNull final Function<? super T, F> adapter) {
        return (MorphedLooping<N, F>) super.map(adapter);
    }

    @NotNull
    @Override
    public <F extends N> MorphedLooping<N, F> map(@NotNull final ValueLoopAdapter<? super T, F> adapter) {
        return (MorphedLooping<N, F>) super.map(adapter);
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

    public static <N> MorphedLooping<N, N> create(final N element, @NotNull Looping<N> looping) {
        return new MorphedLooping<>(element, ValueLoopAdapterImpl.of(), looping);
    }
}
