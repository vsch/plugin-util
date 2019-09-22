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

package com.vladsch.plugin.util

import java.util.function.Function

class LazyFunction<T : Any, R : Any>(private val computable: Function<T, R>) {

    // getting this value will do the computation on first request
    private var argument: T? = null

    private val _value: R by lazy {
        computable.apply(argument!!)
    }

    /**
     * Always returns result from first computation
     */
    fun getValue(argument:T):R  {
        this.argument = argument
        return _value;
    }
}
