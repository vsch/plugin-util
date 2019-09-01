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
import java.util.BitSet;

public class LineRangeHighlighter<T> extends LineHighlighter<T> {
    public LineRangeHighlighter(@NotNull LineRangeHighlightProvider<T> highlightProvider, @NotNull final Editor editor) {
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
        LineRangeHighlightProvider<T> highlightProvider = (LineRangeHighlightProvider<T>) myHighlightProvider;
        if (highlightProvider.isShowHighlights() && highlightProvider.getHighlightLines() != null) {
            removeHighlightsRaw();
            Document document = myEditor.getDocument();
            MarkupModel markupModel = myEditor.getMarkupModel();

            int startLine = 0;
            int startOffset = 0;
            int endOffset = document.getTextLength();
            int endLine = document.getLineCount();
            BitSet bitSet = highlightProvider.getHighlightLines();
            boolean isInvertedHighlights = highlightProvider.isInvertedHighlights();

            while (startOffset < endOffset && startLine < endLine) {
                int nextLine = isInvertedHighlights ? bitSet.nextSetBit(startLine) : bitSet.nextClearBit(startLine);
                int firstOffset = nextLine == -1 ? endOffset : document.getLineStartOffset(nextLine) - 1;

                if (startOffset < firstOffset) {
                    TextAttributes attributes = highlightProvider.getHighlightAttributes(startLine, 0, startOffset, firstOffset, null, null, null, 0);
                    attributes = getAttributes(attributes, startLine, startOffset, firstOffset);

                    if (attributes != null) {
                        RangeHighlighter rangeHighlighter = markupModel.addRangeHighlighter(startOffset, firstOffset, HighlighterLayer.SELECTION - 2, attributes, HighlighterTargetArea.LINES_IN_RANGE);
                        rangeHighlighter = rangeHighlighterCreated(rangeHighlighter, startLine, startLine, startOffset, firstOffset);
                        if (myHighlighters == null) {
                            myHighlighters = new ArrayList<>();
                        }

                        myHighlighters.add(rangeHighlighter);
                    }
                }

                if (nextLine == -1) break;

                int lastLine = isInvertedHighlights ? bitSet.nextClearBit(nextLine) : bitSet.nextSetBit(nextLine);
                if (lastLine == -1 || lastLine >= endLine) break;

                int lastOffset = document.getLineStartOffset(lastLine);

                startLine = lastLine;
                startOffset = lastOffset;
            }

            myHighlightProvider.fireHighlightsUpdated();
        } else {
            removeHighlights();
        }
    }
}
