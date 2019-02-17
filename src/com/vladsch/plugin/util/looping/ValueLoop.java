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

public interface ValueLoop<R> extends VoidLoop {
    /**
     * Set the result which will be returned by the loop if terminated without {@link #Return(Object)} call.
     *
     * @param value value to return from the loop
     */
    void setResult(@NotNull R value);

    /**
     * @return true if the current loop result value was never set (ie. only set at loop instantiation with
     * defaultValue)
     * <p>
     * NOTE: does not test current value with equality to default value, set by {@link #setResult(Object)} or {@link
     * #Return(Object)}
     */
    boolean isDefaultResult();       // true if value never set other than initial default setting

    /**
     * @return defaultValue passed to loop instance
     */
    @NotNull
    R getDefaultValue();

    /**
     * @return  current result value
     */
    @NotNull
    R getResult();

    /**
     * Set result value and terminate all recursions
     * 
     * @param value     value to return for the result of the loop
     */
    void Return(@NotNull R value);
}
