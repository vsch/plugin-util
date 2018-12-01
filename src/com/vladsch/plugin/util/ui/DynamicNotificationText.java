/*
 * Copyright (c) 2016-2018 Vladimir Schneider <vladimir.schneider@gmail.com>
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package com.vladsch.plugin.util.ui;

import com.intellij.notification.Notification;
import com.vladsch.flexmark.util.RunnableValue;
import com.vladsch.flexmark.util.ValueRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class DynamicNotificationText {
    private final ArrayList<DynamicNotificationItem> myItems;

    public DynamicNotificationText() {
        myItems = new ArrayList<>();
    }

    public DynamicNotificationText add(@Nullable final String placeholderText, @Nullable final RunnableValue<String> dynamicText, @Nullable final String linkText, @Nullable final ValueRunnable<Notification> linkAction) {
        myItems.add(new DynamicNotificationItem(placeholderText, dynamicText, linkText, linkAction));
        return this;
    }

    public DynamicNotificationText addText(@NotNull final String placeholderText, @NotNull final RunnableValue<String> dynamicText) {
        myItems.add(new DynamicNotificationItem(placeholderText, dynamicText));
        return this;
    }

    public DynamicNotificationText addLink(@NotNull final String linkText, @NotNull final ValueRunnable<Notification> linkAction) {
        myItems.add(new DynamicNotificationItem(null, null, linkText, linkAction));
        return this;
    }

    public String replaceText(String text) {
        String result = text;
        for (DynamicNotificationItem item : myItems) {
            if (item.getPlaceholderText() != null && item.getDynamicText() != null) {
                String resultText = item.getDynamicText().run();
                result = result.replace(item.getPlaceholderText(), resultText);
            }
        }
        return result;
    }

    public boolean linkAction(@NotNull Notification notification, @NotNull String linkText) {
        for (DynamicNotificationItem item : myItems) {
            if (linkText.equals(item.getLinkText()) && item.getLinkAction() != null) {
                item.getLinkAction().run(notification);
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("WeakerAccess")
    private static class DynamicNotificationItem {
        private final @Nullable String myPlaceholderText;
        private final @Nullable RunnableValue<String> myDynamicText;
        private final @Nullable String myLinkText;
        private final @Nullable ValueRunnable<Notification> myLinkAction;

        DynamicNotificationItem(
                @Nullable final String placeholderText,
                @Nullable final RunnableValue<String> dynamicText,
                @Nullable final String linkText,
                @Nullable final ValueRunnable<Notification> linkAction
        ) {
            myPlaceholderText = placeholderText;
            myDynamicText = dynamicText;
            myLinkText = linkText;
            myLinkAction = linkAction;
        }

        public DynamicNotificationItem(final String placeholderText, final RunnableValue<String> dynamicText) {
            this(placeholderText, dynamicText, null, null);
        }

        DynamicNotificationItem(@Nullable final String linkText, @Nullable final ValueRunnable<Notification> linkAction) {
            this(null, null, linkText, linkAction);
        }

        @Nullable
        public String getPlaceholderText() {
            return myPlaceholderText;
        }

        @Nullable
        public RunnableValue<String> getDynamicText() {
            return myDynamicText;
        }

        @Nullable
        public String getLinkText() {
            return myLinkText;
        }

        @Nullable
        public ValueRunnable<Notification> getLinkAction() {
            return myLinkAction;
        }
    }
}
