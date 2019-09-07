package com.vladsch.plugin.util.image

import org.junit.Assert.assertEquals
import org.junit.Test

class TransformerTest {

    @Test
    fun test_transImage() {
        val scale = ScaleTransform(2f, 5f)
        val crop = CropTransform(Rectangle.of(1, 2, 5, 0,0))
        val border = BorderTransform(1)
        val trans = Transformer(listOf(crop, scale, border))

        val rect = Rectangle.of(0, 10, 0, 20, 1)
        assertEquals(border.transformBounds(scale.transformBounds(crop.transformBounds(rect))), trans.transformBounds(rect))
        assertEquals(crop.reverseBounds(scale.reverseBounds(border.reverseBounds(rect))), trans.reverseBounds(rect))
        assertEquals(border.transformBounds(scale.transformBounds(crop.transformBounds(rect))), trans.reversed().reverseBounds(rect))
        assertEquals(crop.reverseBounds(scale.reverseBounds(border.reverseBounds(rect))), trans.reversed().transformBounds(rect))
        assertEquals(rect, trans.transformBounds(trans.reverseBounds(rect)).roundTo(0.00001f))
        assertEquals(rect, trans.reverseBounds(trans.transformBounds(rect)).roundTo(0.00001f))
    }

    @Test
    fun test_transRect() {
        val scale = ScaleTransform(2f, 5f)
        val crop = CropTransform(Rectangle.of(1, 2, 5, 0,0))
        val border = BorderTransform(1)
        val trans = Transformer(listOf(crop, scale, border))

        val rect = Rectangle.of(0, 10, 0, 20,2)

        println("trans rect: $rect transformed: ${trans.transform(rect)}")
        println("trans rect: $rect reversed: ${trans.reverse(rect)}")

        assertEquals(border.transform(scale.transform(crop.transform(rect))), trans.transform(rect))
        assertEquals(crop.reverse(scale.reverse(border.reverse(rect))), trans.reverse(rect))
        assertEquals(border.transform(scale.transform(crop.transform(rect))), trans.reversed().reverse(rect))
        assertEquals(crop.reverse(scale.reverse(border.reverse(rect))), trans.reversed().transform(rect))
        assertEquals(rect, trans.reverse(trans.transform(rect)).roundTo(0.00001f))
        assertEquals(rect, trans.transform(trans.reverse(rect)).roundTo(0.00001f))
    }

    @Test
    fun test_transPoint() {
        val scale = ScaleTransform(2f, 5f)
        val crop = CropTransform(Rectangle.of(1, 2, 5, 0,0))
        val border = BorderTransform(1)
        val trans = Transformer(listOf(crop, scale, border))

        val point = Point.of(10, 20)
        assertEquals(border.transform(scale.transform(crop.transform(point))), trans.transform(point))
        assertEquals(crop.reverse(scale.reverse(border.reverse(point))), trans.reverse(point))
        assertEquals(border.transform(scale.transform(crop.transform(point))), trans.reversed().reverse(point))
        assertEquals(crop.reverse(scale.reverse(border.reverse(point))), trans.reversed().transform(point))
        assertEquals(point, trans.reverse(trans.transform(point)))
        assertEquals(point, trans.transform(trans.reverse(point)).round)
    }
}

