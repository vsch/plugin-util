/*
 *
 */
package com.vladsch.plugin.util;

import com.vladsch.plugin.util.html.HtmlBuilderTest;
import com.vladsch.plugin.util.image.ImageTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        StudiedWordTest.class,
        HtmlBuilderTest.class,
        ImageTestSuite.class,
})
public class PluginUtilTestSuite {
}
