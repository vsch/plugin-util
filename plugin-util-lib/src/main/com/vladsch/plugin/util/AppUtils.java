package com.vladsch.plugin.util;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ex.ApplicationInfoEx;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.BuildNumber;
import com.intellij.util.ui.UIUtil;
import com.vladsch.flexmark.util.html.ui.BackgroundColor;

import java.util.HashMap;
import java.util.HashSet;

import static com.intellij.openapi.diagnostic.Logger.getInstance;
import static com.vladsch.plugin.util.ui.Helpers.errorColor;
import static com.vladsch.plugin.util.ui.Helpers.warningColor;

@SuppressWarnings("WeakerAccess")
public class AppUtils {
    static final Logger LOG = getInstance("com.vladsch.plugin.util");

    public static final String PARAMETER_HINTS_APP_VERSION = "163.3512";
    public static final String PARAMETER_HINTS_FORCE_UPDATE_APP_VERSION = "172.1909";
    public static final String LOADS_SVG_ICONS_APP_VERSION = "180";
    public static final String CLIPBOARD_CHANGE_NOTIFICATIONS = "180";
    public static final String JETBRAINS_MARKETPLACE_APP_VERSION = "191";

    // class names which were determined by their version to be services not components
    private static final HashSet<String> APP_SERVICES = new HashSet<>();
    // application components which are now services starting with given version
    private static final HashMap<String, String> APP_COMPONENT_SERVICES = new HashMap<>();
    static {
        APP_COMPONENT_SERVICES.put("com.intellij.diagnostic.DebugLogManager", "193.5662.15");
    }
    public static <T> T getApplicationComponentOrService(Class<T> componentClass) {
        Application application = ApplicationManager.getApplication();
        if (application != null) {
            if (APP_SERVICES.contains(componentClass.getName())) {
                return ApplicationManager.getApplication().getService(componentClass);
            } else {
                String serviceAppVersion = APP_COMPONENT_SERVICES.get(componentClass.getName());

                if (serviceAppVersion == null || !isAppVersionEqualOrGreaterThan(serviceAppVersion, true)) {
                    return application.getComponent(componentClass);
                } else {
                    APP_SERVICES.add(componentClass.getName());
                    return ApplicationManager.getApplication().getService(componentClass);
                }
            }
        }
        return null;
    }

    public static boolean isClipboardChangeNotificationsAvailable() {
        boolean available = isAppVersionEqualOrGreaterThan(CLIPBOARD_CHANGE_NOTIFICATIONS, true);
        LOG.info("ClipboardChangeNotifications " + available);
        return available;
    }

    public static boolean isSvgLoadIconAvailable() {
        boolean available = isAppVersionEqualOrGreaterThan(LOADS_SVG_ICONS_APP_VERSION, true);
        LOG.info("SvgIconsAvailable " + available);
        return available;
    }

    public static boolean isParameterHintsAvailable() {
        return isAppVersionEqualOrGreaterThan(PARAMETER_HINTS_APP_VERSION, true);
    }

    public static boolean isParameterHintsForceUpdateAvailable() {
        return isAppVersionEqualOrGreaterThan(PARAMETER_HINTS_FORCE_UPDATE_APP_VERSION, true);
    }

    /**
     * See if app version is equal or greater than given
     *
     * @param requiredAppVersion required app version in format major.minor.bugfix
     * @param defaultUnderTest   default value to use if running under test
     *
     * @return true if app version is equal or greater than given
     *
     * @deprecated Use #isAppVersionEqualOrGreaterThan
     */
    @Deprecated
    public static boolean isAppVersionGreaterThan(String requiredAppVersion, boolean defaultUnderTest) {
        return isAppVersionEqualOrGreaterThan(requiredAppVersion, defaultUnderTest);
    }

    /**
     * See if app version is equal or greater than given
     *
     * @param requiredAppVersion required app version in format major.minor.bugfix
     * @param defaultUnderTest   default value to use if running under test
     *
     * @return true if app version is equal or greater than given
     */
    public static boolean isAppVersionEqualOrGreaterThan(String requiredAppVersion, boolean defaultUnderTest) {
        Application application = ApplicationManager.getApplication();
        if (application != null && !application.isUnitTestMode()) {
            BuildNumber build = ApplicationInfoEx.getInstance().getBuild();
            Version requiredVersion = Version.parseVersion(requiredAppVersion);
            if (build != null && requiredVersion != null) {
                int[] buildComponents = build.getComponents();
                Version version = new Version(buildComponents);
                return version.isOrGreaterThan(requiredVersion.major, requiredVersion.minor, requiredVersion.bugfix);
            }
            return false;
        }
        return defaultUnderTest;
    }

    public static boolean isCommunity(boolean defaultUnderTest) {
        Application application = ApplicationManager.getApplication();
        if (application != null && !application.isUnitTestMode()) {
            String build = ApplicationInfoEx.getInstance().getBuild().toString();
            return build.startsWith("IC-") || build.startsWith("PC-") || build.startsWith("IE-") || build.startsWith("PE-");
        }
        return defaultUnderTest;
    }

    public static int compareVersions(String versionString1, String versionString2) {
        Version version1 = versionString1 == null ? null : Version.parseVersion(versionString1);
        Version version2 = versionString2 == null ? null : Version.parseVersion(versionString2);
        if (version1 != null && version2 != null) {
            return version1.compareTo(version2);
        } else if (version1 != null) {
            return 1;
        } else if (version2 != null) {
            return -1;
        }
        return 0;
    }

    public static BackgroundColor getInvalidTextFieldBackground() {
        return BackgroundColor.of(errorColor(UIUtil.getTextFieldBackground()));
    }

    public static BackgroundColor getWarningTextFieldBackground() {
        return BackgroundColor.of(warningColor(UIUtil.getTextFieldBackground()));
    }

    public static BackgroundColor getValidTextFieldBackground() {
        return BackgroundColor.of(UIUtil.getTextFieldBackground());
    }

    public static BackgroundColor getInvalidTableBackground(boolean isSelected) {
        return BackgroundColor.of(errorColor(getTableBackground(isSelected)));
    }

    public static BackgroundColor getTableBackground(boolean isSelected) {
        return BackgroundColor.of(!isSelected ? UIUtil.getTableBackground() : UIUtil.getTableSelectionBackground(true));
    }
}
