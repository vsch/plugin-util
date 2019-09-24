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

import org.jetbrains.annotations.Nullable;

import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;

public enum WordHighlighterFlags {
    NONE(RangeHighlighterFlags.NONE),
    IDE_WARNING(RangeHighlighterFlags.IDE_WARNING),      // marks this highlight as using standard ide warning highlight
    IDE_ERROR(RangeHighlighterFlags.IDE_ERROR),            // marks this highlight as using standard ide error highlight
    IDE_HIGHLIGHT(RangeHighlighterFlags.IDE_HIGHLIGHT),    // marks this highlight as using standard ide highlights
    BEGIN_WORD(4, 0),
    END_WORD(8, 0),
    CASE_INSENSITIVE(16, 32),
    CASE_SENSITIVE(32, 16),
    ;

    final public int mask;
    final public int invert;
    final public @Nullable RangeHighlighterFlags rangeFlags;


    WordHighlighterFlags(int mask, int invert, @Nullable RangeHighlighterFlags rangeFlags) {
        this.mask = mask;
        this.invert = invert;
        this.rangeFlags = rangeFlags;
    }

    WordHighlighterFlags(int mask, int invert) {
        this(mask,invert, null);
    }

    WordHighlighterFlags(RangeHighlighterFlags flags) {
        this(flags.mask, flags.invert, flags);
    }

    public static boolean haveFlags(int mask, WordHighlighterFlags flags) {
        return (mask & flags.mask) != 0;
    }

    public static WordHighlighterFlags fromFlags(int flags) {
        if (haveFlags(flags, IDE_ERROR)) return IDE_ERROR;
        if (haveFlags(flags, IDE_WARNING)) return IDE_WARNING;
        return NONE;
    }

    public static Set<WordHighlighterFlags> getSetFromFlags(int flags) {
        HashSet<WordHighlighterFlags> flagSet = new HashSet<>();
        if (haveFlags(flags, IDE_ERROR)) flagSet.add(IDE_ERROR);
        else if (haveFlags(flags, IDE_WARNING)) flagSet.add(IDE_WARNING);

        if (haveFlags(flags, BEGIN_WORD)) flagSet.add(BEGIN_WORD);
        if (haveFlags(flags, END_WORD)) flagSet.add(END_WORD);

        if (haveFlags(flags, CASE_SENSITIVE)) flagSet.add(CASE_SENSITIVE);
        else if (haveFlags(flags, CASE_INSENSITIVE)) flagSet.add(CASE_INSENSITIVE);
        return flagSet;
    }
}
