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

package com.vladsch.plugin.util.image

import com.vladsch.plugin.util.image.Point
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class PointTest {
    @Test
    fun test_NULL() {
        assertSame(Point.NULL, Point.of(0,0))
        assertSame(Point.NULL, Point.of(0,0).translate(0, 0))
        //        assertSame(Point.NULL, Point.of(0,0).translate(10,10))
        //        assertSame(Point.NULL, Point.of(0,0).translate(10,0))
        //        assertSame(Point.NULL, Point.of(0,0).translate(0,10))

        assertSame(Point.NULL, Point.of(0,0).scale(1f, 1f))
        assertSame(Point.NULL, Point.of(0,0).scale(10f, 10f))
        assertSame(Point.NULL, Point.of(0,0).scale(.1f, .1f))
    }

    @Test
    fun test_basic() {
        val point1 = Point.of(10,30)

        assertEquals(10, point1.x)
        assertEquals(30, point1.y)
    }

    @Test
    fun test_translate() {
        val point1 = Point.of(10,30)
        val point00 = point1.translate(0, 0)
        val point01 = point1.translate(0, 1)
        val point10 = point1.translate(1, 0)
        val point11 = point1.translate(1, 1)

        val point1_01 = Point.of(10,30+1)
        val point1_10 = Point.of(10 + 1,30)
        val point1_11 = Point.of(10 + 1,30 + 1)

        assertSame(point1, point00)
        assertEquals(point1_01, point01)
        assertEquals(point1_10, point10)
        assertEquals(point1_11, point11)
    }

    @Test
    fun test_scale() {
        val point1 = Point.of(10,30)
        val point00 = point1.scale(1f, 1f)
        val point01 = point1.scale(1f, 2f)
        val point10 = point1.scale(2f, 1f)
        val point11 = point1.scale(2f, 2f)

        val point1_01 = Point.of(10,30 * 2)
        val point1_10 = Point.of(10 * 2,30)
        val point1_11 = Point.of(10 * 2,30 * 2)

        assertSame(point1, point00)
        assertEquals(point1_01, point01)
        assertEquals(point1_10, point10)
        assertEquals(point1_11, point11)
    }

}
