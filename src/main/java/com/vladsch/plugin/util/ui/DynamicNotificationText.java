package com.vladsch.plugin.util.ui;

import com.intellij.notification.Notification;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DynamicNotificationText {
    private final ArrayList<DynamicNotificationItem> myItems;

    public DynamicNotificationText() {
        myItems = new ArrayList<>();
    }

    public DynamicNotificationText add(@Nullable final String placeholderText, @Nullable final Supplier<String> dynamicText, @Nullable final String linkText, @Nullable final Consumer<Notification> linkAction) {
        myItems.add(new DynamicNotificationItem(placeholderText, dynamicText, linkText, linkAction));
        return this;
    }

    public DynamicNotificationText addText(@NotNull final String placeholderText, @NotNull final Supplier<String> dynamicText) {
        myItems.add(new DynamicNotificationItem(placeholderText, dynamicText));
        return this;
    }

    public DynamicNotificationText addLink(@NotNull final String linkText, @NotNull final Consumer<Notification> linkAction) {
        myItems.add(new DynamicNotificationItem(null, null, linkText, linkAction));
        return this;
    }

    public String replaceText(String text) {
        String result = text;
        for (DynamicNotificationItem item : myItems) {
            if (item.getPlaceholderText() != null && item.getDynamicText() != null) {
                String resultText = item.getDynamicText().get();
                result = result.replace(item.getPlaceholderText(), resultText);
            }
        }
        return result;
    }

    public boolean linkAction(@NotNull Notification notification, @NotNull String linkText) {
        for (DynamicNotificationItem item : myItems) {
            if (linkText.equals(item.getLinkText()) && item.getLinkAction() != null) {
                item.getLinkAction().accept(notification);
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("WeakerAccess")
    private static class DynamicNotificationItem {
        private final @Nullable String myPlaceholderText;
        private final @Nullable Supplier<String> myDynamicText;
        private final @Nullable String myLinkText;
        private final @Nullable Consumer<Notification> myLinkAction;

        DynamicNotificationItem(
                @Nullable final String placeholderText,
                @Nullable final Supplier<String> dynamicText,
                @Nullable final String linkText,
                @Nullable final Consumer<Notification> linkAction
        ) {
            myPlaceholderText = placeholderText;
            myDynamicText = dynamicText;
            myLinkText = linkText;
            myLinkAction = linkAction;
        }

        public DynamicNotificationItem(final String placeholderText, final Supplier<String> dynamicText) {
            this(placeholderText, dynamicText, null, null);
        }

        DynamicNotificationItem(@Nullable final String linkText, @Nullable final Consumer<Notification> linkAction) {
            this(null, null, linkText, linkAction);
        }

        @Nullable
        public String getPlaceholderText() {
            return myPlaceholderText;
        }

        @Nullable
        public Supplier<String> getDynamicText() {
            return myDynamicText;
        }

        @Nullable
        public String getLinkText() {
            return myLinkText;
        }

        @Nullable
        public Consumer<Notification> getLinkAction() {
            return myLinkAction;
        }
    }
}
