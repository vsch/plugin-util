package com.vladsch.plugin.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Template {
    final public static Resolver MAP_RESOLVER = groups -> null;

    public static class MappedResolver implements Resolver {
        final private Map<String, String> myMap;

        public MappedResolver(final Map<String, String> map) {
            myMap = map;
        }

        @Override
        public String resolve(final String[] groups) {
            return groups.length > 1 ? null : myMap.get(groups[0]);
        }
    }

    public interface Resolver {
        String resolve(String[] groups);
    }

    public static String resolveRefs(CharSequence text, Pattern pattern, Resolver resolver) {
        if (text == null) return "";

        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            StringBuffer sb = new StringBuffer();

            do {
                String[] groups = new String[matcher.groupCount() + 1];
                for (int i = 0; i < groups.length; i++) {
                    groups[i] = matcher.group(i);
                }

                String resolved = resolver.resolve(groups);

                matcher.appendReplacement(sb, resolved == null ? "" : resolved.replace("$", "\\$"));
            } while (matcher.find());

            matcher.appendTail(sb);
            return sb.toString();
        }
        return text.toString();
    }
}
