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

import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.vladsch.flexmark.util.misc.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class TypedRangeHighlightProviderBase<R, T> extends HighlightProviderBase<T> implements TypedRangeHighlightProvider<R, T> {
    @Nullable private Map<R, Integer> myHighlightRangeFlags;
    @Nullable private Map<R, Integer> myOriginalIndexMap;
    private int myOriginalOrderIndex = 0;

    public TypedRangeHighlightProviderBase(@NotNull T settings) {
        super(settings);
    }

    @Nullable
    protected HashMap<R, Pair<Integer, Integer>> getHighlightState() {
        if (myHighlightRangeFlags != null && myOriginalIndexMap != null) {
            HashMap<R, Pair<Integer, Integer>> state = new HashMap<>(myHighlightRangeFlags.size());
            for (R key : myHighlightRangeFlags.keySet()) {
                state.put(key, Pair.of(myHighlightRangeFlags.get(key), myOriginalIndexMap.get(key)));
            }
            return state;
        }
        return null;
    }

    /**
     * Restore highlight state
     *
     * @param state highlight state previously returned by {@link #getHighlightState()}
     */
    protected void setHighlightState(@NotNull Map<R, Pair<Integer, Integer>> state) {
        clearHighlightsRaw();
        myHighlightRangeFlags = new LinkedHashMap<>();
        myOriginalIndexMap = new LinkedHashMap<>();

        for (R key : state.keySet()) {
            Pair<Integer, Integer> pair = state.get(key);
            if (pair != null && pair.getFirst() != null && pair.getSecond() != null) {
                int flags = pair.getFirst();
                int originalIndex = pair.getSecond();

                // call add highlight range to ensure sub-classes build their structures from saved data
                addHighlightRange(key, flags, originalIndex);
            }
        }
    }

    @Override
    public void disposeComponent() {
        clearHighlights();
        super.disposeComponent();
    }

    @Nullable
    protected Map<R, Integer> getOriginalIndexMap() {
        return myOriginalIndexMap;
    }

    protected int getOriginalOrderIndex() {
        return myOriginalOrderIndex;
    }

    public void clearHighlightsRaw() {
        myHighlightRangeFlags = null;
        myOriginalIndexMap = null;
        myOriginalOrderIndex = 0;
    }

    @Override
    public void clearHighlights() {
        clearHighlightsRaw();
        fireHighlightsChanged();
    }

    @Override
    public boolean isRangeHighlighted(R range) {
        return myHighlightRangeFlags != null && myHighlightRangeFlags.containsKey(range);
    }

    @Override
    @Nullable
    public Map<R, Integer> getHighlightRangeFlags() {
        return myHighlightRangeFlags;
    }

    @Override
    @Nullable
    public Map<R, Integer> getHighlightRangeIndices() {
        return myOriginalIndexMap;
    }

    @Override
    public boolean isShowHighlights() {
        return isHighlightsMode() && myHighlightRangeFlags != null && !myHighlightRangeFlags.isEmpty();
    }

    /**
     * Must call getHighlightPattern() before calling this function for the first time to ensure
     * the cached structures are updated.
     *
     * @param index           highlighted range index
     * @param flags
     * @param startOffset     start offset in editor
     * @param endOffset       end offset in editor
     * @param foregroundColor
     * @param effectColor
     * @param effectType
     * @param fontType
     *
     * @return text attributes to use for highlight or null if not highlighted
     */
    @Override
    @Nullable
    public TextAttributes getHighlightAttributes(final int index, final int flags, final int startOffset, final int endOffset, final @Nullable Color foregroundColor, final @Nullable Color effectColor, final @Nullable EffectType effectType, final int fontType) {
        if (index >= 0) {
            TextAttributes ideAttributes = getIdeAttributes(flags);

            if (ideAttributes != null) {
                return ideAttributes;
            } else if (myHighlightColors != null) {
                int colorRepeatIndex = myHighlightColorRepeatIndex;
                int colorRepeatSteps = myHighlightColors.length - colorRepeatIndex;
                return new TextAttributes(foregroundColor,
                        myHighlightColors[index < colorRepeatIndex ? index : colorRepeatIndex + ((index - colorRepeatIndex) % colorRepeatSteps)],
                        effectColor,
                        effectType,
                        fontType
                );
            }
        }
        return null;
    }

    @Nullable
    public static TextAttributes getIdeAttributes(final int flags) {
        EditorColorsScheme uiTheme = EditorColorsManager.getInstance().getGlobalScheme();
        IdeHighlight highlight = IdeHighlight.get(flags);
        return highlight.attributesKey == null ? null : uiTheme.getAttributes(highlight.attributesKey);
    }

    /**
     * Must call getHighlightPattern() before calling this function for the first time to ensure
     * the cached structures are updated.
     *
     * @param range highlighted range
     *
     * @return original index of range
     */
    @Override
    public int getHighlightRangeIndex(final R range) {
        if (myOriginalIndexMap != null) {
            Integer index = myOriginalIndexMap.get(range);
            if (index != null) {
                return index;
            }
        }
        return -1;
    }

    @Override
    public int getMaxHighlightRangeIndex() {
        return myOriginalOrderIndex;
    }

    @Override
    public boolean haveHighlights() {
        return myHighlightRangeFlags != null && !myHighlightRangeFlags.isEmpty();
    }

    protected abstract void highlightRangeAdded(R range, int flags, int originalOrderIndex);

    @Override
    public int addHighlightRange(final R range, final int flags, final int orderIndex) {
        // remove and add so flags will be modified and it will be moved to the end of list (which is considered the head)
        if (myHighlightRangeFlags != null && myOriginalIndexMap != null) {
            myHighlightRangeFlags.remove(range);
            myOriginalIndexMap.remove(range);
        } else {
            myHighlightRangeFlags = new LinkedHashMap<>();
            myOriginalIndexMap = new LinkedHashMap<>();
        }

        int useOrderIndex = orderIndex;
        if (useOrderIndex < 0) {
            useOrderIndex = myOriginalOrderIndex;
            if (!isInHighlightSet()) myOriginalOrderIndex++;
        } else {
            myOriginalOrderIndex = Math.max(myOriginalOrderIndex, useOrderIndex + (!isInHighlightSet() ? 1 : 0));
        }

        myHighlightRangeFlags.put(range, flags);
        myOriginalIndexMap.put(range, useOrderIndex);
        highlightRangeAdded(range, flags, useOrderIndex);
        fireHighlightsChanged();
        return useOrderIndex;
    }

    @Override
    protected void skipHighlightSets(final int skipSets) {
        myOriginalOrderIndex += skipSets;
    }

    @Override
    protected void setHighlightIndex(int index) {
        myOriginalOrderIndex = index;
    }

    @Override
    protected int getHighlightIndex() {
        return myOriginalOrderIndex;
    }

    protected abstract void highlightRangeRemoved(R range);

    /**
     * NOTE: does not call getAdjustedRange() because the subclass needs to take care of all adjustments here
     *
     * @param range range to remove
     */
    @Override
    public void removeHighlightRange(R range) {
        if (myHighlightRangeFlags != null) {
            if (myHighlightRangeFlags.containsKey(range)) {
                myHighlightRangeFlags.remove(range);
                if (myOriginalIndexMap != null) myOriginalIndexMap.remove(range);
                highlightRangeRemoved(range);
                fireHighlightsChanged();
            }
        }
    }
}
