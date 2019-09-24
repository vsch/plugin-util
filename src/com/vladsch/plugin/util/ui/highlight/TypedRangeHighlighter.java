package com.vladsch.plugin.util.ui.highlight;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class TypedRangeHighlighter<R, T> extends Highlighter<T> {
    private int[] myIndexedRangeCount = null;

    public TypedRangeHighlighter(@NotNull TypedRangeHighlightProvider<R, T> highlightProvider, @NotNull final Editor editor) {
        super(highlightProvider, editor);
    }

    public static interface RangeHighlighterCreationListener<R> {
        RangeHighlighter rangeHighlighterCreated(RangeHighlighter rangeHighlighter, final R range, final int index);
    }

    public static interface HighlighterAttributeConsumer<R> {
        void addRangeHighlighter(R range, int index, int startOffset, int endOffset, int layer, @Nullable TextAttributes attributes, @Nullable HighlighterTargetArea targetArea, @Nullable RangeHighlighterCreationListener<R> listener);
    }

    protected abstract boolean isHighlightAvailable();

    protected abstract void generateAttributeRanges(@NotNull HighlighterAttributeConsumer<R> consumer);

    public void updateHighlights() {
        boolean handled = false;

        TypedRangeHighlightProvider<R, T> highlightProvider = (TypedRangeHighlightProvider<R, T>) myHighlightProvider;
        if (highlightProvider.isShowHighlights()) {
            if (isHighlightAvailable()) {
                handled = true;
                removeHighlightsRaw();
                MarkupModel markupModel = myEditor.getMarkupModel();
                myIndexedRangeCount = new int[highlightProvider.getMaxHighlightRangeIndex()];

                generateAttributeRanges((range, index, startOffset, endOffset, layer, attributes, targetArea, listener) -> {
                    if (attributes != null) {
                        RangeHighlighter rangeHighlighter = markupModel.addRangeHighlighter(startOffset, endOffset, layer > 0 ? layer : HighlighterLayer.SELECTION - 2, attributes, targetArea != null ? targetArea : HighlighterTargetArea.EXACT_RANGE);
                        rangeHighlighter = listener == null ? rangeHighlighter : listener.rangeHighlighterCreated(rangeHighlighter, range, index);
                        if (myHighlighters == null || myHighlighterIndexList == null) {
                            myHighlighters = new ArrayList<>();
                            myHighlighterIndexList = new ArrayList<>();
                        }

                        myHighlighterIndexList.add(index);
                        myIndexedRangeCount[index]++;
                        myHighlighters.add(rangeHighlighter);
                    }
                });

                myHighlightProvider.fireHighlightsUpdated();
            }
        }

        if (!handled) {
            removeHighlightsRaw();
        }
    }

    public int getIndexedRangeCount(int rangeIndex) {
        if (myIndexedRangeCount != null && rangeIndex >= 0 && rangeIndex < myIndexedRangeCount.length) {
            return myIndexedRangeCount[rangeIndex];
        }
        return 0;
    }

    public int[] getIndexedRangeCounts() {
        return myIndexedRangeCount;
    }
}
