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

import com.vladsch.plugin.util.ui.Helpers;
import org.junit.Test;

import java.awt.Font;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("UseJBColor")
public class HtmlHelpersTest {

    @Test
    public void test_mixedColor() {
        assertEquals(Color.of("#807240").toString(), Color.of(HtmlHelpers.mixedColor(Color.BLACK, Color.ORANGE)).toString());
    }
}
