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

package com.vladsch.plugin.util;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Wrapped<T> {
    private final T myValue;

    private Wrapped(final T value) {
        myValue = value;
    }

    public T getValue() {
        return myValue;
    }
    
    private static HashMap<Class, Wrapped> ourWrappedNulls = new HashMap<>();
    
    public static <T> Wrapped<T> nullOf(@NotNull Class<T> clazz) {
        //noinspection unchecked
        return ourWrappedNulls.computeIfAbsent(clazz, (it) -> new Wrapped<T>(null));
    }

    public static <T> Wrapped<T> of(@NotNull T value) {
        return new Wrapped<>(value);
    }
}
