package com.vladsch.plugin.util.image

import org.junit.Assert.assertEquals
import org.junit.Test
import java.awt.Color

class TransformerTest:ImageTest() {

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

    @Test
    fun test_Image1() {
        val name = "Image1"
        val scale = ScaleTransform(2f, 5f)
        val crop = CropTransform(Rectangle.of(15, 20, 5, 10,0))
        val border = BorderTransform(2, 0, Color.RED)
        val trans = Transformer(listOf(crop, scale, border))

        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name,actual)
    }

    @Test
    fun test_Image2() {
        val name = "Image2"
        val scale = ScaleTransform(.5f, .5f)
        val crop = CropTransform(Rectangle.of(16, 21, 6, 11,1))
        val border = BorderTransform(3, 10, Color.RED)
        val trans = Transformer(listOf(crop, scale, border))

        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name,actual)
    }

    @Test
    fun test_Image3() {
        val name = "Image3"
        val scale = ScaleTransform(1f, 1f)
        val crop = CropTransform(Rectangle.of(17, 22, 7, 12,2))
        val border = BorderTransform(3, 20, Color.RED)
        val trans = Transformer(listOf(crop, scale, border))

        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name,actual)
    }

    @Test
    fun test_Image4() {
        val name = "Image4"
        val scale = ScaleTransform(1.5f, 1.5f)
        val crop = CropTransform(Rectangle.of(18, 23, 8, 13,3))
        val border = BorderTransform(1, 10, Color.RED)
        val trans = Transformer(listOf(crop, scale, border))

        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name,actual)
    }
}

