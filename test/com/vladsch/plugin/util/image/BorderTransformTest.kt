package com.vladsch.plugin.util.image

import org.junit.Assert.*
import org.junit.Test

class BorderTransformTest {

    @Test
    fun test_transImage() {
        val trans = BorderTransform(1)
        val rect = Rectangle.of(0, 10, 0, 20, 0)
        assertEquals(Rectangle.of(0,12, 0,22, 0), trans.transformBounds(rect))
        assertEquals(Rectangle.of(0,8, 0,18, 0), trans.reverseBounds(rect))
        assertEquals(Rectangle.of(0,12, 0,22, 0), trans.reversed().reverseBounds(rect))
        assertEquals(Rectangle.of(0,8, 0,18, 0), trans.reversed().transformBounds(rect))
        assertEquals(rect, trans.transformBounds(trans.reverseBounds( rect)))
        assertEquals(rect, trans.reverseBounds(trans.transformBounds(rect)))
    }

    @Test
    fun test_transRect() {
        val trans = BorderTransform(1)
        val rect = Rectangle.of(0, 10, 0, 20, 1)
        assertEquals(Rectangle.of(1,11, 1,21, 1), trans.transform(rect))
        assertEquals(Rectangle.of(-1,9, -1,19, 1), trans.reverse(rect))
        assertEquals(Rectangle.of(1,11, 1,21, 1), trans.reversed().reverse(rect))
        assertEquals(Rectangle.of(-1,9, -1,19, 1), trans.reversed().transform(rect))
        assertEquals(rect, trans.reverse(trans.transform(rect)))
        assertEquals(rect, trans.transform(trans.reverse(rect)))
    }

    @Test
    fun test_transPoint() {
        val trans = BorderTransform(1)
        val point = Point.of(10, 20)
        assertEquals(Point.of(11,21), trans.transform(point))
        assertEquals(Point.of(9,19), trans.reverse(point))
        assertEquals(Point.of(11,21), trans.reversed().reverse(point))
        assertEquals(Point.of(9,19), trans.reversed().transform(point))
        assertEquals(point, trans.reverse(trans.transform(point)))
        assertEquals(point, trans.transform(trans.reverse(point)))
    }
}
