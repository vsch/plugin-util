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

import com.vladsch.plugin.util.image.Rectangle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class RectangleTest {
    @Test
    fun test_NULL() {
        assertSame(Rectangle.NULL, Rectangle.of(0, 0, 0, 0))
        assertSame(Rectangle.NULL, Rectangle.of(0, 0, 0, 0).translate(0, 0))
        //        assertSame(Rectangle.NULL, Rectangle.of(0, 0, 0, 0).translate(10,10))
        //        assertSame(Rectangle.NULL, Rectangle.of(0, 0, 0, 0).translate(10,0))
        //        assertSame(Rectangle.NULL, Rectangle.of(0, 0, 0, 0).translate(0,10))

        assertSame(Rectangle.NULL, Rectangle.of(0, 0, 0, 0).scale(1f, 1f))
        assertSame(Rectangle.NULL, Rectangle.of(0, 0, 0, 0).scale(10f, 10f))
        assertSame(Rectangle.NULL, Rectangle.of(0, 0, 0, 0).scale(.1f, .1f))
    }

    @Test
    fun test_basic() {
        val rect1 = Rectangle.of(10, 30, 20, 50)

        assertEquals(10, rect1.x0)
        assertEquals(20, rect1.y0)
        assertEquals(30, rect1.x1)
        assertEquals(50, rect1.y1)
        assertEquals(10, rect1.left)
        assertEquals(20, rect1.top)
        assertEquals(30, rect1.right)
        assertEquals(50, rect1.bottom)


        assertEquals(20, rect1.spanX)
        assertEquals(30, rect1.spanY)

        assertEquals(20, rect1.width)
        assertEquals(30, rect1.height)
    }

    @Test
    fun test_basicBottomRight() {
        val rect1 = Rectangle.of(10, 30, 20, 50)
        val rect2 = Rectangle.fromBottomRight(10, 20, 30, 50)

        assertEquals(rect1, rect2)
    }

    @Test
    fun test_basicWidthHeight() {
        val rect1 = Rectangle.of(10, 30, 20, 50)
        val rect2 = Rectangle.fromWidthHeight(10, 20, 20, 30)

        assertEquals(rect1, rect2)
    }

    @Test
    fun test_translate() {
        val rect1 = Rectangle.of(10, 30, 20, 50)
        val rect00 = rect1.translate(0, 0)
        val rect01 = rect1.translate(0, 1)
        val rect10 = rect1.translate(1, 0)
        val rect11 = rect1.translate(1, 1)

        val rect1_01 = Rectangle.of(10, 30, 20 + 1, 50 + 1)
        val rect1_10 = Rectangle.of(10 + 1, 30 + 1, 20, 50)
        val rect1_11 = Rectangle.of(10 + 1, 30 + 1, 20 + 1, 50 + 1)

        assertSame(rect1, rect00)
        assertEquals(rect1_01, rect01)
        assertEquals(rect1_10, rect10)
        assertEquals(rect1_11, rect11)
    }

    @Test
    fun test_invert() {
        val rect1 = Rectangle.of(10, 30, 20, 50)
        val rect01 = Rectangle.of(10, 30, 50, 20)
        val rect10 = Rectangle.of(30, 10, 20, 50)
        val rect11 = Rectangle.of(30, 10, 50, 20)

        assertEquals(rect1, rect01.invert(false,true))
        assertEquals(rect1, rect10.invert(true,false))
        assertEquals(rect1, rect11.invert(true,true))
    }

    @Test
    fun test_normalized() {
        val rect1 = Rectangle.of(10, 30, 20, 50)
        val rect01 = Rectangle.of(10, 30, 50, 20)
        val rect10 = Rectangle.of(30, 10, 20, 50)
        val rect11 = Rectangle.of(30, 10, 50, 20)

        assertEquals(rect1, rect01.normalized)
        assertEquals(rect1, rect10.normalized)
        assertEquals(rect1, rect11.normalized)
    }

    @Test
    fun test_grow() {
        val rect1 = Rectangle.of(10, 30, 20, 50)
        val rect00 = rect1.grow(0, 0)
        val rect01 = rect1.grow(0, 1)
        val rect10 = rect1.grow(1, 0)
        val rect11 = rect1.grow(1, 1)

        val rect1_01 = Rectangle.of(10, 30, 20 - 1, 50 + 1)
        val rect1_10 = Rectangle.of(10 - 1, 30 + 1, 20, 50)
        val rect1_11 = Rectangle.of(10 - 1, 30 + 1, 20 - 1, 50 + 1)

        assertSame(rect1, rect00)
        assertEquals(rect1_01, rect01)
        assertEquals(rect1_10, rect10)
        assertEquals(rect1_11, rect11)
    }

    @Test
    fun test_scale() {
        val rect1 = Rectangle.of(10, 30, 20, 50)
        val rect00 = rect1.scale(1f, 1f)
        val rect01 = rect1.scale(1f, 2f)
        val rect10 = rect1.scale(2f, 1f)
        val rect11 = rect1.scale(2f, 2f)

        val rect1_01 = Rectangle.of(10, 30, 20 * 2, 50 * 2)
        val rect1_10 = Rectangle.of(10 * 2, 30 * 2, 20, 50)
        val rect1_11 = Rectangle.of(10 * 2, 30 * 2, 20 * 2, 50 * 2)

        assertSame(rect1, rect00)
        assertEquals(rect1_01, rect01)
        assertEquals(rect1_10, rect10)
        assertEquals(rect1_11, rect11)
    }

}
