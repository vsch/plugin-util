package com.vladsch.plugin.util;

import com.vladsch.flexmark.util.misc.Utils;
import org.jetbrains.annotations.NotNull;

import static com.vladsch.flexmark.util.sequence.SequenceUtils.parseIntOrNull;

public class SemanticVersion implements Comparable<SemanticVersion> {
    public final Integer major;
    public final Integer minor;
    public final Integer patch;
    public final Integer tweak;
    public final String tail;
    public final boolean tailSignificant;

    public SemanticVersion(CharSequence version) {
        this(version, false);
    }

    public SemanticVersion(CharSequence version, boolean tailIsSignificant) {
        String text = String.valueOf(version);
        String[] parts = text.split("\\.", 5);
        major = parseIntOrNull(parts[0]);
        minor = parts.length > 1 ? parseIntOrNull(parts[1]) : null;
        patch = parts.length > 2 ? parseIntOrNull(parts[2]) : null;
        tweak = parts.length > 3 ? parseIntOrNull(parts[3]) : null;
        tail = parts.length > 4 ? parts[4] : null;
        tailSignificant = tailIsSignificant;
    }

    public int compareTo(@NotNull final SemanticVersion o) {
        int val = Utils.compareNullable(major, o.major);
        if (val == 0) {
            val = Utils.compareNullable(minor, o.minor);
            if (val == 0) {
                val = Utils.compareNullable(patch, o.patch);
                if (val == 0) {
                    val = Utils.compareNullable(tweak, o.tweak);
                    if (val == 0 && tailSignificant && tail != null && o.tail != null) {
                        val = tail.compareTo(o.tail);
                    }
                }
            }
        }
        return val;
    }

    public boolean isEarlierThan(SemanticVersion other) {
        return compareTo(other) < 0;
    }

    public boolean isLaterThan(SemanticVersion other) {
        return compareTo(other) > 0;
    }

    public boolean isEarlierThan(String other) {
        return compareTo(other) < 0;
    }

    public boolean isLaterThan(String other) {
        return compareTo(other) > 0;
    }

    public int compareTo(final String text) {
        SemanticVersion o = new SemanticVersion(text, tailSignificant);
        return compareTo(o);
    }
}
