package com.vladsch.plugin.util.ui.highlight;

import com.vladsch.flexmark.util.misc.BitField;
import com.vladsch.flexmark.util.misc.BitFieldSet;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public interface WordHighlightProvider<T> extends TypedRangeHighlightProvider<String, T> {
    enum Flags implements BitField {
        IDE_HIGHLIGHT(TypedRangeHighlightProvider.IDE_HIGHLIGHT.bits),   // reserved for RangeHighlighter IDE flags
        BEGIN_WORD,
        END_WORD,
        CASE_SENSITIVITY(2),
        ;

        final public int bits;

        Flags() { this(1); }

        Flags(int bits) { this.bits = bits; }

        @Override
        public int getBits() { return bits; }
    }

    enum CaseSensitivity {
        NONE,
        SENSITIVE,
        INSENSITIVE,
        ;

        public final int mask;

        CaseSensitivity() {
            mask = BitFieldSet.setBitField(0, Flags.CASE_SENSITIVITY, ordinal());
        }
    }

    Flags BEGIN_WORD = Flags.BEGIN_WORD;
    Flags END_WORD = Flags.END_WORD;
    Flags CASE_SENSITIVITY = Flags.CASE_SENSITIVITY;

    int F_BEGIN_WORD = BitFieldSet.intMask(BEGIN_WORD);
    int F_END_WORD = BitFieldSet.intMask(END_WORD);
    int F_WORD = F_BEGIN_WORD | F_END_WORD;

    int F_CASE_SENSITIVITY = BitFieldSet.intMask(CASE_SENSITIVITY);
    int F_CASE_SENSITIVE = CaseSensitivity.SENSITIVE.mask;
    int F_CASE_INSENSITIVE = CaseSensitivity.INSENSITIVE.mask;

    default int encodeFlags(boolean beginWord, boolean endWord, int ideHighlight, @Nullable Boolean caseSensitive) {
        //noinspection PointlessBitwiseExpression
        return 0
                | (beginWord ? F_BEGIN_WORD : 0)
                | (endWord ? F_END_WORD : 0)
                | BitFieldSet.setBitField(0, IDE_HIGHLIGHT, ideHighlight)
                | (caseSensitive == null ? 0 : caseSensitive ? F_CASE_SENSITIVE : F_CASE_INSENSITIVE);
    }

    default int encodeFlags(int ideHighlight) {
        return BitFieldSet.setBitField(0, IDE_HIGHLIGHT, ideHighlight);
    }

    default int addHighlightRange(String range, boolean beginWord, boolean endWord, int ideHighlight, @Nullable Boolean caseSensitive) {
        return addHighlightRange(range, encodeFlags(beginWord, endWord, ideHighlight, caseSensitive));
    }

    default int addHighlightRange(String range, int orderIndex, boolean beginWord, boolean endWord, int ideHighlight, @Nullable Boolean caseSensitive) {
        return addHighlightRange(range, encodeFlags(beginWord, endWord, ideHighlight, caseSensitive), orderIndex);
    }

    Pattern getHighlightPattern();

    void updateHighlightPattern();

    boolean isHighlightCaseSensitive();

    void setHighlightCaseSensitive(boolean highlightCaseSensitive);

    boolean isHighlightWordsMatchBoundary();

    void setHighlightWordsMatchBoundary(boolean highlightWordsMatchBoundary);
}
