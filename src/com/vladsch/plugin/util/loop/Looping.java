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

import java.util.function.Function;
import java.util.function.Predicate;

public class Looping<N> implements Loop<N> {
    private final LoopConstraints<N> myConstraints;
    private final Predicate<N> myRecursion;
    protected final Predicate<N> myFilter;

    public Looping(final LoopConstraints<N> constraints, final Predicate<N> filter) {
        //noinspection unchecked
        this(constraints, filter, Loop.FALSE);
    }

    public Looping(final LoopConstraints<N> constraints) {
        //noinspection unchecked
        this(constraints, Loop.TRUE, Loop.TRUE);
    }

    public Looping(final LoopConstraints<N> constraints, final Predicate<N> filter, Predicate<N> recursion) {
        myConstraints = constraints;
        myRecursion = recursion;
        myFilter = filter;
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
    
    public Looping<N> modifiedCopy(final LoopConstraints<N> constraints, final Predicate<N> filter, Predicate<N> recursion) {
        return new Looping<>(constraints, recursion, filter);
    }

    public Looping<N> aborted() {
        return modifiedCopy(myConstraints.getAborted(), myFilter, myRecursion);
    }

    public Looping<N> reversed() {
        return modifiedCopy(myConstraints.getReversed(), myFilter, myRecursion);
    }

    public Looping<N> recursive() {
        //noinspection unchecked
        return modifiedCopy(myConstraints, myFilter, (Predicate<N>) Loop.TRUE);
    }

    public Looping<N> nonRecursive() {
        //noinspection unchecked
        return modifiedCopy(myConstraints, myFilter, (Predicate<N>) Loop.FALSE);
    }

    public Looping<N> filter(Predicate<N> predicate) {
        return modifiedCopy(myConstraints, myFilter.and(predicate), myRecursion);
    }

    public Looping<N> filterOut(Predicate<N> predicate) {
        return modifiedCopy(myConstraints, myFilter.and(predicate.negate()), myRecursion);
    }

    public Looping<N> filter(Class clazz) {
        return modifiedCopy(myConstraints, myFilter.and(clazz::isInstance), myRecursion);
    }

    public Looping<N> filterOut(Class clazz) {
        return filterOut(clazz::isInstance);
    }

    public Looping<N> recurse(Predicate<N> predicate) {
        return modifiedCopy(myConstraints, myFilter, predicate);
    }

    public FixedLooping<N> over(N element) {
        return new FixedLooping<>(element, Function.identity(), this);
    }

    public static <N> Looping<N> of(LoopConstraints<N> constraints) {
        return new Looping<N>(constraints);
    }

    public static <N> Looping<N> of(LoopConstraints<N> constraints, Predicate<N> filter) {
        return new Looping<N>(constraints, filter);
    }

    public static <N> Looping<N> of(LoopConstraints<N> constraints, Predicate<N> filter, Predicate<N> recursion) {
        return new Looping<N>(constraints, filter, recursion);
    }
    
    public static <N> Predicate<N> TRUE() {
        return n -> true;
    }
    
    public static <N> Predicate<N> FALSE() {
        return n -> true;
    }
}
