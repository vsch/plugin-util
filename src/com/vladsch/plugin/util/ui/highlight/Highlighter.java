/*
 *
 */

package com.vladsch.plugin.util.ui.highlight;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class Highlighter<T> {
    @NotNull protected final Editor myEditor;
    @NotNull protected final HighlightProvider<T> myHighlightProvider;
    @Nullable protected List<RangeHighlighter> myHighlighters = null;
    @Nullable protected List<Integer> myHighlighterIndexList = null;

    public Highlighter(@NotNull final HighlightProvider<T> highlightProvider, @NotNull final Editor editor) {
        myEditor = editor;
        myHighlightProvider = highlightProvider;
    }

    static public void clearHighlighters(Editor editor, List<RangeHighlighter> highlighters) {
        if (highlighters != null) {
            MarkupModel markupModel = editor.getMarkupModel();
            for (RangeHighlighter marker : highlighters) {
                if (marker.isValid()) {
                    markupModel.removeHighlighter(marker);
                    marker.dispose();
                }
            }
            editor.getContentComponent().invalidate();
        }
    }

    protected void removeHighlightsRaw() {
        clearHighlighters(myEditor, myHighlighters);
        myHighlighters = null;
        myHighlighterIndexList = null;
    }

    public void removeHighlights() {
        if (myHighlighters != null && !myHighlighters.isEmpty()) {
            removeHighlightsRaw();
            myHighlightProvider.fireHighlightsUpdated();
        }
    }

    public RangeHighlighter getRangeHighlighter(int offset) {
        if (myHighlighters != null) {
            for (RangeHighlighter rangeHighlighter : myHighlighters) {
                if (offset >= rangeHighlighter.getStartOffset() && offset <= rangeHighlighter.getEndOffset()) {
                    return rangeHighlighter;
                }
            }
        }
        return null;
    }

    public RangeHighlighter getNextRangeHighlighter(int offset) {
        if (myHighlighters != null) {
            for (RangeHighlighter rangeHighlighter : myHighlighters) {
                if (rangeHighlighter.getStartOffset() >= offset) {
                    return rangeHighlighter;
                }
            }
        }
        return null;
    }

    public RangeHighlighter getPreviousRangeHighlighter(int offset) {
        if (myHighlighters != null) {
            int iMax = myHighlighters.size();
            for (int i = iMax; i-- > 0; ) {
                RangeHighlighter rangeHighlighter = myHighlighters.get(i);
                if (rangeHighlighter.getEndOffset() <= offset) {
                    return rangeHighlighter;
                }
            }
        }
        return null;
    }

    public int getIndex(RangeHighlighter rangeHighlighter) {
        if (myHighlighters != null) {
            return myHighlighters.indexOf(rangeHighlighter);
        }
        return -1;
    }

    public int getOriginalIndex(RangeHighlighter rangeHighlighter) {
        int index = getIndex(rangeHighlighter);
        if (myHighlighterIndexList != null && index >= 0 && index < myHighlighterIndexList.size()) {
            return myHighlighterIndexList.get(index);
        }
        return index;
    }

    public abstract void updateHighlights();
}
