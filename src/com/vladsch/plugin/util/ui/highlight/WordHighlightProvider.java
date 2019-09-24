package com.vladsch.plugin.util.ui.highlight;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.regex.Pattern;

public interface WordHighlightProvider<T> extends TypedRangeHighlightProvider<String, T> {
    default int encodeFlags(boolean beginWord, boolean endWord, final boolean ideWarning, final boolean ideError, Boolean caseSensitive) {
        //noinspection PointlessBitwiseExpression
        return 0
                | (beginWord ? WordHighlighterFlags.BEGIN_WORD.mask : 0)
                | (endWord ? WordHighlighterFlags.END_WORD.mask : 0)
                | (ideError ? WordHighlighterFlags.IDE_ERROR.mask : (ideWarning ? WordHighlighterFlags.IDE_WARNING.mask : 0))
                | (caseSensitive == null ? 0 : caseSensitive ? WordHighlighterFlags.CASE_SENSITIVE.mask : WordHighlighterFlags.CASE_INSENSITIVE.mask);
    }

    default int encodeFlags(final boolean ideWarning, final boolean ideError) {
        return (ideError ? RangeHighlighterFlags.IDE_ERROR.mask : (ideWarning ? RangeHighlighterFlags.IDE_WARNING.mask : 0));
    }

    default int encodeFlags(@NotNull WordHighlighterFlags... flags) {
        int mask = 0;
        for (WordHighlighterFlags flag : flags) {
            mask |= flag.mask;
            mask &= ~flag.invert;
        }
        return mask;
    }

    default int addHighlightRange(String range, boolean beginWord, boolean endWord, final boolean ideWarning, final boolean ideError, Boolean caseSensitive) {
        return addHighlightRange(range, encodeFlags(beginWord, endWord, ideWarning, ideError, caseSensitive));
    }

    default int addHighlightRange(String range, int orderIndex, boolean beginWord, boolean endWord, final boolean ideWarning, final boolean ideError, Boolean caseSensitive) {
        return addHighlightRange(range, encodeFlags(beginWord, endWord, ideWarning, ideError, caseSensitive), orderIndex);
    }

    default int addHighlightRange(String range, WordHighlighterFlags... flags) {
        return addHighlightRange(range, encodeFlags(flags));
    }

    default int addHighlightRange(String range, int orderIndex, WordHighlighterFlags... flags) {
        return addHighlightRange(range, encodeFlags(flags), orderIndex);
    }

    Pattern getHighlightPattern();

    void updateHighlightPattern();

    @Nullable
    Map<String, Integer> getHighlightCaseSensitiveWordIndices();

    @Nullable
    Map<String, Integer> getHighlightCaseInsensitiveWordIndices();

    boolean isHighlightWordsCaseSensitive();

    void setHighlightWordsCaseSensitive(boolean highlightWordsCaseSensitive);
}
