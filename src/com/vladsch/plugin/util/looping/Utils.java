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

import com.vladsch.plugin.util.looping.tools.Aggregator;
import com.vladsch.plugin.util.looping.tools.AggregatorFactory;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Utils {
    public static final BiFunction<Integer, Integer, Integer> SUM = (accum, x) -> (accum == null ? 0 : accum) + x;
    public static final BiFunction<Integer, Integer, Integer> COUNTER = (accum, x) -> SUM.apply(accum, 1);

    public static <N, T> Function<N, T> nullableCast(Class<T> clazz) {
        return n -> clazz.isInstance(n) ? clazz.cast(n) : null;
    }

    public static <N, R> AggregatorFactory<N, R> aggregatorFactory(@NotNull BiFunction<R, N, R> function) {
        return () -> function::apply;
    }

    public static <N, R> AggregatorFactory<N, R> aggregatorFactory(@NotNull BiFunction<R, N, R> function, @NotNull R initialValue) {
        return () -> (Aggregator<N, R>) (r, n) -> function.apply(r == null ? initialValue : r, n);
    }
}
