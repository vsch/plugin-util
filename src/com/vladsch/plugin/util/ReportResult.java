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

public class ReportResult<R> extends BreakResult {
    @NotNull private R myValue;        // return value

    public ReportResult(final @NotNull R initialValue) {
        myValue = initialValue;
    }

    public void Return() {
        Break();
    }

    public void Return(@NotNull R value) {
        myValue = value;
        Break();
    }

    @NotNull
    public R getValue() {
        return myValue;
    }
}
