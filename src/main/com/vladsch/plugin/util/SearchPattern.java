package com.vladsch.plugin.util;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class SearchPattern {
    private final @NotNull String myText;
    private final boolean myIsRegex;
    private Pattern myPattern;

    public SearchPattern(final @NotNull String text, final boolean isRegex) {
        myText = text;
        myIsRegex = isRegex;
    }

    @NotNull
    public String getText() {
        return myText;
    }

    public boolean isRegex() {
        return myIsRegex;
    }

    public String getPatternText() {
        return getPatternText(myText, myIsRegex, true);
    }

    public String getPatternText(boolean caseSensitive) {
        return getPatternText(myText, myIsRegex, caseSensitive);
    }

    public static String getPatternText(String text, boolean isRegex, boolean caseSensitive) {
        return isRegex ? (caseSensitive ? text : String.format("(?i:%s)", text)) : caseSensitive ? String.format("\\Q%s\\E", text) : String.format("(?i:\\Q%s\\E)", text);
    }

    public Pattern getPattern() {
        if (myPattern == null) {
            myPattern = Pattern.compile(getPatternText());
        }
        return myPattern;
    }
}
