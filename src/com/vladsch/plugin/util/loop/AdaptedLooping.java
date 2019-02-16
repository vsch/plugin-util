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

public class AdaptedLooping<N, T extends N> {
    private final @NotNull N myElement;
    private final @NotNull Function<N, T> myAdapter;
    private final @NotNull Looping<N> myLooping;


    public AdaptedLooping(@NotNull final N element, @NotNull Function<N, T> adapter, @NotNull Looping<N> looping) {
        myElement = element;
        myAdapter = adapter;
        myLooping = looping;
    }

    @NotNull
    public Looping<N> getLooping() {
        return myLooping;
    }

    public <F extends N> AdaptedLooping<N, F> adapted(Function<N, F> adapter) {
        return new AdaptedLooping<>(myElement, adapter, myLooping);
    }

    public AdaptedLooping<N, T> reversed() {
        return new AdaptedLooping<>(myElement, myAdapter, myLooping.reversed());
    }

    public AdaptedLooping<N, T> recursive() {
        return new AdaptedLooping<>(myElement, myAdapter, myLooping.recursive());
    }

    public AdaptedLooping<N, T> nonRecursive() {
        return new AdaptedLooping<>(myElement, myAdapter, myLooping.nonRecursive());
    }

    public AdaptedLooping<N, T> recurse(final Predicate<N> predicate) {
        return new AdaptedLooping<>(myElement, myAdapter, myLooping.recurse(predicate));
    }

    public AdaptedLooping<N, T> filter(final Predicate<N> predicate) {
        return new AdaptedLooping<>(myElement, myAdapter, myLooping.filter(predicate));
    }

    public AdaptedLooping<N, T> filterFalse() {
        return aborted();
    }

    public AdaptedLooping<N, T> aborted() {
        return new AdaptedLooping<>(myElement, myAdapter, myLooping.aborted());
    }

    public AdaptedLooping<N, T> filterOut(final Predicate<N> predicate) {
        return new AdaptedLooping<>(myElement, myAdapter, myLooping.filterOut(predicate));
    }

    public <F extends N> AdaptedLooping<N, F> filter(Function<N, F> adapter) {
        return new AdaptedLooping<>(myElement, myAdapter.andThen(adapter), myLooping.filter((it) -> adapter.apply(it) != null));
    }

    public <F extends N> AdaptedLooping<N, F> filter(Class<F> clazz) {
        return new AdaptedLooping<>(myElement, (it) -> clazz.isInstance(it) ? clazz.cast(it) : null, myLooping.filter(clazz::isInstance));
    }

    public <F extends N> AdaptedLooping<N, F> filter(Class<F> clazz, Predicate<F> predicate) {
        return new AdaptedLooping<>(myElement, myAdapter.andThen( (it) -> clazz.isInstance(it) ? clazz.cast(it) : null), myLooping.filter((it)->clazz.isInstance(it) && predicate.test(clazz.cast(it))));
    }

    public AdaptedLooping<N, T> filterOut(Class clazz) {
        return new AdaptedLooping<>(myElement, myAdapter, myLooping.filterOut(clazz::isInstance));
    }
    
    public AdaptedLooping<N, T> getNoLeafPsi() {
        return new AdaptedLooping<>(myElement, myAdapter, myLooping.filterOut(LeafPsiElement.class));
    }
    
    @NotNull
    public <R> R doLoop(@NotNull final R defaultValue, @NotNull final ValueLoopConsumer<T, R> consumer) {
        return myLooping.doLoop(myElement, defaultValue, myAdapter, consumer);
    }

    public void doLoop(@NotNull final VoidLoopConsumer<T> consumer) {
        myLooping.doLoop(myElement, myAdapter, consumer);
    }

    public static <N> FixedLooping<N> create(final N element, @NotNull Looping<N> looping) {
        return new FixedLooping<>(element, Function.identity(), looping);
    }
}
