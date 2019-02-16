/*
 * Copyright (c) 2015-2019 Vladimir Schneider <vladimir.schneider@gmail.com>, all rights reserved.
 *
 * Looping<N>his code is private property of the copyright holder and cannot be used without
 * having obtained a license or prior written permission of the of the copyright holder.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WILooping<N>HOULooping<N> WARRANLooping<N>IES OR CONDILooping<N>IONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package com.vladsch.plugin.util.loop;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class LoopingImpl<N> implements Loop<N> {
    private final LoopConstraints<N> myConstraints;
    private final Predicate<N> myRecursion;
    protected final Predicate<N> myFilter;

    public LoopingImpl(final LoopConstraints<N> constraints, final Predicate<N> filter) {
        //noinspection unchecked
        this(constraints, filter, Loop.FALSE);
    }

    public LoopingImpl(final LoopConstraints<N> constraints) {
        //noinspection unchecked
        this(constraints, Loop.TRUE, Loop.TRUE);
    }

    public LoopingImpl(final LoopConstraints<N> constraints, final Predicate<N> filter, Predicate<N> recursion) {
        myConstraints = constraints;
        myRecursion = recursion;
        myFilter = filter;
    }

    @NotNull
    public Predicate<N> getPredicate(@NotNull final Class clazz) {
        return clazz::isInstance;
    }

    @NotNull
    public <F> Predicate<N> getPredicate(@NotNull final Class<F> clazz, @NotNull final Predicate<F> predicate) {
        return (it) -> clazz.isInstance(it) && predicate.test(clazz.cast(it));
    }

    @NotNull
    @Override
    public LoopConstraints<N> getConstraints() {
        return myConstraints;
    }

    @Override
    public Predicate<N> getRecursion() {
        return myRecursion;
    }

    @Override
    public Predicate<N> getFilter() {
        return myFilter;
    }

    @NotNull
    public LoopingImpl<N> modifiedCopy(final @NotNull LoopConstraints<N> constraints, final @NotNull Predicate<N> filter, final @NotNull Predicate<N> recursion) {
        return new LoopingImpl<>(constraints, filter, recursion);
    }

    @NotNull
    public LoopingImpl<N> reversed() {
        return modifiedCopy(myConstraints.getReversed(), myFilter, myRecursion);
    }

    @NotNull
    public LoopingImpl<N> recursive() {
        //noinspection unchecked
        return modifiedCopy(myConstraints, myFilter, (Predicate<N>) Loop.TRUE);
    }

    @NotNull
    public LoopingImpl<N> nonRecursive() {
        //noinspection unchecked
        return modifiedCopy(myConstraints, myFilter, (Predicate<N>) Loop.FALSE);
    }

    @NotNull
    public LoopingImpl<N> recurse(final @NotNull Predicate<N> predicate) {
        return modifiedCopy(myConstraints, myFilter, myRecursion.or(predicate));
    }

    @NotNull
    public LoopingImpl<N> recurse(final @NotNull Class clazz) {
        return recurse(getPredicate(clazz));
    }

    @NotNull
    public <F> LoopingImpl<N> recurse(final @NotNull Class<F> clazz, @NotNull Predicate<F> predicate) {
        return recurse(getPredicate(clazz, predicate));
    }

    @NotNull
    public LoopingImpl<N> noRecurse(final @NotNull Predicate<N> predicate) {
        return modifiedCopy(myConstraints, myFilter, myRecursion.and(predicate.negate()));
    }

    @NotNull
    public LoopingImpl<N> noRecurse(final @NotNull Class clazz) {
        return noRecurse(getPredicate(clazz));
    }

    @NotNull
    public <F> LoopingImpl<N> noRecurse(final @NotNull Class<F> clazz, @NotNull Predicate<F> predicate) {
        return noRecurse(getPredicate(clazz, predicate));
    }

    @NotNull
    public LoopingImpl<N> aborted() {
        return modifiedCopy(myConstraints.getAborted(), myFilter, myRecursion);
    }

    @NotNull
    public LoopingImpl<N> filterOut(final @NotNull Predicate<N> predicate) {
        return modifiedCopy(myConstraints, myFilter.and(predicate.negate()), myRecursion);
    }

    @NotNull
    public LoopingImpl<N> filterOut(final @NotNull Class clazz) {
        return filterOut(getPredicate(clazz));
    }

    @NotNull
    public <F> LoopingImpl<N> filterOut(final @NotNull Class<F> clazz, @NotNull Predicate<F> predicate) {
        return filterOut(getPredicate(clazz, predicate));
    }

    @NotNull
    public LoopingImpl<N> filter(final @NotNull Predicate<N> predicate) {
        return modifiedCopy(myConstraints, myFilter.and(predicate), myRecursion);
    }

    @NotNull
    public LoopingImpl<N> filter(final @NotNull Class clazz) {
        return filter(getPredicate(clazz));
    }

    @NotNull
    public <F> LoopingImpl<N> filter(final @NotNull Class<F> clazz, @NotNull Predicate<F> predicate) {
        return filter(getPredicate(clazz, predicate));
    }

    @NotNull
    public <R> MorphedLoopingImpl<N, N> over(final @NotNull N element) {
        return new MorphedLoopingImpl<>(element, ValueLoopAdapterImpl.of(), this);
    }

    @NotNull
    public static <N> LoopingImpl<N> of(final @NotNull LoopConstraints<N> constraints) {
        return new LoopingImpl<N>(constraints);
    }

    @NotNull
    public static <N> LoopingImpl<N> of(final @NotNull LoopConstraints<N> constraints, final @NotNull Predicate<N> filter) {
        return new LoopingImpl<N>(constraints, filter);
    }

    @NotNull
    public static <N> LoopingImpl<N> of(final @NotNull LoopConstraints<N> constraints, final @NotNull Predicate<N> filter, final @NotNull Predicate<N> recursion) {
        return new LoopingImpl<N>(constraints, filter, recursion);
    }

    @NotNull
    public static <N> Predicate<N> TRUE() {
        return n -> true;
    }

    @NotNull
    public static <N> Predicate<N> FALSE() {
        return n -> true;
    }
}
