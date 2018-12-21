/*
 * Copyright (c) 2015-2018 Vladimir Schneider <vladimir.schneider@gmail.com>, all rights reserved.
 *
 * This code is private property of the copyright holder and cannot be used without
 * having obtained a license or prior written permission of the of the copyright holder.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

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
        assertEquals("<span style=\"color:#ff0000\"></span>", fa.toFinalizedString());

        fa = new HtmlBuilder();
        fa.attr(JBColor.RED).attr(JBColor.GREEN).span();
        assertEquals("<span style=\"color:#00ff00\"></span>", fa.toFinalizedString());

        fa = new HtmlBuilder();
        fa.attr(JBColor.RED).attr(JBColor.GREEN).span().closeSpan();
        assertEquals("<span style=\"color:#00ff00\"></span>", fa.toFinalizedString());

        fa = new HtmlBuilder();
        fa.attr(JBColor.RED).attr(BackgroundColor.GREEN).span().closeSpan();
        assertEquals("<span style=\"color:#ff0000;background-color:#00ff00\"></span>", fa.toFinalizedString());
    }

}
