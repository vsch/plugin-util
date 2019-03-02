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

public class ParameterizedRow {
    private final String locationPrefix;
    private int index;

    public ParameterizedRow(final String locationPrefix) {
        this.locationPrefix = locationPrefix.endsWith("/") ? locationPrefix : locationPrefix + "/";
        index = 0;
    }

    public static class LineInfo {
        final public String file;
        final public int line;
        final public int index;

        public LineInfo(final String file, final int line, final int index) {
            this.file = file;
            this.line = line;
            this.index = index;
        }
    }

    public LineInfo getCallerInfo(int callerDepth) {
        StackTraceElement callerInfo = Thread.currentThread().getStackTrace()[callerDepth];
        return new LineInfo(locationPrefix + callerInfo.getFileName(), callerInfo.getLineNumber(), index++);
    }
}
