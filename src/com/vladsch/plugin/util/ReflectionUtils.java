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

import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class ReflectionUtils {
    @Nullable
    public static <T> T getField(@NotNull Class<?> instanceClass, @Nullable Object instance, @NotNull String field, @NotNull Class<T> fieldClass) {
        try {
            Field f = instanceClass.getDeclaredField(field); //NoSuchFieldException
            f.setAccessible(true);
            return fieldClass.cast(f.get(null));
        } catch (IllegalAccessException | NoSuchFieldException | ClassCastException ignored ) {
            return null;
        }
    }
}
