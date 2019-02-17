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

public class MappedLooping<N, T> extends TypedLooping<N, T, MappedLooping<N, T>> {
    public MappedLooping(@NotNull final N element, @NotNull ValueLoopAdapter<? super N, T> adapter, @NotNull Looping<N> looping) {
        super(element, adapter, looping);
    }

    @NotNull
    public MappedLooping<N, T> getModifiedCopy(final N element, final ValueLoopAdapter<? super N, T> adapter, final Looping<N> looping) {
        return new MappedLooping<>(element, adapter, looping);
    }

    @NotNull
    public <F> MappedLooping<N, F> filter(@NotNull Class<F> clazz) {
        return new MappedLooping<>(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(clazz)), myLooping);
    }

    @NotNull
    public <F> MappedLooping<N, F> filter(@NotNull Class<F> clazz, @NotNull Predicate<? super F> predicate) {
        return new MappedLooping<>(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(clazz, predicate)), myLooping);
    }

    @NotNull
    public <F> MappedLooping<N, F> map(@NotNull Function<? super T, F> adapter) {
        return new MappedLooping<>(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(adapter)), myLooping);
    }

    @NotNull
    public <F> MappedLooping<N, F> map(@NotNull ValueLoopAdapter<? super T, F> adapter) {
        return new MappedLooping<>(myElement, myAdapter.andThen(adapter), myLooping);
    }

    public static <N> MappedLooping<N, N> create(final N element, @NotNull Looping<N> looping) {
        return new MappedLooping<>(element, ValueLoopAdapterImpl.of(), looping);
    }
}
