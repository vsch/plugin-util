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
import com.intellij.openapi.util.BuildNumber;
import com.intellij.openapi.util.Version;

@SuppressWarnings("WeakerAccess")
public class AppUtils {
    public static final String PARAMETER_HINTS_APP_VERSION = "163.3512";
    public static final String PARAMETER_HINTS_FORCE_UPDATE_APP_VERSION = "172.1909";
    
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
            Version version = new Version(buildComponents[0], buildComponents[1], buildComponents[2]);
            return version.isOrGreaterThan(requiredVersion.major, requiredVersion.minor, requiredVersion.bugfix);
        }
        return false;
    }
}
