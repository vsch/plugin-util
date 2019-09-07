/*
 *
 */
package com.vladsch.plugin.util;

import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Version implements Comparable<Version> {
    public final int major;
    public final int minor;
    public final int bugfix;
    public final int eap;

    public Version(int major, int minor, int bugfix, int eap) {
        this.eap = eap;
        this.bugfix = bugfix;
        this.minor = minor;
        this.major = major;
    }

    public Version(int[] build) {
        this.major = build.length > 0 ? build[0] : 0;
        this.minor = build.length > 1 ? build[1] : 0;
        this.bugfix = build.length > 2 ? build[2] : 0;
        this.eap = build.length > 3 ? build[3] : 0;
    }

    @Nullable
    public static Version parseVersion(@NotNull String versionString) {
        String[] versions = versionString.split("\\.");
        String version = versions[0];
        int major = parseNumber(version, -1);
        if (major < 0) {
            return null;
        }

        int minor = versions.length > 1 ? parseNumber(versions[1], 0) : 0;
        if (minor < 0) {
            return new Version(major, 0, 0, 0);
        }

        int patch = versions.length > 2 ? parseNumber(versions[2], 0) : 0;
        if (patch < 0) {
            return new Version(major, minor, 0, 0);
        }

        int eap = versions.length > 3 ? parseNumber(versions[3], 0) : 0;
        if (eap < 0) {
            return new Version(major, minor, patch, 0);
        }

        return new Version(major, minor, patch, eap);
    }

    private static int parseNumber(String num, int def) {
        return StringUtil.parseInt(num.replaceFirst("(\\d+).*", "$1"), def);
    }

    public boolean is(@Nullable Integer major) {
        return is(major, null);
    }

    public boolean is(@Nullable Integer major, @Nullable Integer minor) {
        return is(major, minor, null);
    }

    public boolean is(@Nullable Integer major, @Nullable Integer minor, @Nullable Integer bugfix) {
        return is(major, minor, bugfix, null);
    }

    public boolean is(@Nullable Integer major, @Nullable Integer minor, @Nullable Integer bugfix, @Nullable Integer eap) {
        return compareTo(major, minor, bugfix, eap) == 0;
    }

    public boolean isOrGreaterThan(@Nullable Integer major) {
        return isOrGreaterThan(major, null);
    }

    public boolean isOrGreaterThan(@Nullable Integer major, @Nullable Integer minor) {
        return isOrGreaterThan(major, minor, null);
    }

    public boolean isOrGreaterThan(@Nullable Integer major, @Nullable Integer minor, @Nullable Integer bugfix) {
        return isOrGreaterThan(major, minor, bugfix, null);
    }

    public boolean isOrGreaterThan(@Nullable Integer major, @Nullable Integer minor, @Nullable Integer bugfix, @Nullable Integer eap) {
        return compareTo(major, minor, bugfix, eap) >= 0;
    }

    public boolean lessThan(@Nullable Integer major) {
        return lessThan(major, null);
    }

    public boolean lessThan(@Nullable Integer major, @Nullable Integer minor) {
        return lessThan(major, minor, null);
    }

    public boolean lessThan(@Nullable Integer major, @Nullable Integer minor, @Nullable Integer bugfix) {
        return lessThan(major, minor, bugfix, null);
    }

    public boolean lessThan(@Nullable Integer major, @Nullable Integer minor, @Nullable Integer bugfix, @Nullable Integer eap) {
        return compareTo(major, minor, bugfix, eap) < 0;
    }

    @Override
    public int compareTo(@NotNull Version version) {
        return compareTo(version.major, version.minor, version.bugfix, version.eap);
    }

    public int compareTo(@Nullable Integer major) {
        return compareTo(major, null);
    }

    public int compareTo(@Nullable Integer major, @Nullable Integer minor) {
        return compareTo(major, minor, null, null);
    }

    public int compareTo(@Nullable Integer major, @Nullable Integer minor, @Nullable Integer bugfix, @Nullable Integer eap) {
        int result = doCompare(this.major, major);
        if (result != 0) return result;

        result = doCompare(this.minor, minor);
        if (result != 0) return result;

        result = doCompare(this.bugfix, bugfix);
        if (result != 0) return result;

        return doCompare(this.eap, eap);
    }

    private static int doCompare(Integer l, Integer r) {
        if (l == null || r == null) return 0;
        return l - r;
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + bugfix + (eap > 0 ? "." + eap : "");
    }

    /**
     * @return compact string representation in the following form: "n.n", "n.n.n", e.g 1.0, 1.1.0
     */
    public String toCompactString() {
        return toCompactString(major, minor, bugfix);
    }

    public static String toCompactString(int major, int minor, int bugfix) {
        String res = major + "." + minor;
        if (bugfix > 0) res += "." + bugfix;
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Version version = (Version) o;

        if (eap != version.eap) return false;
        if (bugfix != version.bugfix) return false;
        if (major != version.major) return false;
        return minor == version.minor;
    }

    @Override
    public int hashCode() {
        int result = major;
        result = 31 * result + minor;
        result = 31 * result + bugfix;
        result = 31 * result + eap;
        return result;
    }
}
