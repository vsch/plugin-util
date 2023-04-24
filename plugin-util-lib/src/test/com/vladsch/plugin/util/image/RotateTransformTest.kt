package com.vladsch.plugin.util.image

import org.junit.Assert.assertEquals
import org.junit.Test

class RotateTransformTest : ImageTest() {

    @Test
    fun test_rotateImage() {
        val trans = RotateTransform(0)
        val rect = Rectangle.of(0, 10, 0, 20, 0)
        assertEquals(rect, trans.transformBounds(rect))
        assertEquals(rect, trans.reverseBounds(rect))
        assertEquals(rect, trans.reversed().reverseBounds(rect))
        assertEquals(rect, trans.reversed().transformBounds(rect))
        assertEquals(rect, trans.transformBounds(trans.reverseBounds(rect)))
        assertEquals(rect, trans.reverseBounds(trans.transformBounds(rect)))
    }

    @Test
    fun test_rotateShape() {
        val trans = RotateTransform(90)
        val bounds = Rectangle.of(0, 10, 0, 20, 0)
        val rect = Rectangle.of(0, 5, 0, 10, 0)
        val rect2 = Rectangle.of(5, 10, 10, 20, 0)
        val rotBounds = trans.transformBounds(bounds)
        val rotRect = trans.transform(rect, bounds)
        val rotRect2 = trans.transform(rect2, bounds)
        
        print("bounds: "); println(bounds.toString())
        print("rect: "); println(rect.toString())
        print("rotBounds: "); println(rotBounds.toString())
        print("rotRect: "); println(rotRect.toString())
        print("rotRect2: "); println(rotRect2.toString())
        
        assertEquals(rect, trans.reverseBounds(rect))
        assertEquals(rect, trans.reversed().reverseBounds(rect))
        assertEquals(rect, trans.reversed().transformBounds(rect))
        assertEquals(rect, trans.transformBounds(trans.reverseBounds(rect)))
        assertEquals(rect, trans.reverseBounds(trans.transformBounds(rect)))
    }
}
