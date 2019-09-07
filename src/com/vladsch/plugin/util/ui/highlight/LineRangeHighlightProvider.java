package com.vladsch.plugin.util.ui.highlight;

import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.BitSet;

public interface LineRangeHighlightProvider<T> extends LineHighlightProvider<T> {
    boolean isLineHighlighted(int line);

    @Nullable
    BitSet getHighlightLines();

    @Nullable
    BitSet addHighlightLines(int startLine, int endLine);

    @Nullable
    BitSet addHighlightLines(@Nullable BitSet bitSet);

    @Nullable
    BitSet removeHighlightLines(@Nullable BitSet bitSet);

    @Nullable
    BitSet removeHighlightLines(int startLine, int endLine);

    boolean isInvertedHighlights();

    void setInvertedHighlights(boolean invertedHighlights);

    LineRangeHighlighter<T> getHighlighter(@NotNull Editor editor);

    void setHighlightLines(BitSet bitSet, Boolean highlightMode);
}
