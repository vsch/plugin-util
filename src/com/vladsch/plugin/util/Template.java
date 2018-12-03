/*
 * Copyright (c) 2016-2018 Vladimir Schneider <vladimir.schneider@gmail.com>
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

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Template {
    final public static Resolver MAP_RESOLVER = new Resolver() {

        @Override
        public String resolve(final String[] groups) {
            return null;
        }
    };

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

    public static interface Resolver {
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
