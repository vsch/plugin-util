package com.vladsch.plugin.util.ui.highlight;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.vladsch.flexmark.util.misc.BitField;
import com.vladsch.flexmark.util.misc.BitFieldSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.vladsch.plugin.util.ui.highlight.TypedRangeHighlightProvider.IdeHighlight.IDE_ERROR;
import static com.vladsch.plugin.util.ui.highlight.TypedRangeHighlightProvider.IdeHighlight.IDE_IGNORED;
import static com.vladsch.plugin.util.ui.highlight.TypedRangeHighlightProvider.IdeHighlight.IDE_WARNING;
import static com.vladsch.plugin.util.ui.highlight.TypedRangeHighlightProvider.IdeHighlight.NONE;

public interface TypedRangeHighlightProvider<R, T> extends HighlightProvider<T> {
    TextAttributesKey TYPO_ATTRIBUTES_KEY = TextAttributesKey.createTextAttributesKey("TYPO");
    TextAttributesKey ERROR_ATTRIBUTES_KEY = CodeInsightColors.ERRORS_ATTRIBUTES; // CodeInsightColors.MARKED_FOR_REMOVAL_ATTRIBUTES; // this one is only defined in 2018.1
    TextAttributesKey WARNING_ATTRIBUTES_KEY = TYPO_ATTRIBUTES_KEY;// CodeInsightColors.WEAK_WARNING_ATTRIBUTES;
    TextAttributesKey IGNORED_ATTRIBUTES_KEY = CodeInsightColors.NOT_USED_ELEMENT_ATTRIBUTES; // CodeInsightColors.MARKED_FOR_REMOVAL_ATTRIBUTES; // this one is only defined in 2018.1

    enum Flags implements BitField {
        IDE_HIGHLIGHT(8),   // reserved for TypedRangeHighlighter IDE flags
        ;

        final public int bits;

        Flags() { this(1); }

        Flags(int bits) { this.bits = bits; }

        @Override
        public int getBits() { return bits; }
    }

    Flags IDE_HIGHLIGHT = Flags.IDE_HIGHLIGHT;

    enum IdeHighlight {
        NONE(null),
        IDE_WARNING(WARNING_ATTRIBUTES_KEY),    // marks this highlight as using standard ide warning highlight
        IDE_ERROR(ERROR_ATTRIBUTES_KEY),        // marks this highlight as using standard ide error highlight
        IDE_IGNORED(IGNORED_ATTRIBUTES_KEY),    // marks this highlight as using standard ide unused variable highlight
        ;

        final public int mask;
        final public @Nullable TextAttributesKey attributesKey;

        IdeHighlight(@Nullable TextAttributesKey attributesKey) {
            mask = BitFieldSet.setBitField(0, Flags.IDE_HIGHLIGHT, ordinal());
            this.attributesKey = attributesKey;
        }

        @NotNull
        public static IdeHighlight get(int flags) {
            int ordinal = flags & F_IDE_HIGHLIGHT;

            if (ordinal >= values().length)
                throw new IllegalArgumentException(String.format("Ordinal %d is not in IdeHighlight enum", ordinal));
            return values()[ordinal];
        }
    }

    int F_IDE_HIGHLIGHT = BitFieldSet.intMask(IDE_HIGHLIGHT);
    int F_NONE = NONE.ordinal();
    int F_IDE_WARNING = IDE_WARNING.mask;
    int F_IDE_ERROR = IDE_ERROR.mask;
    int F_IDE_IGNORED = IDE_IGNORED.mask;

    default int addHighlightRange(R range, int flags) {
        return addHighlightRange(range, flags, -1);
    }

    /**
     * Add highlight range
     *
     * @param range      range object for highlight
     * @param flags      flags for the highlight
     * @param orderIndex index to use if &gt;0, else use next index
     *
     * @return order index used for the highlight
     */
    int addHighlightRange(R range, int flags, int orderIndex);

    TypedRangeHighlighter<R, T> getHighlighter(@NotNull Editor editor);

    boolean isRangeHighlighted(R range);

    int getMaxHighlightRangeIndex();

    int getHighlightRangeIndex(final R range);

    void removeHighlightRange(R range);

    /**
     * CAUTION: Do Not Use range value get flags for highlighting because implementors are free to modify the range before lookup, like change case
     *
     * NOTE: for lookup use value returned by getAdjustedRange()
     *
     * @return map of range to highlight flags
     */
    @Nullable
    Map<R, Integer> getHighlightRangeFlags();

    /**
     * CAUTION: Do Not Use range value get flags for highlighting because implementors are free to modify the range before lookup, like change case
     *
     * NOTE: for lookup use value returned by getAdjustedRange()
     *
     * @return map of range to highlight flags
     */
    @Nullable
    Map<R, Integer> getHighlightRangeIndices();

    /**
     * Used to adjust for things like case sensitivity otherwise in case insensitive mode the lookup in the map will fail
     *
     * @param range range to look up
     *
     * @return adjusted range
     */
    @NotNull
    R getAdjustedRange(@NotNull R range);
}
