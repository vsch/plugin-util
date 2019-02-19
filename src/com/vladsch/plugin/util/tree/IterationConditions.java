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

package com.vladsch.plugin.util.tree;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface IterationConditions<N> {
    @NotNull
    Function<? super N, N> getInitializer();

    @NotNull
    Function<? super N, N> getIterator();

    @NotNull
    default IterationConditions<N> getReversed() {
        throw new IllegalStateException("Method not implemented");
    }

    @NotNull
    default IterationConditions<N> getAborted() {
        Function<? super N, N> function = n -> null;
        return new FixedIterationConditions<>(function, function, function, function);
    }
}
