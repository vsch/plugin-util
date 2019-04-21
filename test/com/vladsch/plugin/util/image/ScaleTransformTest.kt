package com.vladsch.plugin.util.image

import org.junit.Assert.assertEquals
import org.junit.Test

class ScaleTransformTest {

    @Test
    fun test_transImage() {
        val trans = ScaleTransform(2f, 5f)
        val rect = Rectangle.of(0, 10, 0, 20)
        assertEquals(Rectangle.of(0, 20, 0, 100), trans.transformImage(rect))
        assertEquals(Rectangle.of(0, 5, 0, 4), trans.reverseImage(rect))
        assertEquals(Rectangle.of(0, 20, 0, 100), trans.reversed().reverseImage(rect))
        assertEquals(Rectangle.of(0, 5, 0, 4), trans.reversed().transformImage(rect))
        assertEquals(rect, trans.transformImage(trans.reverseImage(rect)))
        assertEquals(rect, trans.reverseImage(trans.transformImage(rect)))
    }

    @Test
    fun test_transRect() {
        val trans = ScaleTransform(2f, 5f)
        val rect = Rectangle.of(0, 10, 0, 20)
        assertEquals(Rectangle.of(0, 20, 0, 100), trans.transform(rect))
        assertEquals(Rectangle.of(0, 5, 0, 4), trans.reverse(rect))
        assertEquals(Rectangle.of(0, 20, 0, 100), trans.reversed().reverse(rect))
        assertEquals(Rectangle.of(0, 5, 0, 4), trans.reversed().transform(rect))
        assertEquals(rect, trans.reverse(trans.transform(rect)))
        assertEquals(rect, trans.transform(trans.reverse(rect)))
    }

    @Test
    fun test_transPoint() {
        val trans = ScaleTransform(2f, 5f)
        val point = Point.of(10, 20)
        assertEquals(Point.of(20, 100), trans.transform(point))
        assertEquals(Point.of(5, 4), trans.reverse(point))
        assertEquals(Point.of(20, 100), trans.reversed().reverse(point))
        assertEquals(Point.of(5, 4), trans.reversed().transform(point))
        assertEquals(point, trans.reverse(trans.transform(point)))
        assertEquals(point, trans.transform(trans.reverse(point)))
    }
}
