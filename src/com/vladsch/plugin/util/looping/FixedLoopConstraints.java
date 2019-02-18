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

public class FixedLoopConstraints<N> implements LoopConstraints<N> {
    final private @NotNull Function<? super N, N> initializer;
    final private @NotNull Function<? super N, N> iterator;
    final private @NotNull Function<? super N, N> reverseInitializer;
    final private @NotNull Function<? super N, N> reverseIterator;

    public FixedLoopConstraints(@NotNull final Function<? super N, N> initializer, @NotNull final Function<? super N, N> iterator, @NotNull final Function<? super N, N> reverseInitializer, @NotNull final Function<? super N, N> reverseIterator) {
        this.initializer = initializer;
        this.iterator = iterator;
        this.reverseInitializer = reverseInitializer;
        this.reverseIterator = reverseIterator;
    }

    @Override
    @NotNull
    public Function<? super N, N> getInitializer() {
        return initializer;
    }

    @Override
    @NotNull
    public Function<? super N, N> getIterator() {
        return iterator;
    }

    @NotNull
    @Override
    public LoopConstraints<N> getReversed() {
        return new FixedLoopConstraints<>(reverseInitializer, reverseIterator, initializer, iterator);
    }

    public static <B, T> Function<? super B, B> getAdapter(Function<? super T, T> function, Function<? super B, T> adaptBtoT, Function<? super T, B> adaptTtoB) {
        return adaptBtoT.andThen(function).andThen(adaptTtoB);
    }

    public static <B, T> FixedLoopConstraints<B> mapTtoB(LoopConstraints<T> constraints, Function<? super B, T> adaptBtoT, Function<? super T, B> adaptTtoB) {
        return new FixedLoopConstraints<>(
                getAdapter(constraints.getInitializer(), adaptBtoT, adaptTtoB),
                getAdapter(constraints.getIterator(), adaptBtoT, adaptTtoB),
                getAdapter(constraints.getReversed().getInitializer(), adaptBtoT, adaptTtoB),
                getAdapter(constraints.getReversed().getIterator(), adaptBtoT, adaptTtoB)
        );
    }
}
