package com.vladsch.plugin.util;

import static com.vladsch.plugin.util.HelpersKt.minLimit;
import static com.vladsch.plugin.util.HelpersKt.rangeLimit;
import static java.lang.Character.isDigit;

@SuppressWarnings("WeakerAccess")
public class StudiedWord {
    public static final int EMPTY = 0x0001;
    public static final int NUL = 0x0002;
    public static final int CTRL = 0x0004;
    public static final int SPACE = 0x0008;
    public static final int UNDER = 0x0010;
    public static final int DOT = 0x0020;
    public static final int DASH = 0x0040;
    public static final int SLASH = 0x0080;
    public static final int DIGITS = 0x0100;
    public static final int LOWER = 0x0200;
    public static final int UPPER = 0x0400;
    public static final int SYMBOLS = 0x0800;
    public static final int OTHER = 0x1000;
    public static final int NOT_DEFINED = 0x2000;

    public static final int LOWER_TO_UPPER = 0x00010000;
    public static final int UNDER_TO_UPPER = 0x00010000;
    public static final int DASH_TO_UPPER = 0x00020000;
    public static final int UPPER_TO_LOWER = 0x00020000;

    public static final int LETTER = LOWER | UPPER;
    public static final int ALPHANUMERIC = LOWER | UPPER | DIGITS;
    public static final int SEPARATOR = UNDER | DOT | DASH | SLASH;

    private static int[] ascii = new int[] {
            0x0002, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004,
            0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004, 0x0004,
            0x0008, 0x0800, 0x0800, 0x0800, 0x0800, 0x0800, 0x0800, 0x0800, 0x0800, 0x0800, 0x0800, 0x0800, 0x0800, 0x0040, 0x0020, 0x0080,
            0x0100, 0x0100, 0x0100, 0x0100, 0x0100, 0x0100, 0x0100, 0x0100, 0x0100, 0x0100, 0x0800, 0x0800, 0x0800, 0x0800, 0x0800, 0x0800,
            0x0800, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400,
            0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0800, 0x0800, 0x0800, 0x0800, 0x0010,
            0x0800, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200,
            0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0800, 0x0800, 0x0800, 0x0800, 0x0800,
            0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000,
            0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000,
            0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x0200, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000,
            0x1000, 0x1000, 0x1000, 0x1000, 0x1000, 0x0200, 0x1000, 0x1000, 0x1000, 0x1000, 0x0200, 0x1000, 0x1000, 0x1000, 0x1000, 0x1000,
            0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400,
            0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x1000, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0400, 0x0200,
            0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200,
            0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x1000, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200, 0x0200,
    };

    public static int separatorCharFlags(char separator) {
        return separator < ascii.length ? ascii[separator] & SEPARATOR : 0;
    }

    private final CharSequence myWord;
    private final int myFirstFlags;
    private final int myRestFlags;
    private final int mySecondFlags;
    private final int myLastFlags;
    private final int myWordFlags;
    private final int mySeparators;

    public StudiedWord(final CharSequence word, int separators) {
        mySeparators = separators;
        myWord = word;
        myFirstFlags = myWord.length() == 0 ? EMPTY : flags(myWord.charAt(0));
        mySecondFlags = myWord.length() < 2 ? EMPTY : flags(myWord.charAt(1));
        myRestFlags = flags(myWord, 1);
        myWordFlags = flags(myWord);
        myLastFlags = myWord.length() == 0 ? EMPTY : flags(myWord.charAt(myWord.length() - 1));
    }

    public static StudiedWord of(CharSequence word, int separators) {
        return new StudiedWord(word, separators);
    }

    public CharSequence getWord() {
        return myWord;
    }

    public boolean some(int flags) {
        return some(myWordFlags, flags);
    }

    public boolean none(int flags) {
        return none(myWordFlags, flags);
    }

    public boolean only(int flags) {
        return only(myWordFlags, flags);
    }

    public boolean is(int flags) {
        return is(myWordFlags, flags);
    }

    public boolean all(int flags) {
        return all(myWordFlags, flags);
    }

    public boolean first(int flags) {
        return some(myFirstFlags, flags) && only(myFirstFlags, flags);
    }

    public boolean rest(int flags) {
        return some(myRestFlags, flags) && only(myRestFlags, flags);
    }

    public boolean second(int flags) {
        return some(mySecondFlags, flags) && only(mySecondFlags, flags);
    }

