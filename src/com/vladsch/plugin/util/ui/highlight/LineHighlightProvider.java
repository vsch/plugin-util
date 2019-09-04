/*
 *
 */

package com.vladsch.plugin.util.ui.highlight;

import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface LineHighlightProvider<T> extends HighlightProvider<T> {
    boolean isLineHighlighted(int line);

    int getHighlightLineIndex(int line);

    default void addHighlightLine(int line, boolean replaceExisting) {
        throw new IllegalStateException("method not implemented");
    }

    void addHighlightLine(int line);

    default void addHighlightLines(int... lines) {
        for (int line : lines) addHighlightLine(line);
    }

    default void addHighlightLines(Collection<Integer> lines) {
        for (int line : lines) addHighlightLine(line);
    }

    default void addHighlightLines(boolean replaceExisting, int... lines) {
        for (int line : lines) addHighlightLine(line, replaceExisting);
    }

    void removeHighlightLine(int line);

    LineHighlighter<T> getHighlighter(@NotNull Editor editor);
}
