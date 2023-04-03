package com.vladsch.plugin.util;

import com.intellij.CommonBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.util.ResourceBundle;

public class UtilBundle {
    @NonNls
    protected static final String BUNDLE_NAME = "com.vladsch.plugin.util.localization.strings";

    protected static final ResourceBundle BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private UtilBundle() {
    }

    public static ResourceBundle getBundle() {
        return BUNDLE;
    }

    public static String getString(String key, Object... params) {
        return CommonBundle.messageOrNull(BUNDLE, key, params);
    }

    public static String message(@PropertyKey(resourceBundle = BUNDLE_NAME) String key, Object... params) {
        return CommonBundle.messageOrNull(BUNDLE, key, params);
    }

    public static String messageOrBlank(@PropertyKey(resourceBundle = BUNDLE_NAME) String key, Object... params) {
        String value = CommonBundle.messageOrNull(BUNDLE, key, params);
        return value == null ? "" : value;
    }
}
