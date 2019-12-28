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

package com.vladsch.plugin.util.ui.highlight;

public enum RangeHighlighterFlags {
    NONE(0, 0),
    IDE_WARNING(1, 6),    // marks this highlight as using standard ide warning highlight
    IDE_ERROR(2, 5),      // marks this highlight as using standard ide error highlight
    IDE_IGNORED(4, 3),      // marks this highlight as using standard ide error highlight
    IDE_HIGHLIGHT(7, 7),  // remove highlight as using standard ide highlights
    ;

    final public int mask;
    final public int invert;

    RangeHighlighterFlags(int mask, int invert) {
        this.mask = mask;
        this.invert = invert;
    }

    public static boolean haveFlags(int mask, RangeHighlighterFlags flags) {
        return (mask & flags.mask) != 0;
    }

    public static RangeHighlighterFlags fromFlags(int flags) {
        if (haveFlags(flags, IDE_ERROR)) return IDE_ERROR;
        if (haveFlags(flags, IDE_WARNING)) return IDE_WARNING;
        if (haveFlags(flags, IDE_IGNORED)) return IDE_IGNORED;
        return NONE;
    }
}
