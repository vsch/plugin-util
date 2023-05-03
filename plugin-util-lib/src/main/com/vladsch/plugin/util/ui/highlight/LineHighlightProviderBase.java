/*
 * Copyright (c) 2015-2019 Vladimir Schneider <vladimir.schneider@gmail.com>, all rights reserved.
 *
 * This code is private property of the copyright holder and cannot be used without
 * having obtained a license or prior written permission of the copyright holder.
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

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public abstract class LineHighlightProviderBase<T> extends HighlightProviderBase<T> implements LineHighlightProvider<T> {
    @Nullable protected Map<Integer, Integer> myHighlightLines;
    protected int myOriginalOrderIndex = 0;

    public LineHighlightProviderBase(@NotNull T settings) {
        super(settings);
    }

    @Override
    public LineHighlighter<T> getHighlighter(@NotNull final Editor editor) {
        return new LineHighlighter<>(this, editor);
    }

    public void clearHighlights() {
        myHighlightLines = null;
        myOriginalOrderIndex = 0;
        fireHighlightsChanged();
    }

    @Override
    public boolean isLineHighlighted(int line) {
        return myHighlightLines != null && myHighlightLines.containsKey(line);
    }

    @Nullable
    public Map<Integer, Integer> getHighlightLines() {
        return myHighlightLines;
    }

    @Override
    public boolean haveHighlights() {
        return myHighlightLines != null && isHighlightsMode();
    }

    // WordHighlightProvider
    @Override
    public boolean isShowHighlights() {
        return isHighlightsMode() && myHighlightLines != null && !myHighlightLines.isEmpty();
    }

    /**
     * Must call getHighlightPattern() before calling this function for the first time to ensure
     * the cached structures are updated.
     *
     * @param index       highlighted line number
     * @param flags       not used
     * @param startOffset start offset in editor
     * @param endOffset   end offset in editor
     *
     * @return text attributes to use for highlight or null if not highlighted
     */
    @Override
    @Nullable
    public TextAttributes getHighlightAttributes(final int index, final int flags, final int startOffset, final int endOffset, final @Nullable Color foregroundColor, final @Nullable Color effectColor, final @Nullable EffectType effectType, final int fontType) {
        if (myHighlightLines != null && myHighlightColors != null) {
            if (index >= 0) {
                int colorRepeatIndex = myHighlightColorRepeatIndex;
                int colorRepeatSteps = myHighlightColors.length - colorRepeatIndex;
                return new TextAttributes(foregroundColor,
                        myHighlightColors[index < colorRepeatIndex ? index : colorRepeatIndex + ((index - colorRepeatIndex) % colorRepeatSteps)],
                        effectColor,
                        effectType,
                        fontType
                );
            }
        }
        return null;
    }

    @Override
    public int getHighlightLineIndex(final int line) {
        return myHighlightLines != null && myHighlightLines.containsKey(line) ? myHighlightLines.get(line) : -1;
    }

    @Override
    public void addHighlightLine(final int line) {
        addHighlightLine(line, true);
    }

    @Override
    public void addHighlightLine(final int line, boolean replaceExisting) {
        if (myHighlightLines != null) {
            if (!replaceExisting && myHighlightLines.containsKey(line)) return;

            // remove and add so flags will be modified and it will be moved to the end of list (which is considered the head)
            myHighlightLines.remove(line);
        } else {
            myHighlightLines = new HashMap<>();
        }

        int originalOrderIndex = myOriginalOrderIndex;
        if (!isInHighlightSet()) myOriginalOrderIndex++;

        myHighlightLines.put(line, originalOrderIndex);

        fireHighlightsChanged();
    }

    @Override
    protected void skipHighlightSets(final int skipSets) {
        myOriginalOrderIndex += skipSets;
    }

    @Override
    protected void setHighlightIndex(int index) {
        myOriginalOrderIndex = index;
    }

    @Override
    protected int getHighlightIndex() {
        return myOriginalOrderIndex;
    }

    @Override
    public void removeHighlightLine(final int line) {
        if (myHighlightLines != null) {
            if (myHighlightLines.containsKey(line)) {
                // remove and add
                myHighlightLines.remove(line);
                fireHighlightsChanged();
            }
        }
    }
}
