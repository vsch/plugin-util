package com.vladsch.plugin.util.image

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import kotlin.test.assertNotSame

class RectangleTest {
    @Test
    fun test_NULL() {
        assertSame(Rectangle.NULL, Rectangle.of(0, 0, 0, 0, 0))
        assertNotSame(Rectangle.NULL, Rectangle.of(0, 0, 0, 0, 1))
        assertSame(Rectangle.NULL, Rectangle.of(0, 0, 0, 0, 0).translate(0, 0))
        //        assertSame(Rectangle.NULL, Rectangle.of(0, 0, 0, 0).translate(10,10))
        //        assertSame(Rectangle.NULL, Rectangle.of(0, 0, 0, 0).translate(10,0))
        //        assertSame(Rectangle.NULL, Rectangle.of(0, 0, 0, 0).translate(0,10))

        assertSame(Rectangle.NULL, Rectangle.of(0, 0, 0, 0, 0).scale(1f, 1f))
        assertSame(Rectangle.NULL, Rectangle.of(0, 0, 0, 0, 0).scale(10f, 10f))
        assertSame(Rectangle.NULL, Rectangle.of(0, 0, 0, 0, 0).scale(.1f, .1f))
    }

    @Test
    fun test_basic() {
        val rect1 = Rectangle.of(10, 30, 20, 50, 1)

        assertEquals(10f, rect1.x0)
        assertEquals(30f, rect1.x1)
        assertEquals(20f, rect1.y0)
        assertEquals(50f, rect1.y1)
        assertEquals(10f, rect1.left)
        assertEquals(20f, rect1.top)
        assertEquals(30f, rect1.right)
        assertEquals(50f, rect1.bottom)
        assertEquals(1f, rect1.radius)

        assertEquals(20f, rect1.spanX)
        assertEquals(30f, rect1.spanY)
        assertEquals(20f, rect1.square)
        assertEquals(20f, rect1.squareSpanX)
        assertEquals(20f, rect1.squareSpanY)

        assertEquals(20f, rect1.width)
        assertEquals(30f, rect1.height)
    }

    @Test
    fun test_round() {
        val rect1 = Rectangle.of(10.4f, 30.4f, 20.4f, 50.4f, 1f)
        val rect2 = Rectangle.of(10.4f, 29.5f, 20.4f, 49.5f, 1f)
        val rect3 = Rectangle.of(9.5f, 30.4f, 19.5f, 50.4f, 1f)
        val rect4 = Rectangle.of(9.5f, 29.5f, 19.5f, 49.5f, 1f)

        assertEquals(10, rect1.intX0)
        assertEquals(30, rect1.intX1)
        assertEquals(20, rect1.intY0)
        assertEquals(50, rect1.intY1)
        assertEquals(10, rect1.intLeft)
        assertEquals(20, rect1.intTop)
        assertEquals(30, rect1.intRight)
        assertEquals(50, rect1.intBottom)
        assertEquals(20, rect1.intWidth)
        assertEquals(30, rect1.intHeight)


        assertEquals(20, rect1.intSpanX)
        assertEquals(30, rect1.intSpanY)
        assertEquals(20, rect1.intSquare)
        assertEquals(20, rect1.intSquareSpanX)
        assertEquals(20, rect1.intSquareSpanY)

        assertEquals(10f, rect1.round.x0)
        assertEquals(30f, rect1.round.x1)
        assertEquals(20f, rect1.round.y0)
        assertEquals(50f, rect1.round.y1)
        assertEquals(10f, rect1.round.left)
        assertEquals(20f, rect1.round.top)
        assertEquals(30f, rect1.round.right)
        assertEquals(50f, rect1.round.bottom)
        assertEquals(20f, rect1.round.width)
        assertEquals(30f, rect1.round.height)


        assertEquals(20f, rect1.round.spanX)
        assertEquals(30f, rect1.round.spanY)
        assertEquals(20f, rect1.round.square)
        assertEquals(20f, rect1.round.squareSpanX)
        assertEquals(20f, rect1.round.squareSpanY)

        assertEquals(rect1.round, rect2.round)
        assertEquals(rect1.round, rect3.round)
        assertEquals(rect1.round, rect4.round)
    }

