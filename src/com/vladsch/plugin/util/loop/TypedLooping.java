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

public interface TypedLooping<N, T> {
    // @formatter:off
    @NotNull LoopingImpl<N> getLooping();
    @NotNull TypedLooping<N, T> reversed();
    @NotNull TypedLooping<N, T> recursive();
    @NotNull TypedLooping<N, T> nonRecursive();
    @NotNull TypedLooping<N, T> recurse(@NotNull Predicate<N> predicate);
    @NotNull TypedLooping<N, T> filter(@NotNull Predicate<N> predicate);
    @NotNull TypedLooping<N, T> filterFalse();
    @NotNull TypedLooping<N, T> aborted();
    @NotNull TypedLooping<N, T> filterOut(@NotNull Predicate<N> predicate);
    @NotNull TypedLooping<N, T> filterOut(@NotNull Class clazz);
    @NotNull<R> R doLoop(@NotNull R defaultValue, @NotNull ValueLoopConsumer<T, R> consumer);
    // @formatter:on
    
    void doLoop(@NotNull VoidLoopConsumer<T> consumer);
}
