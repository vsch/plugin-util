package com.vladsch.plugin.util.ui.highlight;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.vladsch.plugin.util.ui.ColorIterable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;

public interface HighlightProvider<T> {
    void settingsChanged(final ColorIterable colors, final T settings);

    void clearHighlights();

    boolean haveHighlights();

    boolean isHighlightsMode();

    void setHighlightsMode(boolean highlightsMode);

    boolean isShowHighlights();

    void initComponent();

    void disposeComponent();

    void enterUpdateRegion();

    void leaveUpdateRegion();

    void addHighlightListener(@NotNull HighlightListener highlightListener, @NotNull Disposable parent);

    void removeHighlightListener(HighlightListener highlightListener);

    void fireHighlightsChanged();

    void fireHighlightsUpdated();

    @Nullable
    TextAttributes getHighlightAttributes(int index, final int flags, int startOffset, int endOffset, @Nullable Color foregroundColor, @Nullable Color effectColor, @Nullable EffectType effectType, int fontType);

    Highlighter<T> getHighlighter(@NotNull Editor editor);
}
