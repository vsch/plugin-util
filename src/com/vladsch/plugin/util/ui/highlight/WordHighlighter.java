package com.vladsch.plugin.util.ui.highlight;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordHighlighter<T> extends TypedRangeHighlighter<String, T> {
    private int[] myIndexedWordCount = null;

    public WordHighlighter(@NotNull WordHighlightProvider<T> highlightProvider, @NotNull final Editor editor) {
        super(highlightProvider, editor);
    }

    // Override to customize
    public TextAttributes getAttributes(@Nullable TextAttributes attributes, final String word, final int startOffset, int endOffset) {
        return attributes;
    }

    // Override to customize
    public RangeHighlighter rangeHighlighterCreated(RangeHighlighter rangeHighlighter, final String word, final int index, final int startOffset, int endOffset) {
        return rangeHighlighter;
    }

    @Override
    protected boolean isHighlightAvailable() {
        WordHighlightProvider<T> highlightProvider = (WordHighlightProvider<T>) myHighlightProvider;
        Pattern pattern = highlightProvider.getHighlightPattern();
        Map<String, Integer> highlightWordFlags = highlightProvider.getHighlightRangeFlags();

        return pattern != null && highlightWordFlags != null;
    }

    @Override
    protected void generateAttributeRanges(@NotNull final HighlighterAttributeConsumer<String> consumer) {
        WordHighlightProvider<T> highlightProvider = (WordHighlightProvider<T>) myHighlightProvider;
        Pattern pattern = highlightProvider.getHighlightPattern();
        Map<String, Integer> highlightWordFlags = highlightProvider.getHighlightRangeFlags();

        assert pattern != null && highlightWordFlags != null;

        Document document = myEditor.getDocument();
        Matcher matcher = pattern.matcher(document.getCharsSequence());

        while (matcher.find()) {
            // create a highlighter
            String group = matcher.group();
            int startOffset = matcher.start();
            int endOffset = matcher.end();

            Integer flags = highlightWordFlags.get(highlightProvider.getAdjustedRange(group));
            if (flags == null) flags = 0;

            int index = highlightProvider.getHighlightRangeIndex(group);
            TextAttributes attributes = highlightProvider.getHighlightAttributes(index, flags, startOffset, endOffset, null, null, EffectType.BOLD_DOTTED_LINE, 0);
            attributes = getAttributes(attributes, group, startOffset, endOffset);

            if (attributes != null) {
                consumer.addRangeHighlighter(group, index, startOffset, endOffset, -1, attributes, null,
                        (rangeHighlighter, range, index1) -> rangeHighlighter = rangeHighlighterCreated(rangeHighlighter, group, index, startOffset, endOffset));
            }
        }
    }
}