    public boolean last(int flags) {
        return some(myLastFlags, flags) && only(myLastFlags, flags);
    }

    public boolean identifier() {
        return only(LOWER | UPPER | DIGITS | UNDER) && first(LOWER | UPPER | UNDER);
    }

    public static int compute(char c) {
        if (c == 0) return NUL;
        else if (c < 0x20) return CTRL;
        else if (c == ' ') return SPACE;
        else if (c == '_') return UNDER;
        else if (c == '.') return DOT;
        else if (c == '-') return DASH;
        else if (c == '/') return SLASH;
        else if (Character.isLowerCase(c)) return LOWER;
        else if (Character.isUpperCase(c)) return UPPER;
        else if (isDigit(c)) return DIGITS;
        else if (c < 128) return SYMBOLS;
        else if (!Character.isDefined(c)) return NOT_DEFINED;
        else return OTHER;
    }

    public static int flags(char c) {
        if (c == 0) return NUL;
        if (c < 256) return ascii[c];
        else if (Character.isLowerCase(c)) return LOWER;
        else if (Character.isUpperCase(c)) return UPPER;
        else if (!Character.isDefined(c)) return NOT_DEFINED;
        else return OTHER;
    }

    public static int flags(CharSequence word) {
        return flags(word, 0, word.length());
    }

    public static int flags(CharSequence word, int startIndex) {
        return flags(word, startIndex, word.length());
    }

    public static int flags(CharSequence word, int startIndex, int endIndex) {
        int flags = 0;
        int start = minLimit(startIndex, 0);
        int iMax = rangeLimit(endIndex, start, word.length());
        if (iMax == start) flags |= EMPTY;
        else {
            for (int i = start; i < iMax; i++) {
                char c = word.charAt(i);
                if (c < 256) flags |= ascii[c];
                else if (Character.isLowerCase(c)) flags |= LOWER;
                else if (Character.isUpperCase(c)) flags |= UPPER;
                else if (!Character.isDefined(c)) flags |= NOT_DEFINED;
                else flags |= OTHER;
            }
        }
        return flags;
    }

    public static boolean some(int wordFlags, int flags) {
        return (wordFlags & flags) != 0;
    }

    public static boolean none(int wordFlags, int flags) {
        return (wordFlags & flags) == 0;
    }

    public static boolean only(int wordFlags, int flags) {
        return some(wordFlags, flags) && none(wordFlags, ~flags);
    }

    public static boolean is(int wordFlags, int flags) {
        return wordFlags == flags;
    }

    public static boolean all(int wordFlags, int flags) {
        return (wordFlags & flags) == flags;
    }

    // @formatter:off
    // these check if only valid case chars are present
    public boolean isAnySnakeCase()                 { return only(UNDER | LOWER | UPPER | DIGITS) && all(UNDER) && some(LOWER | UPPER); }
    public boolean isAnyDashCase()                  { return only(DASH | LOWER | UPPER | DIGITS) && all(DASH) && some(LOWER | UPPER); }
    public boolean isAnyDotCase()                   { return only(DOT | LOWER | UPPER | DIGITS) && all(DOT) && some(LOWER | UPPER); }
    public boolean isAnySlashCase()                 { return only(SLASH | LOWER | UPPER | DIGITS) && all(SLASH) && some(LOWER | UPPER); }

    // these check if only valid case chars are present and first is letter
    public boolean isMixedSnakeCase()               { return only(UNDER | LOWER | UPPER | DIGITS) && all(UNDER | LOWER | UPPER) && first(LOWER | UPPER); }
    public boolean isMixedDashCase()                { return only(DASH | LOWER | UPPER | DIGITS) && all(DASH | LOWER | UPPER) && first(LOWER | UPPER); }
    public boolean isMixedDotCase()                 { return only(DOT | LOWER | UPPER | DIGITS) && all(DOT | LOWER | UPPER) && first(LOWER | UPPER); }
    public boolean isMixedSlashCase()               { return only(SLASH | LOWER | UPPER | DIGITS) && all(SLASH | LOWER | UPPER) && first(LOWER | UPPER); }

