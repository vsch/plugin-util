/*
 *
 */

package com.vladsch.plugin.util.ui.highlight;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordHighlighter<T> extends Highlighter<T> {
    private int[] myIndexedWordCount = null;

    public WordHighlighter(@NotNull WordHighlightProvider<T> highlightProvider, @NotNull final Editor editor) {
        super(highlightProvider, editor);
    }

    // Override to customize
    public TextAttributes getAttributes(@Nullable TextAttributes attributes, final String word, final int startOffset, int endOffset) {
        return attributes;
    }

    public RangeHighlighter rangeHighlighterCreated(RangeHighlighter rangeHighlighter, final String word, final int index, final int startOffset, int endOffset) {
        return rangeHighlighter;
    }

    public void updateHighlights() {
        boolean handled = false;

        WordHighlightProvider<T> highlightProvider = (WordHighlightProvider<T>) myHighlightProvider;
        if (highlightProvider.isShowHighlights()) {
            Pattern pattern = highlightProvider.getHighlightPattern();
            Map<String, Integer> highlightWordFlags = highlightProvider.getHighlightWordFlags();
            if (pattern != null && highlightWordFlags != null) {
                handled = true;
                removeHighlightsRaw();
                Document document = myEditor.getDocument();
                MarkupModel markupModel = myEditor.getMarkupModel();
                Matcher matcher = pattern.matcher(document.getCharsSequence());
                myIndexedWordCount = new int[highlightProvider.getMaxHighlightWordIndex()];

                while (matcher.find()) {
                    // create a highlighter
                    String group = matcher.group();
                    int startOffset = matcher.start();
                    int endOffset = matcher.end();

                    Integer flags = highlightWordFlags.get(group);
                    if (flags == null) flags = 0;

                    int index = highlightProvider.getHighlightWordIndex(group);
                    TextAttributes attributes = highlightProvider.getHighlightAttributes(index, flags, startOffset, endOffset, null, null, EffectType.BOLD_DOTTED_LINE, 0);
                    attributes = getAttributes(attributes, group, startOffset, endOffset);
                    if (attributes != null) {
                        RangeHighlighter rangeHighlighter = markupModel.addRangeHighlighter(startOffset, endOffset, HighlighterLayer.SELECTION - 2, attributes, HighlighterTargetArea.EXACT_RANGE);
                        rangeHighlighter = rangeHighlighterCreated(rangeHighlighter, group, index, startOffset, endOffset);
                        if (myHighlighters == null || myHighlighterIndexList == null) {
                            myHighlighters = new ArrayList<>();
                            myHighlighterIndexList = new ArrayList<>();
                        }

                        myHighlighterIndexList.add(index);
                        myIndexedWordCount[index]++;
                        myHighlighters.add(rangeHighlighter);
                    }
                }

                myHighlightProvider.fireHighlightsUpdated();
            }
        }

        if (!handled) {
            removeHighlightsRaw();
        }
    }

    public int getIndexedWordCount(int wordIndex) {
        if (myIndexedWordCount != null && wordIndex >= 0 && wordIndex < myIndexedWordCount.length) {
            return myIndexedWordCount[wordIndex];
        }
        return 0;
    }

    public int[] getIndexedWordCounts() {
        return myIndexedWordCount;
    }
}
