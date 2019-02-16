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

public interface MorphedLooping<N, T> extends TypedLooping<N, T> {
    @NotNull
    @Override
    MorphedLooping<N, T> reversed();

    @NotNull
    @Override
    MorphedLooping<N, T> recursive();

    @NotNull
    @Override
    MorphedLooping<N, T> nonRecursive();

    @NotNull
    default MorphedLooping<N, T> recursive(boolean recursive) {
        return recursive ? recursive() : nonRecursive();
    }

    @NotNull
    default MorphedLooping<N, T> nonRecursive(boolean nonRecursive) {
        return nonRecursive ? nonRecursive() : recursive();
    }

    @NotNull
    @Override
    MorphedLooping<N, T> recurse(@NotNull Predicate<N> predicate);

    @NotNull
    MorphedLooping<N, T> recurse(@NotNull Class clazz);

    @NotNull
    <F> MorphedLooping<N, T> recurse(@NotNull Class<F> clazz, @NotNull Predicate<F> predicate);

    @NotNull
    @Override
    MorphedLooping<N, T> filterFalse();

    @NotNull
    @Override
    MorphedLooping<N, T> aborted();

    @NotNull
    @Override
    MorphedLooping<N, T> filterOut(@NotNull Predicate<N> predicate);

    @NotNull
    @Override
    MorphedLooping<N, T> filterOut(@NotNull Class clazz);

    @NotNull
    @Override
    <F> MorphedLooping<N, T> filterOut(@NotNull Class<F> clazz, @NotNull Predicate<F> predicate);

    @NotNull
    @Override
    MorphedLooping<N, T> filter(@NotNull Predicate<N> predicate);

    @NotNull
    <F extends N> MorphedLooping<N, F> filter(@NotNull Class<F> clazz);

    @NotNull
    <F extends N> MorphedLooping<N, F> filter(@NotNull Class<F> clazz, @NotNull Predicate<F> predicate);

    @NotNull
    <F extends N> MorphedLooping<N, F> filter(@NotNull Function<T, F> adapter);

    @NotNull
    <F extends N> MorphedLooping<N, F> filter(@NotNull ValueLoopAdapter<T, F> adapter);

    @NotNull
    MorphedLooping<N, T> filter(@NotNull ValueLoopFilter<T> filter);
}
