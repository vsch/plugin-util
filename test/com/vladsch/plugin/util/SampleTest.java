/*
 * Copyright (c) 2015-2019 Vladimir Schneider <vladimir.schneider@gmail.com>, all rights reserved.
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

package com.vladsch.plugin.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(value = Parameterized.class)
public class SampleTest {
    final String location;
    final int expected;
    final int actual;

    public SampleTest(final String location, final int expected, final int actual) {
        this.location = location;
        this.expected = expected;
        this.actual = actual;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        ArrayList<Object[]> rows = new ParamRowGenerator("/Users/vlad/src/projects/console-file-caddy/test/com/vladsch/plugins/consoleFileCaddy")
                .row(new Object[] { 1, 1 })
                .row(new Object[] { 2, 3 })
                .row(new Object[] { 3, 3 })
                .rows;
        return rows;
    }

    @Test
    public void test_sample() {
        assertEquals(location, expected, actual);
    }
}

