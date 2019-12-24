package com.vladsch.plugin.util.ui.highlight;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface TypedRangeHighlightProvider<R, T> extends HighlightProvider<T> {
    TextAttributesKey TYPO_ATTRIBUTES_KEY = TextAttributesKey.createTextAttributesKey("TYPO");
    TextAttributesKey ERROR_ATTRIBUTES_KEY = CodeInsightColors.ERRORS_ATTRIBUTES; // CodeInsightColors.MARKED_FOR_REMOVAL_ATTRIBUTES; // this one is only defined in 2018.1
    TextAttributesKey WARNING_ATTRIBUTES_KEY = TYPO_ATTRIBUTES_KEY;// CodeInsightColors.WEAK_WARNING_ATTRIBUTES;

    default int addHighlightRange(R range, int flags) {
        return addHighlightRange(range, flags, -1);
    }

    /**
     * Add highlight range
     * @param range range object for highlight
     * @param flags flags for the highlight
     * @param orderIndex index to use if &gt;0, else use next index
     * @return order index used for the highlight
     */
    int addHighlightRange(R range, int flags, int orderIndex);

    TypedRangeHighlighter<R, T> getHighlighter(@NotNull Editor editor);

    boolean isRangeHighlighted(R range);

    int getMaxHighlightRangeIndex();

    int getHighlightRangeIndex(final R range);

    void removeHighlightRange(R range);

    /**
     * CAUTION: Do Not Use range value get flags for highlighting because implementors are free to modify the range before lookup, like change case
     *
     * NOTE: for lookup use value returned by getAdjustedRange()
     *
     * @return map of range to highlight flags
     */
    @Nullable
    Map<R, Integer> getHighlightRangeFlags();

    /**
     * CAUTION: Do Not Use range value get flags for highlighting because implementors are free to modify the range before lookup, like change case
     *
     * NOTE: for lookup use value returned by getAdjustedRange()
     *
     * @return map of range to highlight flags
     */
    @Nullable
    Map<R, Integer> getHighlightRangeIndices();

    /**
     * Used to adjust for things like case sensitivity otherwise in case insensitive mode the lookup in the map will fail
     *
     * @param range range to look up
     *
     * @return adjusted range
     */
    @NotNull
    R getAdjustedRange(@NotNull R range);
}
