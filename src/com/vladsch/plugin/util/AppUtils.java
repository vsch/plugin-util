package com.vladsch.plugin.util;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ex.ApplicationInfoEx;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.BuildNumber;

import static com.intellij.openapi.diagnostic.Logger.getInstance;

@SuppressWarnings("WeakerAccess")
public class AppUtils {
    static final Logger LOG = getInstance("com.vladsch.plugin.util");

    public static final String PARAMETER_HINTS_APP_VERSION = "163.3512";
    public static final String PARAMETER_HINTS_FORCE_UPDATE_APP_VERSION = "172.1909";
    public static final String LOADS_SVG_ICONS_APP_VERSION = "180";
    public static final String CLIPBOARD_CHANGE_NOTIFICATIONS = "180";

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
     * @param requiredAppVersion
     * @param defaultUnderTest
     * @deprecated Use #isAppVersionEqualOrGreaterThan
     */
    @Deprecated
    public static boolean isAppVersionGreaterThan(String requiredAppVersion, boolean defaultUnderTest) {
        return isAppVersionEqualOrGreaterThan(requiredAppVersion, defaultUnderTest);
    }

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
}
