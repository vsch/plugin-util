/*
 *
 */

package com.vladsch.plugin.util.ui.highlight;

import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;

public interface LineHighlightProvider<T> extends HighlightProvider<T> {
    boolean isLineHighlighted(int line);
    int getHighlightLineIndex(int line);
    void addHighlightLine(int line);
    void removeHighlightLine(int line);
    LineHighlighter<T> getHighlighter(@NotNull Editor editor);
}
