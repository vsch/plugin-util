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
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

public abstract class TextRangeHighlightProviderBase<T> extends TypedRangeHighlightProviderBase<TextRange, T> implements TextRangeHighlightProvider<T> {
    public TextRangeHighlightProviderBase(@NotNull T settings) {
        super(settings);
    }

    @Override
    public TextRangeHighlighter<T> getHighlighter(@NotNull final Editor editor) {
        return new TextRangeHighlighter<>(this, editor);
    }

    @Override
    public void disposeComponent() {
        clearHighlights();

        super.disposeComponent();
    }

    @Override
    protected void highlightRangeAdded(final TextRange range, final int flags, final int originalOrderIndex) {

    }

    @Override
    protected void highlightRangeRemoved(final TextRange range) {

    }

    @NotNull
    @Override
    public TextRange getAdjustedRange(@NotNull TextRange range) {
        return range;
    }
}