    @Test
    fun test_basicInvertedXY() {
        val rect1 = Rectangle.of(30, 10, 50, 20, 2)

        assertEquals(30f, rect1.x0)
        assertEquals(10f, rect1.x1)
        assertEquals(50f, rect1.y0)
        assertEquals(20f, rect1.y1)
        assertEquals(10f, rect1.left)
        assertEquals(20f, rect1.top)
        assertEquals(30f, rect1.right)
        assertEquals(50f, rect1.bottom)


        assertEquals(-20f, rect1.spanX)
        assertEquals(-30f, rect1.spanY)
        assertEquals(20f, rect1.square)
        assertEquals(-20f, rect1.squareSpanX)
        assertEquals(-20f, rect1.squareSpanY)

        assertEquals(20f, rect1.width)
        assertEquals(30f, rect1.height)
    }

    @Test
    fun test_basicInvertedY() {
        val rect1 = Rectangle.of(10, 30, 50, 20, 3)

        assertEquals(10f, rect1.x0)
        assertEquals(30f, rect1.x1)
        assertEquals(50f, rect1.y0)
        assertEquals(20f, rect1.y1)
        assertEquals(10f, rect1.left)
        assertEquals(20f, rect1.top)
        assertEquals(30f, rect1.right)
        assertEquals(50f, rect1.bottom)


        assertEquals(20f, rect1.spanX)
        assertEquals(-30f, rect1.spanY)
        assertEquals(20f, rect1.square)
        assertEquals(20f, rect1.squareSpanX)
        assertEquals(-20f, rect1.squareSpanY)

        assertEquals(20f, rect1.width)
        assertEquals(30f, rect1.height)
    }

    @Test
    fun test_basicInvertedX() {
        val rect1 = Rectangle.of(30, 10, 20, 50, 3)

        assertEquals(30f, rect1.x0)
        assertEquals(10f, rect1.x1)
        assertEquals(20f, rect1.y0)
        assertEquals(50f, rect1.y1)
        assertEquals(10f, rect1.left)
        assertEquals(20f, rect1.top)
        assertEquals(30f, rect1.right)
        assertEquals(50f, rect1.bottom)


        assertEquals(-20f, rect1.spanX)
        assertEquals(30f, rect1.spanY)
        assertEquals(20f, rect1.square)
        assertEquals(-20f, rect1.squareSpanX)
        assertEquals(20f, rect1.squareSpanY)

        assertEquals(20f, rect1.width)
        assertEquals(30f, rect1.height)
    }

    @Test
    fun test_basicBottomRight() {
        val rect1 = Rectangle.of(10, 30, 20, 50, 2)
        val rect2 = Rectangle.fromBottomRight(10, 20, 30, 50, 2)

        assertEquals(rect1, rect2)
    }

    @Test
    fun test_basicWidthHeight() {
        val rect1 = Rectangle.of(10, 30, 20, 50, 1)
        val rect2 = Rectangle.fromWidthHeight(10, 20, 20, 30, 1)

        assertEquals(rect1, rect2)
    }

    @Test
    fun test_translate() {
        val rect1 = Rectangle.of(10, 30, 20, 50, 4)
        val rect00 = rect1.translate(0, 0)
        val rect01 = rect1.translate(0, 1)
        val rect10 = rect1.translate(1, 0)
        val rect11 = rect1.translate(1, 1)

        val rect1_01 = Rectangle.of(10, 30, 20 + 1, 50 + 1, 4)
        val rect1_10 = Rectangle.of(10 + 1, 30 + 1, 20, 50, 4)
        val rect1_11 = Rectangle.of(10 + 1, 30 + 1, 20 + 1, 50 + 1, 4)

        assertSame(rect1, rect00)
        assertEquals(rect1_01, rect01)
        assertEquals(rect1_10, rect10)
        assertEquals(rect1_11, rect11)
    }

    @Test
    fun test_invert() {
        val rect1 = Rectangle.of(10, 30, 20, 50, 5)
        val rect01 = Rectangle.of(10, 30, 50, 20, 5)
        val rect10 = Rectangle.of(30, 10, 20, 50, 5)
        val rect11 = Rectangle.of(30, 10, 50, 20, 5)

        assertEquals(rect1, rect01.invert(false, true))
        assertEquals(rect1, rect10.invert(true, false))
        assertEquals(rect1, rect11.invert(true, true))
    }

    @Test
    fun test_normalized() {
        val rect1 = Rectangle.of(10, 30, 20, 50, 6)
        val rect01 = Rectangle.of(10, 30, 50, 20, 6)
        val rect10 = Rectangle.of(30, 10, 20, 50, 6)
        val rect11 = Rectangle.of(30, 10, 50, 20, 6)

        assertEquals(rect1, rect01.normalized)
        assertEquals(rect1, rect10.normalized)
        assertEquals(rect1, rect11.normalized)
    }

