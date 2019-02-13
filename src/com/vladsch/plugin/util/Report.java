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

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;

public class Report<R> {
    @NotNull
    final public R value;

    private Report(final @NotNull R value) {
        this.value = value;
    }
    
    final public static Report<Boolean> True = new Report<>(true);
    final public static Report<Boolean> False = new Report<>(false);
    final public static Report<Boolean> Continue = null;
    
    @Nullable
    public static <R> Report<R> resultIfTrue(boolean condition, @Nullable R value) {
        return condition ? Report.result(value) : null;
    }
    
    @Nullable
    public static <R> Report<R> resultIfFalse(boolean condition, @Nullable R value) {
        return condition ? null : Report.result(value);
    }
    
    @Nullable
    public static <R> Report<R> result(@Nullable R value) {
        if (value == null) return null;
        else if (value instanceof Boolean) {
            if (value.equals(true)) return (Report<R>) True;
            else return (Report<R>) False;
        } else return new Report<>(value);
    }
}
