package com.vladsch.plugin.util;

import com.intellij.BundleBase;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.util.Locale;
import java.util.ResourceBundle;

public class UtilBundle {
    @NonNls
    protected static final String BUNDLE_NAME = "com.vladsch.plugin.util.localization.strings";

    protected static final ResourceBundle BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault(), UtilBundle.class.getClassLoader());

    private UtilBundle() {
    }

    public static ResourceBundle getBundle() {
        return BUNDLE;
    }

    public static String getString(String key, Object... params) {
        return BundleBase.messageOrDefault(BUNDLE, key, null, params);
    }

    public static String message(@PropertyKey(resourceBundle = BUNDLE_NAME) String key, Object... params) {
        return BundleBase.messageOrDefault(BUNDLE, key, null, params);
    }

    public static String messageOrBlank(@PropertyKey(resourceBundle = BUNDLE_NAME) String key, Object... params) {
        return BundleBase.messageOrDefault(BUNDLE, key, "", params);
    }
}
