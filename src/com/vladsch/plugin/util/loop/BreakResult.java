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

public class BreakResult {
    public static final Object NULL = new Object();
    
    private boolean myBreak;
    private boolean myContinue;

    public BreakResult() {
        myBreak = false;
        myContinue = false;
    }

    public boolean isBreak() {
        return myBreak;
    }

    public boolean isContinue() {
        return myContinue;
    }

    /**
     * Request descend into children of accepted value
     */
    public void Descend() {
        myContinue = false;
    }

    /**
     * Request continue looping for next value
     */
    public void Continue() {
        myContinue = true;
    }
    public void Return() {
        myBreak = true;
    }

    public void Break() {
        myBreak = true;
    }

}