    // these check if only valid case chars are present and first is uppercase letter
    public boolean isFirstCapSnakeCase()            { return only(UNDER | LOWER | UPPER | DIGITS) && rest(UNDER | LOWER) && first(UPPER); }
    public boolean isFirstCapDashCase()             { return only(DASH | LOWER | UPPER | DIGITS) && rest(DASH | LOWER) && first(UPPER); }
    public boolean isFirstCapDotCase()              { return only(DOT | LOWER | UPPER | DIGITS) && rest(DOT | LOWER) && first(UPPER); }
    public boolean isFirstCapSlashCase()            { return only(SLASH | LOWER | UPPER | DIGITS) && rest(SLASH | LOWER) && first(UPPER); }

    // these check if only valid and upper case chars are present and first is uppercase letter
    public boolean isScreamingSnakeCase()           { return only(UNDER | UPPER | DIGITS) && all(UNDER | UPPER) && first(UNDER | UPPER); }
    public boolean isScreamingDashCase()            { return only(DASH | UPPER | DIGITS) && all(DASH | UPPER) && first(DASH | UPPER); }
    public boolean isScreamingDotCase()             { return only(DOT | UPPER | DIGITS) && all(DOT | UPPER) && first(DOT | UPPER); }
    public boolean isScreamingSlashCase()           { return only(SLASH | UPPER | DIGITS) && all(SLASH | UPPER) && first(SLASH | UPPER); }

    // these check if only valid and lower case chars are present and first is uppercase letter
    public boolean isSnakeCase()                    { return only(UNDER | LOWER | DIGITS) && all(UNDER | LOWER) && first(UNDER | LOWER); }
    public boolean isDashCase()                     { return only(DASH | LOWER | DIGITS) && all(DASH | LOWER) && first(DASH | LOWER); }
    public boolean isDotCase()                      { return only(DOT | LOWER | DIGITS) && all(DOT | LOWER) && first(DOT | LOWER); }
    public boolean isSlashCase()                    { return only(SLASH | LOWER | DIGITS) && all(SLASH | LOWER) && first(SLASH | LOWER); }
    public boolean isCamelCase()                    { return only(LOWER | UPPER | DIGITS) && all(LOWER | UPPER) && first(LOWER | UPPER); }
    public boolean isProperCamelCase()              { return isCamelCase() && first(LOWER); }
    public boolean isPascalCase()                   { return isCamelCase() && first(UPPER) && second(LOWER); }

    // generic checks
    public boolean hasNoUpperCase()                 { return none(UPPER | EMPTY); }
    public boolean hasNoLowerCase()                 { return none(LOWER | EMPTY); }
    public boolean hasUpperCase()                   { return some(UPPER); }
    public boolean hasLowerCase()                   { return some(LOWER); }
    public boolean hasLowerCaseOrUpperCase()        { return some(LOWER | UPPER); }
    public boolean isLowerCase()                    { return is(LOWER); }
    public boolean isUpperCase()                    { return is(UPPER); }
    // @formatter:on

