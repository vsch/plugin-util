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

import java.util.function.Predicate;

public interface TypedLooping<N, T, D extends TypedLooping<N,T,D>> {
    @NotNull
    LoopingImpl<N> getLooping();

    @NotNull
    D reversed();

    @NotNull
    D recursive();

    @NotNull
    D nonRecursive();

    @NotNull
    default D recursive(boolean recursive) {
        return recursive ? recursive() : nonRecursive();
    }

    @NotNull
    default D nonRecursive(boolean nonRecursive) {
        return nonRecursive ? nonRecursive() : recursive();
    }

    @NotNull
    D recurse(@NotNull Predicate<N> predicate);

    @NotNull
    D recurse(@NotNull Class clazz);

    @NotNull
    <F> D recurse(@NotNull Class<F> clazz, @NotNull Predicate<F> predicate);

    @NotNull
    D filterFalse();

    @NotNull
    D aborted();

    @NotNull
    D filterOut(@NotNull Predicate<N> predicate);

    @NotNull
    D filterOut(@NotNull Class clazz);

    @NotNull
    <F> D filterOut(@NotNull Class<F> clazz, @NotNull Predicate<F> predicate);

    @NotNull
    D filter(@NotNull Predicate<N> predicate);

    @NotNull
    <R> R doLoop(@NotNull R defaultValue, @NotNull ValueLoopConsumer<T, R> consumer);

    void doLoop(@NotNull VoidLoopConsumer<T> consumer);
}
