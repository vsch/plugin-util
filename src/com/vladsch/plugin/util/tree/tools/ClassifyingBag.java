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

package com.vladsch.plugin.util.tree.tools;

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ClassifyingBag<N, V> implements BiFunction<HashMap<V, Integer>, N, HashMap<V, Integer>> {
    private final Function<? super N, ? extends V> myClassifier;

    public ClassifyingBag(final Function<? super N, ? extends V> classifier) {
        myClassifier = classifier;
    }

    @Override
    public HashMap<V, Integer> apply(HashMap<V, Integer> map, final N n) {
        if (map == null) {
            map = new HashMap<>();
        }

        V key = myClassifier.apply(n);
        if (key != null) {
            int count = map.computeIfAbsent(key, v -> 0);
            map.put(key, ++count);
        }

        return map;
    }
}
