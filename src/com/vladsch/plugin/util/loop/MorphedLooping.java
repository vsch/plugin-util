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

public class MorphedLooping<N, T extends N> extends TypedLooping<N, T, MorphedLooping<N, T>> {
    public MorphedLooping(@NotNull final N element, @NotNull ValueLoopAdapter<N, T> adapter, @NotNull Looping<N> looping) {
        super(element, adapter, looping);
    }

    @NotNull
    public MorphedLooping<N, T> getModifiedCopy(final N element, final ValueLoopAdapter<N, T> adapter, final Looping<N> looping) {
        return new MorphedLooping<>(element, adapter, looping);
    }

    @NotNull
    public MappedLooping<N, T> asMapped() {
        return new MappedLooping<>(myElement, myAdapter, myLooping);
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> filter(@NotNull final Predicate<N> predicate) {
        return new MorphedLooping<>(myElement, myAdapter, myLooping.filter(predicate));
    }

    @NotNull
    public <F extends N> MorphedLooping<N, F> filter(@NotNull Class<F> clazz) {
        return new MorphedLooping<>(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(clazz)), myLooping);
    }

    @NotNull
    public <F extends N> MorphedLooping<N, F> filter(@NotNull Class<F> clazz, @NotNull Predicate<F> predicate) {
        return new MorphedLooping<>(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(clazz, predicate)), myLooping);
    }

    @NotNull
    public <F extends N> MorphedLooping<N, F> filter(@NotNull Function<T, F> adapter) {
        return new MorphedLooping<>(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(adapter)), myLooping);
    }

    @NotNull
    public <F extends N> MorphedLooping<N, F> filter(@NotNull ValueLoopAdapter<T, F> adapter) {
        return new MorphedLooping<>(myElement, myAdapter.andThen(adapter), myLooping);
    }

    @NotNull
    @Override
    public MorphedLooping<N, T> filter(@NotNull ValueLoopFilter<T> filter) {
        return new MorphedLooping<>(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(filter)), myLooping);
    }

    /*
     * Static Factories
     */

    public static <N> MorphedLooping<N, N> create(final N element, @NotNull Looping<N> looping) {
        return new MorphedLooping<>(element, ValueLoopAdapterImpl.of(), looping);
    }
}