    @Test
    fun test_grow1() {
        val rect1 = Rectangle.of(10, 30, 20, 50, 7)
        val rect00 = rect1.grow(0)
        val rect11 = rect1.grow(1)
        val rect00f = rect1.grow(0f)
        val rect11f = rect1.grow(1f)

        val rect1_01 = Rectangle.of(10, 30, 20 - 1, 50 + 1, 7)
        val rect1_10 = Rectangle.of(10 - 1, 30 + 1, 20, 50, 7)
        val rect1_11 = Rectangle.of(10 - 1, 30 + 1, 20 - 1, 50 + 1, 7)

        assertSame(rect1, rect00)
        assertEquals(rect1_11, rect11)
        assertSame(rect1, rect00f)
        assertEquals(rect1_11, rect11f)
    }

    @Test
    fun test_grow2() {
        val rect1 = Rectangle.of(10, 30, 20, 50, 8)
        val rect00 = rect1.grow(0, 0)
        val rect01 = rect1.grow(0, 1)
        val rect10 = rect1.grow(1, 0)
        val rect11 = rect1.grow(1, 1)
        val rect00f = rect1.grow(0f, 0f)
        val rect01f = rect1.grow(0f, 1f)
        val rect10f = rect1.grow(1f, 0f)
        val rect11f = rect1.grow(1f, 1f)

        val rect1_01 = Rectangle.of(10, 30, 20 - 1, 50 + 1, 8)
        val rect1_10 = Rectangle.of(10 - 1, 30 + 1, 20, 50, 8)
        val rect1_11 = Rectangle.of(10 - 1, 30 + 1, 20 - 1, 50 + 1, 8)

        assertSame(rect1, rect00)
        assertEquals(rect1_01, rect01)
        assertEquals(rect1_10, rect10)
        assertEquals(rect1_11, rect11)

        assertSame(rect1, rect00f)
        assertEquals(rect1_01, rect01f)
        assertEquals(rect1_10, rect10f)
        assertEquals(rect1_11, rect11f)
    }

    @Test
    fun test_grow4() {
        val rect1 = Rectangle.of(10, 30, 20, 50, 9)
        val rect0000 = rect1.grow(0, 0, 0, 0)
        val rect0001 = rect1.grow(0, 0, 0, 1)
        val rect0010 = rect1.grow(0, 0, 1, 0)
        val rect0100 = rect1.grow(0, 1, 0, 0)
        val rect1000 = rect1.grow(1, 0, 0, 0)

        val rect0000f = rect1.grow(0f, 0f, 0f, 0f)
        val rect0001f = rect1.grow(0f, 0f, 0f, 1f)
        val rect0010f = rect1.grow(0f, 0f, 1f, 0f)
        val rect0100f = rect1.grow(0f, 1f, 0f, 0f)
        val rect1000f = rect1.grow(1f, 0f, 0f, 0f)

        val rect1_0001 = Rectangle.of(10, 30, 20, 50 + 1, 9)
        val rect1_0010 = Rectangle.of(10, 30, 20 - 1, 50, 9)
        val rect1_0100 = Rectangle.of(10, 30 + 1, 20, 50, 9)
        val rect1_1000 = Rectangle.of(10 - 1, 30, 20, 50, 9)

        assertSame(rect1, rect0000)
        assertEquals(rect1_0001, rect0001)
        assertEquals(rect1_0001, rect0001)
        assertEquals(rect1_0010, rect0010)
        assertEquals(rect1_0100, rect0100)
        assertEquals(rect1_1000, rect1000)

        assertSame(rect1, rect0000f)
        assertEquals(rect1_0001, rect0001f)
        assertEquals(rect1_0001, rect0001f)
        assertEquals(rect1_0010, rect0010f)
        assertEquals(rect1_0100, rect0100f)
        assertEquals(rect1_1000, rect1000f)
    }

    @Test
    fun test_shrink1() {
        val rect1 = Rectangle.of(10, 30, 20, 50, 10)
        val rect00 = rect1.shrink(0)
        val rect11 = rect1.shrink(-1)

        val rect00f = rect1.shrink(0f)
        val rect11f = rect1.shrink(-1f)

        val rect1_01 = Rectangle.of(10, 30, 20 - 1, 50 + 1, 10)
        val rect1_10 = Rectangle.of(10 - 1, 30 + 1, 20, 50, 10)
        val rect1_11 = Rectangle.of(10 - 1, 30 + 1, 20 - 1, 50 + 1, 10)

        assertSame(rect1, rect00)
        assertEquals(rect1_11, rect11)

        assertSame(rect1, rect00f)
        assertEquals(rect1_11, rect11f)
    }

