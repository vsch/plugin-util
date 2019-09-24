package com.vladsch.plugin.util.ui.highlight;

import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

public interface TextRangeHighlightProvider<T> extends TypedRangeHighlightProvider<TextRange, T> {
    default int encodeFlags(final boolean ideWarning, final boolean ideError) {
        return (ideError ? RangeHighlighterFlags.IDE_ERROR.mask : (ideWarning ? RangeHighlighterFlags.IDE_WARNING.mask : 0));
    }

    default int encodeFlags(@NotNull RangeHighlighterFlags... flags) {
        int mask = 0;
        for (RangeHighlighterFlags flag : flags) {
            mask |= flag.mask;
            mask &= ~flag.invert;
        }
        return mask;
    }
}