    public String makeMixedSeparatorCase(char separator) {
        StringBuilder sb = new StringBuilder();
        int iMax = myWord.length();
        boolean removeSeparator = (flags(separator) & mySeparators) != 0;
        boolean wasLower = false;

        for (int i = 0; i < iMax; i++) {
            char c = myWord.charAt(i);
            if (Character.isUpperCase(c)) {
                if (wasLower) sb.append(separator);
                sb.append(c);
                wasLower = false;
            } else {
                if (Character.isLowerCase(c)) {
                    wasLower = true;
                    sb.append(c);
                } else {
                    wasLower = false;
                    if ((mySeparators & flags(c)) != 0) {
                        sb.append(separator);
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.toString();
    }

    public static String makeProperFirstCapCase(String s) {
        return s.length() == 0 ? "" : s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public String makeMixedSnakeCase() {
        return makeMixedSeparatorCase('_');
    }

    public String makeMixedDotCase() {
        return makeMixedSeparatorCase('.');
    }

    public String makeMixedDashCase() {
        return makeMixedSeparatorCase('-');
    }

    public String makeMixedSlashCase() {
        return makeMixedSeparatorCase('/');
    }

    public String makeFirstCapSnakeCase() {
        return makeProperFirstCapCase(makeMixedSeparatorCase('_'));
    }

    public String makeFirstCapDotCase() {
        return makeProperFirstCapCase(makeMixedSeparatorCase('.'));
    }

    public String makeFirstCapDashCase() {
        return makeProperFirstCapCase(makeMixedSeparatorCase('-'));
    }

    public String makeFirstCapSlashCase() {
        return makeProperFirstCapCase(makeMixedSeparatorCase('/'));
    }

    public String makeCamelCase() {
        StringBuilder sb = new StringBuilder();
        if (some(mySeparators)) {
            int iMax = myWord.length();
            boolean toUpper = false;

            for (int i = 0; i < iMax; i++) {
                char c = myWord.charAt(i);
                if ((mySeparators & flags(c)) != 0) {
                    toUpper = true;
                } else {
                    if (toUpper) sb.append(Character.toUpperCase(c));
                    else sb.append(Character.toLowerCase(c));
                    toUpper = false;
                }
            }
        } else if (only(UPPER | DIGITS) && first(UPPER)) {
            if (myWord.length() != 0) {
                sb.append(myWord.charAt(0));
                sb.append(myWord.toString().substring(1).toLowerCase());
            }
        } else {
            sb.append(myWord);
        }
        return sb.toString();
    }

    public String makeProperCamelCase() {
        String s = makeCamelCase();
        return s.length() == 0 ? "" : s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    public String makePascalCase() {
        String s = makeCamelCase();
        return s.length() == 0 ? "" : s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public String makeScreamingSnakeCase() {
        return makeMixedSnakeCase().toUpperCase();
    }

    public String makeSnakeCase() {
        return makeMixedSnakeCase().toLowerCase();
    }

    public String makeScreamingDashCase() {
        return makeMixedDashCase().toUpperCase();
    }

    public String makeDashCase() {
        return makeMixedDashCase().toLowerCase();
    }

    public String makeScreamingDotCase() {
        return makeMixedDotCase().toUpperCase();
    }

    public String makeDotCase() {
        return makeMixedDotCase().toLowerCase();
    }

    public String makeScreamingSlashCase() {
        return makeMixedSlashCase().toUpperCase();
    }

    public String makeSlashCase() {
        return makeMixedSlashCase().toLowerCase();
    }

    public boolean canBeMixedSnakeCase() {
        if (only(UNDER | UPPER | LOWER | DIGITS) && some(LOWER | UPPER) && first(UNDER | LOWER | UPPER)) {
            String word = makeMixedSnakeCase();
            return !myWord.equals(word) && StudiedWord.of(word, mySeparators).isMixedSnakeCase();
        }
        return false;
    }

    public boolean canBeFirstCapSnakeCase() {
        if (only(UNDER | UPPER | LOWER | DIGITS) && some(LOWER | UPPER) && first(UNDER | LOWER | UPPER)) {
            String word = makeFirstCapSnakeCase();
            return !myWord.equals(word) && StudiedWord.of(word, mySeparators).isFirstCapSnakeCase();
        }
        return false;
    }

    public boolean canBeScreamingSnakeCase() {
        if (only(UNDER | UPPER | LOWER | DIGITS) && some(LOWER | UPPER) && first(UNDER | LOWER | UPPER)) {
            String word = makeScreamingSnakeCase();
            return !myWord.equals(word) && StudiedWord.of(word, mySeparators).isScreamingSnakeCase();
        }
        return false;
    }

    public boolean canBeSnakeCase() {
        if (only(UNDER | UPPER | LOWER | DIGITS) && some(LOWER | UPPER) && first(UNDER | LOWER | UPPER)) {
            String word = makeSnakeCase();
            return !myWord.equals(word) && StudiedWord.of(word, mySeparators).isSnakeCase();
        }
        return false;
    }

    public boolean canBeMixedDashCase() {
        if (only(DASH | UPPER | LOWER | DIGITS) && some(LOWER | UPPER) && first(DASH | LOWER | UPPER)) {
            String word = makeMixedDashCase();
            return !myWord.equals(word) && StudiedWord.of(word, mySeparators).isMixedDashCase();
        }
        return false;
    }

    public boolean canBeFirstCapDashCase() {
        if (only(DASH | UPPER | LOWER | DIGITS) && some(LOWER | UPPER) && first(DASH | LOWER | UPPER)) {
            String word = makeMixedDashCase();
            return !myWord.equals(word) && StudiedWord.of(word, mySeparators).isFirstCapDashCase();
        }
        return false;
    }

    public boolean canBeScreamingDashCase() {
        if (only(DASH | UPPER | LOWER | DIGITS) && some(LOWER | UPPER) && first(DASH | LOWER | UPPER)) {
            String word = makeScreamingDashCase();
            return !myWord.equals(word) && StudiedWord.of(word, mySeparators).isScreamingDashCase();
        }
        return false;
    }

    public boolean canBeDashCase() {
        if (only(DASH | UPPER | LOWER | DIGITS) && some(LOWER | UPPER) && first(DASH | LOWER | UPPER)) {
            String word = makeDashCase();
            return !myWord.equals(word) && StudiedWord.of(word, mySeparators).isDashCase();
        }
        return false;
    }

    public boolean canBeMixedDotCase() {
        if (only(DOT | UPPER | LOWER | DIGITS) && some(LOWER | UPPER) && first(DOT | LOWER | UPPER)) {
            String word = makeMixedDotCase();
            return !myWord.equals(word) && StudiedWord.of(word, mySeparators).isMixedDotCase();
        }
        return false;
    }

    public boolean canBeFirstCapDotCase() {
        if (only(DOT | UPPER | LOWER | DIGITS) && some(LOWER | UPPER) && first(DOT | LOWER | UPPER)) {
            String word = makeMixedDotCase();
            return !myWord.equals(word) && StudiedWord.of(word, mySeparators).isFirstCapDotCase();
        }
        return false;
    }

    public boolean canBeScreamingDotCase() {
        if (only(DOT | UPPER | LOWER | DIGITS) && some(LOWER | UPPER) && first(DOT | LOWER | UPPER)) {
            String word = makeScreamingDotCase();
            return !myWord.equals(word) && StudiedWord.of(word, mySeparators).isScreamingDotCase();
        }
        return false;
    }

    public boolean canBeDotCase() {
        if (only(DOT | UPPER | LOWER | DIGITS) && some(LOWER | UPPER) && first(DOT | LOWER | UPPER)) {
            String word = makeDotCase();
            return !myWord.equals(word) && StudiedWord.of(word, mySeparators).isDotCase();
        }
        return false;
    }

    public boolean canBeMixedSlashCase() {
        if (only(SLASH | UPPER | LOWER | DIGITS) && some(LOWER | UPPER) && first(SLASH | LOWER | UPPER)) {
            String word = makeMixedSlashCase();
            return !myWord.equals(word) && StudiedWord.of(word, mySeparators).isMixedSlashCase();
        }
        return false;
    }

    public boolean canBeFirstCapSlashCase() {
        if (only(SLASH | UPPER | LOWER | DIGITS) && some(LOWER | UPPER) && first(SLASH | LOWER | UPPER)) {
            String word = makeMixedSlashCase();
            return !myWord.equals(word) && StudiedWord.of(word, mySeparators).isFirstCapSlashCase();
        }
        return false;
    }

    public boolean canBeScreamingSlashCase() {
        if (only(SLASH | UPPER | LOWER | DIGITS) && some(LOWER | UPPER) && first(SLASH | LOWER | UPPER)) {
            String word = makeScreamingSlashCase();
            return !myWord.equals(word) && StudiedWord.of(word, mySeparators).isScreamingSlashCase();
        }
        return false;
    }

    public boolean canBeSlashCase() {
        if (only(SLASH | UPPER | LOWER | DIGITS) && some(LOWER | UPPER) && first(SLASH | LOWER | UPPER)) {
            String word = makeSlashCase();
            return !myWord.equals(word) && StudiedWord.of(word, mySeparators).isSlashCase();
        }
        return false;
    }

    public boolean canBeCamelCase() {
        if (only(UNDER | UPPER | LOWER | DIGITS) && some(LOWER | UPPER) && first(UNDER | LOWER | UPPER)) {
            String word = makeCamelCase();
            return !myWord.equals(word) && StudiedWord.of(word, mySeparators).isCamelCase();
        }
        return false;
    }

    public boolean canBeProperCamelCase() {
        if (only(UNDER | UPPER | LOWER | DIGITS) && some(LOWER | UPPER) && first(UNDER | LOWER | UPPER)) {
            String word = makeProperCamelCase();
            return !myWord.equals(word) && StudiedWord.of(word, mySeparators).isProperCamelCase();
        }
        return false;
    }

    public boolean canBePascalCase() {
        if (only(UNDER | UPPER | LOWER | DIGITS) && some(LOWER | UPPER) && first(UNDER | LOWER | UPPER)) {
            String word = makePascalCase();
            return !myWord.equals(word) && StudiedWord.of(word, mySeparators).isPascalCase();
        }
        return false;
    }
}
