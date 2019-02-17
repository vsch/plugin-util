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

class FixedLoopConstraints<N> implements LoopConstraints<N> {
    final private @NotNull Function<N, N> initializer;
    final private @NotNull Function<N, N> iterator;
    final private @NotNull Function<N, N> reverseInitializer;
    final private @NotNull Function<N, N> reverseIterator;

    public FixedLoopConstraints(@NotNull final Function<N, N> initializer, @NotNull final Function<N, N> iterator, @NotNull final Function<N, N> reverseInitializer, @NotNull final Function<N, N> reverseIterator) {
        this.initializer = initializer;
        this.iterator = iterator;
        this.reverseInitializer = reverseInitializer;
        this.reverseIterator = reverseIterator;
    }

    @Override
    @NotNull
    public Function<N, N> getInitializer() {
        return initializer;
    }

    @Override
    @NotNull
    public Function<N, N> getIterator() {
        return iterator;
    }

    @NotNull
    @Override
    public LoopConstraints<N> getReversed() {
        return new FixedLoopConstraints<>(reverseInitializer, reverseIterator, initializer, iterator);
    }
}
