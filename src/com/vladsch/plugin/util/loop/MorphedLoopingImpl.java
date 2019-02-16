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

import com.intellij.psi.impl.source.tree.LeafPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

public class MorphedLoopingImpl<N, T> implements MorphedLooping<N, T> {
    private final @NotNull N myElement;
    private final @NotNull Function<N, T> myAdapter;
    private final @NotNull LoopingImpl<N> myLooping;

    public MorphedLoopingImpl(@NotNull final N element, @NotNull Function<N, T> adapter, @NotNull LoopingImpl<N> looping) {
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
    public MorphedLooping<N, T> reversed() {
        return new MorphedLoopingImpl<>(myElement, myAdapter, myLooping.reversed());
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> recursive() {
        return new MorphedLoopingImpl<>(myElement, myAdapter, myLooping.recursive());
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> nonRecursive() {
        return new MorphedLoopingImpl<>(myElement, myAdapter, myLooping.nonRecursive());
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> recurse(@NotNull final Predicate<N> predicate) {
        return new MorphedLoopingImpl<>(myElement, myAdapter, myLooping.recurse(predicate));
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> filter(@NotNull final Predicate<N> predicate) {
        return new MorphedLoopingImpl<>(myElement, myAdapter, myLooping.filter(predicate));
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> filterFalse() {
        return aborted();
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> aborted() {
        return new MorphedLoopingImpl<>(myElement, myAdapter, myLooping.aborted());
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> filterOut(@NotNull final Predicate<N> predicate) {
        return new MorphedLoopingImpl<>(myElement, myAdapter, myLooping.filterOut(predicate));
    }

    @NotNull
    @Override
    public <F extends N> MorphedLooping<N, F> filter(@NotNull Function<T, F> adapter) {
        return new MorphedLoopingImpl<>(myElement, myAdapter.andThen(adapter), myLooping);
    }

    @NotNull
    @Override
    public <F extends N> MorphedLooping<N, F> filter(@NotNull Class<F> clazz) {
        return new MorphedLoopingImpl<>(myElement, myAdapter.andThen((it) -> clazz.isInstance(it) ? clazz.cast(it) : null), myLooping);
    }

    @NotNull
    @Override
    public <F extends N> MorphedLooping<N, F> filter(@NotNull Class<F> clazz, @NotNull Predicate<F> predicate) {
        MorphedLoopingImpl<N, F> morphedLooping = new MorphedLoopingImpl<>(myElement, myAdapter.andThen((it) -> clazz.isInstance(it) ? clazz.cast(it) : null), myLooping);
        return morphedLooping.filter((Predicate<N>) n -> {
            F apply = morphedLooping.myAdapter.apply(n);
            return apply != null && predicate.test(apply);
        });
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> filterOut(@NotNull Class clazz) {
        return new MorphedLoopingImpl<>(myElement, myAdapter, myLooping.filterOut(clazz::isInstance));
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

    public static <N> MorphedLooping<N, N> create(final N element, @NotNull LoopingImpl<N> looping) {
        return new MorphedLoopingImpl<>(element, Function.identity(), looping);
    }
}
