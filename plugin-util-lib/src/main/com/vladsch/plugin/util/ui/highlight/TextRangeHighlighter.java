package com.vladsch.plugin.util.ui.highlight;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class TextRangeHighlighter<T> extends TypedRangeHighlighter<TextRange, T> {
    public TextRangeHighlighter(@NotNull TypedRangeHighlightProvider<TextRange, T> highlightProvider, @NotNull final Editor editor) {
        super(highlightProvider, editor);
    }

    // Override to customize
    public TextAttributes getAttributes(@Nullable TextAttributes attributes, final TextRange range) {
        return attributes;
    }

    // Override to customize
    public RangeHighlighter rangeHighlighterCreated(RangeHighlighter rangeHighlighter, final TextRange range, final int index) {
        return rangeHighlighter;
    }

    @Override
    protected boolean isHighlightAvailable() {
        TypedRangeHighlightProvider<TextRange, T> highlightProvider = (TypedRangeHighlightProvider<TextRange, T>) myHighlightProvider;
        Map<TextRange, Integer> highlightRangeFlags = highlightProvider.getHighlightRangeFlags();
        Map<TextRange, Integer> rangeIndices = highlightProvider.getHighlightRangeIndices();
        return rangeIndices != null && highlightRangeFlags != null;
    }

    @Override
    protected void generateAttributeRanges(@NotNull final HighlighterAttributeConsumer<TextRange> consumer) {
        TypedRangeHighlightProvider<TextRange, T> highlightProvider = (TypedRangeHighlightProvider<TextRange, T>) myHighlightProvider;
        Map<TextRange, Integer> highlightRangeFlags = highlightProvider.getHighlightRangeFlags();
        Map<TextRange, Integer> rangeIndices = highlightProvider.getHighlightRangeIndices();
        assert rangeIndices != null && highlightRangeFlags != null;

        for (Map.Entry<TextRange, Integer> entry : rangeIndices.entrySet()) {
            // create a highlighter
            TextRange group = entry.getKey();
            int index = entry.getValue();
            Integer flags = highlightRangeFlags.get(group);
            int startOffset = group.getStartOffset();
            int endOffset = group.getEndOffset();

            if (flags == null) flags = 0;

            TextAttributes attributes = highlightProvider.getHighlightAttributes(index, flags, startOffset, endOffset, null, null, EffectType.BOLD_DOTTED_LINE, 0);
            attributes = getAttributes(attributes, group);
            consumer.addRangeHighlighter(group, index, startOffset, endOffset, -1, attributes, null,
                    (rangeHighlighter, range, index1) -> rangeHighlighterCreated(rangeHighlighter, group, index));
        }
    }
}
