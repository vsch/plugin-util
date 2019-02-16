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

package com.vladsch.plugin.util.loop;

import com.vladsch.flexmark.util.options.MutableDataHolder;

public interface VoidLoop {
    Object NULL = new Object();

    void Recurse();              // recurse into current element regardless of recursion predicate 

    void Continue(int outerLevels);

    void Break(int outerLevels);

    boolean haveNext();

    boolean isBreak();          // true if looping terminated 

    boolean isTerminated();     // return true if current iteration is terminated ( break, continue, return)

    boolean isActive();          // return true if current iteration has not been terminated

    int getLoopCount();         // return total times through the loop of the iteration, includes skipped elements due to filtering

    int getCount();             // return total times consumer was invoked, ie. valid elements

    int getTotalLoopCount();    // return loop count across all recursions  

    int getTotalCount();        // return count across all recursions 

    int getRecursionCount();

    MutableDataHolder getData();

    default void Continue() { Continue(0); }

    default void Break() { Break(0); }
}
