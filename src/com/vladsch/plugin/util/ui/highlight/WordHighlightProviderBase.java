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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public abstract class WordHighlightProviderBase<T> extends TypedRangeHighlightProviderBase<String, T> implements WordHighlightProvider<T> {
    protected boolean myHighlightWordsCaseSensitive = true;
    @Nullable protected Pattern myHighlightPattern;
    @Nullable private Map<String, Integer> myHighlightCaseSensitiveWordIndices;
    @Nullable protected Map<String, Integer> myHighlightCaseInsensitiveWordIndices;

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
        myHighlightPattern = null;
        myHighlightCaseSensitiveWordIndices = null;
        myHighlightCaseInsensitiveWordIndices = null;
    }

    @Override
    public void clearHighlights() {
        clearHighlightsRaw();
        fireHighlightsChanged();
    }

    @Override
    public boolean isRangeHighlighted(String word) {
        if (getHighlightRangeFlags() == null) return false;
        if (getHighlightRangeFlags().containsKey(word)) return true;

        if (!myHighlightWordsCaseSensitive) {
            updateHighlightPattern();
            return myHighlightCaseInsensitiveWordIndices != null && myHighlightCaseInsensitiveWordIndices.containsKey(word.toLowerCase());
        }
        return false;
    }

    @Override
    @Nullable
    public Map<String, Integer> getHighlightCaseSensitiveWordIndices() {
        return myHighlightCaseSensitiveWordIndices;
    }

    @Override
    @Nullable
    public Map<String, Integer> getHighlightCaseInsensitiveWordIndices() {
        updateHighlightPattern();
        return myHighlightCaseInsensitiveWordIndices;
    }

    @Nullable
    public Pattern getHighlightPattern() {
        updateHighlightPattern();
        return myHighlightPattern;
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
        return super.getHighlightRangeIndex(range);
    }

    @Override
    public boolean isHighlightWordsCaseSensitive() {
        return myHighlightWordsCaseSensitive;
    }

    @Override
    public void setHighlightWordsCaseSensitive(final boolean highlightWordsCaseSensitive) {
        if (myHighlightWordsCaseSensitive != highlightWordsCaseSensitive) {
            myHighlightWordsCaseSensitive = highlightWordsCaseSensitive;
            myHighlightPattern = null;
            myHighlightCaseSensitiveWordIndices = null;
            myHighlightCaseInsensitiveWordIndices = null;
            if (haveHighlights()) {
                fireHighlightsChanged();
            }
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

        myHighlightCaseInsensitiveWordIndices = null;

        return super.addHighlightRange(range, flags, originalIndex);
    }

    @Override
    final public void removeHighlightRange(String range) {
        super.removeHighlightRange(range);
    }

    @Override
    protected void highlightRangeAdded(String range, int flags, int originalOrderIndex) {
        myHighlightPattern = null;
        myHighlightCaseSensitiveWordIndices = null;
        myHighlightCaseInsensitiveWordIndices = null;
    }

    @Override
    protected void highlightRangeRemoved(String range) {
        myHighlightPattern = null;
        myHighlightCaseSensitiveWordIndices = null;
        myHighlightCaseInsensitiveWordIndices = null;
    }

    @Override
    public void updateHighlightPattern() {
        if (myHighlightPattern == null && getHighlightRangeFlags() != null && !getHighlightRangeFlags().isEmpty() && getOriginalIndexMap() != null) {
            StringBuilder sb = new StringBuilder();
            String sep = "";
            int iMax = getHighlightRangeFlags().size();
            myHighlightCaseSensitiveWordIndices = new HashMap<>(iMax);
            myHighlightCaseInsensitiveWordIndices = new HashMap<>(iMax);
            boolean isCaseSensitive = true;

            ArrayList<Entry<String, Integer>> entries = new ArrayList<>(getHighlightRangeFlags().entrySet());

            entries.sort(Comparator.comparing((entry) -> -entry.getKey().length()));

            for (Entry<String, Integer> entry : entries) {
                sb.append(sep);
                sep = "|";

                boolean nextCaseSensitive = myHighlightWordsCaseSensitive;
                if (WordHighlighterFlags.haveFlags(entry.getValue(), WordHighlighterFlags.CASE_INSENSITIVE)) {
                    nextCaseSensitive = false;
                }
                if (WordHighlighterFlags.haveFlags(entry.getValue(), WordHighlighterFlags.CASE_SENSITIVE)) {
                    nextCaseSensitive = true;
                }
                if (isCaseSensitive != nextCaseSensitive) {
                    isCaseSensitive = nextCaseSensitive;
                    sb.append(isCaseSensitive ? "(?-i)" : "(?i)");
                }

                if (WordHighlighterFlags.haveFlags(entry.getValue(), WordHighlighterFlags.BEGIN_WORD)) sb.append("\\b");
                sb.append("\\Q").append(entry.getKey()).append("\\E");
                if (WordHighlighterFlags.haveFlags(entry.getValue(), WordHighlighterFlags.END_WORD)) sb.append("\\b");

                if (isCaseSensitive) {
                    myHighlightCaseSensitiveWordIndices.put(entry.getKey(), getOriginalIndexMap().get(entry.getKey()));
                } else {
                    myHighlightCaseInsensitiveWordIndices.put(entry.getKey().toLowerCase(), getOriginalIndexMap().get(entry.getKey()));
                }
            }

            myHighlightPattern = Pattern.compile(sb.toString());
        }
    }
}
