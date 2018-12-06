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

import com.vladsch.plugin.util.html.HtmlBuilderTest;
import com.vladsch.plugin.util.html.HtmlHelpersTest;
import com.vladsch.plugin.util.image.BorderTransformTest;
import com.vladsch.plugin.util.image.CropTransformTest;
import com.vladsch.plugin.util.image.PointTest;
import com.vladsch.plugin.util.image.RectangleTest;
import com.vladsch.plugin.util.image.ScaleTransformTest;
import com.vladsch.plugin.util.image.TransformerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        StudiedWordTest.class,
        HtmlBuilderTest.class,
        HtmlHelpersTest.class,
        PointTest.class,
        RectangleTest.class,
        BorderTransformTest.class,
        CropTransformTest.class,
        ScaleTransformTest.class,
        TransformerTest.class,
})
public class PluginUtilTestSuite {
}
