package com.vladsch.plugin.util.html;

import com.intellij.ui.JBColor;
import com.vladsch.flexmark.util.html.ui.BackgroundColor;
import com.vladsch.flexmark.util.html.ui.HtmlBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HtmlBuilderTest {
    @Test
    public void test_Attr() throws Exception {
        HtmlBuilder fa;

        fa = new HtmlBuilder();
        fa.attr(JBColor.RED).span();
        assertEquals("<span style=\"color:#ff0000\"></span>\n", fa.toFinalizedString());

        fa = new HtmlBuilder();
        fa.attr(JBColor.RED).attr(JBColor.GREEN).span();
        assertEquals("<span style=\"color:#00ff00\"></span>\n", fa.toFinalizedString());

        fa = new HtmlBuilder();
        fa.attr(JBColor.RED).attr(JBColor.GREEN).span().closeSpan();
        assertEquals("<span style=\"color:#00ff00\"></span>\n", fa.toFinalizedString());

        fa = new HtmlBuilder();
        fa.attr(JBColor.RED).attr(BackgroundColor.GREEN).span().closeSpan();
        assertEquals("<span style=\"color:#ff0000;background-color:#00ff00\"></span>\n", fa.toFinalizedString());
    }
}
