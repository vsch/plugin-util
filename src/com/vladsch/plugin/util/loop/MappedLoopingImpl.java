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

public class MappedLoopingImpl<N, T> extends TypedLoopingBase<N, T, MappedLoopingImpl<N, T>> {
    public MappedLoopingImpl(@NotNull final N element, @NotNull ValueLoopAdapter<N, T> adapter, @NotNull LoopingImpl<N> looping) {
        super(element, adapter, looping);
    }

    @NotNull
    public MappedLoopingImpl<N, T> getModifiedCopy(final N element, final ValueLoopAdapter<N, T> adapter, final LoopingImpl<N> looping) {
        return new MappedLoopingImpl<>(element, adapter, looping);
    }

    @NotNull
    public <F> MappedLoopingImpl<N, F> filter(@NotNull Class<F> clazz) {
        return new MappedLoopingImpl<>(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(clazz)), myLooping);
    }

    @NotNull
    public <F> MappedLoopingImpl<N, F> filter(@NotNull Class<F> clazz, @NotNull Predicate<F> predicate) {
        return new MappedLoopingImpl<>(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(clazz, predicate)), myLooping);
    }

    @NotNull
    public <F> MappedLoopingImpl<N, F> filter(@NotNull Function<T, F> adapter) {
        return new MappedLoopingImpl<>(myElement, myAdapter.andThen(ValueLoopAdapterImpl.of(adapter)), myLooping);
    }

    @NotNull
    public <F> MappedLoopingImpl<N, F> filter(@NotNull ValueLoopAdapter<T, F> adapter) {
        return new MappedLoopingImpl<>(myElement, myAdapter.andThen(adapter), myLooping);
    }

    public static <N> MappedLoopingImpl<N, N> create(final N element, @NotNull LoopingImpl<N> looping) {
        return new MappedLoopingImpl<>(element, ValueLoopAdapterImpl.of(), looping);
    }
}
