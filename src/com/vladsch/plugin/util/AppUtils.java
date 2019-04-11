/*
 * Copyright (c) 2016-2019 Vladimir Schneider <vladimir.schneider@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.vladsch.plugin.util;

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
        boolean available = isAppVersionGreaterThan(CLIPBOARD_CHANGE_NOTIFICATIONS);
        LOG.info("ClipboardChangeNotifications " + available);
        return available;
    }

    public static boolean isSvgLoadIconAvailable() {
        boolean available = isAppVersionGreaterThan(LOADS_SVG_ICONS_APP_VERSION);
        LOG.info("SvgIconsAvailable " + available);
        return available;
    }

    public static boolean isParameterHintsAvailable() {
        return isAppVersionGreaterThan(PARAMETER_HINTS_APP_VERSION);
    }

    public static boolean isParameterHintsForceUpdateAvailable() {
        return isAppVersionGreaterThan(PARAMETER_HINTS_FORCE_UPDATE_APP_VERSION);
    }

    public static boolean isAppVersionGreaterThan(String requiredAppVersion) {
        BuildNumber build = ApplicationInfoEx.getInstance().getBuild();
        Version requiredVersion = Version.parseVersion(requiredAppVersion);
        if (build != null && requiredVersion != null) {
            int[] buildComponents = build.getComponents();
            Version version = new Version(buildComponents[0], buildComponents[1], buildComponents[2], 0);
            return version.isOrGreaterThan(requiredVersion.major, requiredVersion.minor, requiredVersion.bugfix);
        }
        return false;
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
