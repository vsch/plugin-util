/*
 * Copyright (c) 2016-2018 Vladimir Schneider <vladimir.schneider@gmail.com>
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.vladsch.plugin.util.ui.highlight;

import com.intellij.openapi.editor.Editor;
import com.vladsch.flexmark.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class WordHighlightProviderBase<T> extends TypedRangeHighlightProviderBase<String, T> implements WordHighlightProvider<T> {
    public static final String[] EMPTY_STRINGS = new String[0];
    final private @NotNull Map<String, LinkedHashSet<String>> myHighlightCaseSensitiveWords = new HashMap<>();
    final private @NotNull Map<String, LinkedHashSet<String>> myHighlightCaseUnspecifiedWords = new HashMap<>();
    final private @NotNull Set<String> myHighlightCaseInsensitiveWords = new HashSet<>();

    private boolean myHighlightCaseSensitive = true;
    private boolean myHighlightWordsMatchBoundary = true;
    private @Nullable Pattern myHighlightPattern;

    public WordHighlightProviderBase(@NotNull T settings) {
        super(settings);
    }

    @Override
    public WordHighlighter<T> getHighlighter(@NotNull final Editor editor) {
        return new WordHighlighter<>(this, editor);
    }

    @Nullable
    protected HashMap<String, Pair<Integer, Integer>> getHighlightState() {
        return super.getHighlightState();
    }

    @Override
    protected void setHighlightState(Map<String, Pair<Integer, Integer>> state) {
        super.setHighlightState(state);
    }

    @Override
    public void disposeComponent() {
        clearHighlightsRaw();

        super.disposeComponent();
    }

    @Override
    public void clearHighlightsRaw() {
        super.clearHighlightsRaw();

        myHighlightCaseSensitiveWords.clear();
        myHighlightCaseInsensitiveWords.clear();
        myHighlightCaseUnspecifiedWords.clear();

        myHighlightPattern = null;
    }

    @Override
    public void clearHighlights() {
        clearHighlightsRaw();
        fireHighlightsChanged();
    }

    @Nullable
    public Pattern getHighlightPattern() {
        updateHighlightPattern();
        return myHighlightPattern;
    }

    @Override
    final public boolean isRangeHighlighted(String range) {
        return super.isRangeHighlighted(getAdjustedRange(range));
    }

    /**
     * Must call getHighlightPattern() before calling this function for the first time to ensure
     * the cached structures are updated.
     *
     * @param range highlighted word
     *
     * @return original index of word
     */
    @Override
    public int getHighlightRangeIndex(final String range) {
        return super.getHighlightRangeIndex(getAdjustedRange(range));
    }

    @Override
    public boolean isHighlightCaseSensitive() {
        return myHighlightCaseSensitive;
    }

    @Override
    public void setHighlightCaseSensitive(final boolean highlightCaseSensitive) {
        if (myHighlightCaseSensitive != highlightCaseSensitive) {
            myHighlightCaseSensitive = highlightCaseSensitive;
            myHighlightPattern = null;

            if (haveHighlights()) {
                fireHighlightsChanged();
            }
        }
    }

    @Override
    public boolean isHighlightWordsMatchBoundary() {
        return myHighlightWordsMatchBoundary;
    }

    @Override
    public void setHighlightWordsMatchBoundary(final boolean highlightWordsMatchBoundary) {
        if (myHighlightWordsMatchBoundary != highlightWordsMatchBoundary) {
            myHighlightWordsMatchBoundary = highlightWordsMatchBoundary;
            myHighlightPattern = null;

            if (haveHighlights()) {
                fireHighlightsChanged();
            }
        }
    }

    @NotNull
    private static String getFirstRange(@NotNull Map<String, LinkedHashSet<String>> listMap, @NotNull String lowerCase) {
        LinkedHashSet<String> ranges = listMap.get(lowerCase);
        if (ranges != null) {
            for (String range : ranges) {
                return range;
            }
        }
        return lowerCase;
    }

    private void removeHighlightRanges(@NotNull Map<String, LinkedHashSet<String>> listMap, @NotNull String lowerCase) {
        // remove all case sensitive and unspecified words matching this one
        LinkedHashSet<String> caseSensitive = listMap.remove(lowerCase);
        if (caseSensitive != null) {
            for (String word : caseSensitive) {
                super.removeHighlightRange(word);
            }
        }
    }

    private void removeHighlightRanges(@NotNull Set<String> listSet, @NotNull String lowerCase) {
        // remove all case sensitive and unspecified words matching this one
        super.removeHighlightRange(lowerCase);
        listSet.remove(lowerCase);
    }

    private static void addHighlightRanges(@NotNull Map<String, LinkedHashSet<String>> listMap, @NotNull String lowerCase, @NotNull String range) {
        // remove all case sensitive and unspecified words matching this one
        LinkedHashSet<String> list = listMap.computeIfAbsent(lowerCase, k -> new LinkedHashSet<>());
        list.remove(range);
        list.add(range);
    }

    private static void addHighlightRanges(@NotNull Set<String> listMap, @NotNull String lowerCase, @NotNull String range) {
        // remove all case sensitive and unspecified words matching this one
        listMap.remove(lowerCase);
        listMap.add(lowerCase);
    }

    @NotNull
    @Override
    public String getAdjustedRange(@NotNull String range) {
        String lowerCase = range.toLowerCase();
        if (myHighlightCaseUnspecifiedWords.containsKey(lowerCase)) {
            // have to use the first range for it
            return myHighlightCaseSensitive ? range : getFirstRange(myHighlightCaseUnspecifiedWords, lowerCase);
        } else {
            return myHighlightCaseInsensitiveWords.contains(lowerCase) ? lowerCase : range;
        }
    }

    @Override
    public int addHighlightRange(String range, int flags, int originalIndex) {
        if (WordHighlighterFlags.haveFlags(flags, WordHighlighterFlags.BEGIN_WORD) && (range.length() == 0 || range.charAt(0) == '$')) {
            flags &= ~WordHighlighterFlags.BEGIN_WORD.mask;
        }

        if (WordHighlighterFlags.haveFlags(flags, WordHighlighterFlags.END_WORD) && (range.length() == 0 || range.charAt(range.length() - 1) == '$')) {
            flags &= ~WordHighlighterFlags.END_WORD.mask;
        }

        String useRange;
        String lowerCase = range.toLowerCase();

        if (WordHighlighterFlags.haveFlags(flags, WordHighlighterFlags.CASE_SENSITIVE)) {
            // remove all case insensitive and unspecified words matching this one
            removeHighlightRanges(myHighlightCaseInsensitiveWords, lowerCase);
            removeHighlightRanges(myHighlightCaseUnspecifiedWords, lowerCase);

            addHighlightRanges(myHighlightCaseSensitiveWords, lowerCase, range);
            useRange = range;
        } else if (WordHighlighterFlags.haveFlags(flags, WordHighlighterFlags.CASE_INSENSITIVE)) {
            // remove all case sensitive and unspecified words matching this one
            removeHighlightRanges(myHighlightCaseSensitiveWords, lowerCase);
            removeHighlightRanges(myHighlightCaseUnspecifiedWords, lowerCase);

            addHighlightRanges(myHighlightCaseInsensitiveWords, lowerCase, range);
            flags &= ~WordHighlighterFlags.CASE_SENSITIVE.mask;
            useRange = lowerCase;
        } else {
            // remove all case sensitive and insensitive words matching this one
            removeHighlightRanges(myHighlightCaseInsensitiveWords, lowerCase);
            removeHighlightRanges(myHighlightCaseSensitiveWords, lowerCase);

            addHighlightRanges(myHighlightCaseUnspecifiedWords, lowerCase, range);
            useRange = range;
        }

        return super.addHighlightRange(useRange, flags, originalIndex);
    }

    @Override
    final public void removeHighlightRange(String range) {
        String lowerCase = range.toLowerCase();

        if (myHighlightCaseInsensitiveWords.contains(lowerCase)) {
            removeHighlightRanges(myHighlightCaseInsensitiveWords, lowerCase);
        } else if (myHighlightCaseSensitiveWords.containsKey(lowerCase)) {
            removeHighlightRanges(myHighlightCaseSensitiveWords, lowerCase);
        } else if (myHighlightCaseUnspecifiedWords.containsKey(lowerCase)) {
            removeHighlightRanges(myHighlightCaseUnspecifiedWords, lowerCase);
        }
    }

    @Override
    protected void highlightRangeAdded(String range, int flags, int originalOrderIndex) {
        myHighlightPattern = null;
    }

    @Override
    protected void highlightRangeRemoved(String range) {
        myHighlightPattern = null;
    }

    @Override
    public void updateHighlightPattern() {
        Map<String, Integer> highlightRangeFlags = getHighlightRangeFlags();

        if (myHighlightPattern == null && highlightRangeFlags != null && !highlightRangeFlags.isEmpty() && getOriginalIndexMap() != null) {
            StringBuilder sb = new StringBuilder();
            String sep = "";
            boolean isCaseSensitive = true;

            String[] ranges = highlightRangeFlags.keySet().toArray(EMPTY_STRINGS);

            Arrays.sort(ranges, Comparator.reverseOrder());

            for (String range : ranges) {
                sb.append(sep);
                sep = "|";

                boolean nextCaseSensitive;

                String adjustedRange = getAdjustedRange(range);

                int flags = highlightRangeFlags.get(adjustedRange);

                if (myHighlightCaseSensitive) {
                    if (WordHighlighterFlags.haveFlags(flags, WordHighlighterFlags.CASE_SENSITIVE)) nextCaseSensitive = true;
                    else nextCaseSensitive = !WordHighlighterFlags.haveFlags(flags, WordHighlighterFlags.CASE_INSENSITIVE);
                } else {
                    nextCaseSensitive = false;
                }

                if (isCaseSensitive != nextCaseSensitive) {
                    isCaseSensitive = nextCaseSensitive;
                    sb.append(isCaseSensitive ? "(?-i)" : "(?i)");
                }

                if (myHighlightWordsMatchBoundary && WordHighlighterFlags.haveFlags(flags, WordHighlighterFlags.BEGIN_WORD)) sb.append("\\b");
                sb.append("\\Q").append(range).append("\\E");
                if (myHighlightWordsMatchBoundary && WordHighlighterFlags.haveFlags(flags, WordHighlighterFlags.END_WORD)) sb.append("\\b");
            }

            myHighlightPattern = Pattern.compile(sb.toString());
        }
    }
}
