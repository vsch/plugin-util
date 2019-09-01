/*
 *
 */

package com.vladsch.plugin.util.ui.highlight;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class LineHighlighter<T> extends Highlighter<T> {
    public LineHighlighter(@NotNull LineHighlightProvider<T> highlightProvider, @NotNull final Editor editor) {
        super(highlightProvider, editor);
    }

    // Override to customize
    public TextAttributes getAttributes(@Nullable TextAttributes attributes, final int line, final int startOffset, int endOffset) {
        return attributes;
    }

    @Override
    public int getOriginalIndex(final RangeHighlighter rangeHighlighter) {
        return getIndex(rangeHighlighter);
    }

    public RangeHighlighter rangeHighlighterCreated(RangeHighlighter rangeHighlighter, final int line, final int index, final int startOffset, int endOffset) {
        return rangeHighlighter;
    }

    public void updateHighlights() {
        LineHighlightProvider<T> highlightProvider = (LineHighlightProvider<T>) myHighlightProvider;
        if (highlightProvider.isShowHighlights()) {
            removeHighlightsRaw();
            Document document = myEditor.getDocument();
            MarkupModel markupModel = myEditor.getMarkupModel();
            int iMax = document.getLineCount();
            for (int i = 0; i < iMax; i++) {
                // create a highlighter
                int startOffset = document.getLineStartOffset(i);
                int endOffset = document.getLineEndOffset(i);
                int index = highlightProvider.getHighlightLineIndex(i);
                TextAttributes attributes = highlightProvider.getHighlightAttributes(index, 0, startOffset, endOffset, null, null, null, 0);
                attributes = getAttributes(attributes, i, startOffset, endOffset);

                if (attributes != null) {
                    RangeHighlighter rangeHighlighter = markupModel.addRangeHighlighter(startOffset, endOffset, HighlighterLayer.SELECTION - 2, attributes, HighlighterTargetArea.LINES_IN_RANGE);
                    rangeHighlighter = rangeHighlighterCreated(rangeHighlighter, i, index, startOffset, endOffset);
                    if (myHighlighters == null) {
                        myHighlighters = new ArrayList<>();
                    }

                    myHighlighters.add(rangeHighlighter);
                }
            }

            myHighlightProvider.fireHighlightsUpdated();
        } else {
            removeHighlights();
        }
    }
}