    @Test
    fun test_shrink2() {
        val rect1 = Rectangle.of(10, 30, 20, 50, 11)
        val rect00 = rect1.shrink(0, 0)
        val rect01 = rect1.shrink(0, -1)
        val rect10 = rect1.shrink(-1, 0)
        val rect11 = rect1.shrink(-1, -1)

        val rect00f = rect1.shrink(0f, 0f)
        val rect01f = rect1.shrink(0f, -1f)
        val rect10f = rect1.shrink(-1f, 0f)
        val rect11f = rect1.shrink(-1f, -1f)

        val rect1_01 = Rectangle.of(10, 30, 20 - 1, 50 + 1, 11)
        val rect1_10 = Rectangle.of(10 - 1, 30 + 1, 20, 50, 11)
        val rect1_11 = Rectangle.of(10 - 1, 30 + 1, 20 - 1, 50 + 1, 11)

        assertSame(rect1, rect00)
        assertEquals(rect1_01, rect01)
        assertEquals(rect1_10, rect10)
        assertEquals(rect1_11, rect11)

        assertSame(rect1, rect00f)
        assertEquals(rect1_01, rect01f)
        assertEquals(rect1_10, rect10f)
        assertEquals(rect1_11, rect11f)
    }

    @Test
    fun test_shrink4() {
        val rect1 = Rectangle.of(10, 30, 20, 50, 12)
        val rect0000 = rect1.shrink(0, 0, 0, 0)
        val rect0001 = rect1.shrink(0, 0, 0, -1)
        val rect0010 = rect1.shrink(0, 0, -1, 0)
        val rect0100 = rect1.shrink(0, -1, 0, 0)
        val rect1000 = rect1.shrink(-1, 0, 0, 0)

        val rect0000f = rect1.shrink(0f, 0f, 0f, 0f)
        val rect0001f = rect1.shrink(0f, 0f, 0f, -1f)
        val rect0010f = rect1.shrink(0f, 0f, -1f, 0f)
        val rect0100f = rect1.shrink(0f, -1f, 0f, 0f)
        val rect1000f = rect1.shrink(-1f, 0f, 0f, 0f)

        val rect1_0001 = Rectangle.of(10, 30, 20, 50 + 1, 12)
        val rect1_0010 = Rectangle.of(10, 30, 20 - 1, 50, 12)
        val rect1_0100 = Rectangle.of(10, 30 + 1, 20, 50, 12)
        val rect1_1000 = Rectangle.of(10 - 1, 30, 20, 50, 12)

        assertSame(rect1, rect0000)
        assertEquals(rect1_0001, rect0001)
        assertEquals(rect1_0001, rect0001)
        assertEquals(rect1_0010, rect0010)
        assertEquals(rect1_0100, rect0100)
        assertEquals(rect1_1000, rect1000)

        assertSame(rect1, rect0000f)
        assertEquals(rect1_0001, rect0001f)
        assertEquals(rect1_0001, rect0001f)
        assertEquals(rect1_0010, rect0010f)
        assertEquals(rect1_0100, rect0100f)
        assertEquals(rect1_1000, rect1000f)
    }

    @Test
    fun test_scale1() {
        val rect1 = Rectangle.of(10, 30, 20, 50, 1)
        val rect00 = rect1.scale(1f)
        val rect01 = rect1.scale(3f)

        val rect1_01 = Rectangle.of(10 * 3, 30 * 3, 20 * 3, 50 * 3, 1 * 3)

        assertSame(rect1, rect00)
        assertEquals(rect1_01, rect01)
    }

    @Test
    fun test_scale2() {
        val rect1 = Rectangle.of(10, 30, 20, 50, 2)
        val rect00 = rect1.scale(1f, 1f)
        val rect01 = rect1.scale(1f, 2f)
        val rect10 = rect1.scale(2f, 1f)
        val rect11 = rect1.scale(2f, 2f)

        val rect1_01 = Rectangle.of(10, 30, 20 * 2, 50 * 2, 2)
        val rect1_10 = Rectangle.of(10 * 2, 30 * 2, 20, 50, 3)
        val rect1_11 = Rectangle.of(10 * 2, 30 * 2, 20 * 2, 50 * 2, 4)

        assertSame(rect1, rect00)
        assertEquals(rect1_01, rect01)
        assertEquals(rect1_10, rect10)
        assertEquals(rect1_11, rect11)
    }
}
