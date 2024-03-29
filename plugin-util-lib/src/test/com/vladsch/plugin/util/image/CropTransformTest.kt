package com.vladsch.plugin.util.image

import org.junit.Assert.assertEquals
import org.junit.Test

class CropTransformTest : ImageTest() {

    @Test
    fun test_transImage() {
        val trans = CropTransform(Rectangle.of(1, 2, 5, 0, 2))
        val rect = Rectangle.of(0, 10, 0, 20, 2)
        assertEquals(Rectangle.of(0, 7, 0, 15, 2), trans.transformBounds(rect))
        assertEquals(Rectangle.of(0, 13, 0, 25, 2), trans.reverseBounds(rect))
        assertEquals(Rectangle.of(0, 7, 0, 15, 2), trans.reversed().reverseBounds(rect))
        assertEquals(Rectangle.of(0, 13, 0, 25, 2), trans.reversed().transformBounds(rect))
        assertEquals(rect, trans.transformBounds(trans.reverseBounds(rect)))
        assertEquals(rect, trans.reverseBounds(trans.transformBounds(rect)))
    }

    @Test
    fun test_transRect() {
        val trans = CropTransform(Rectangle.of(1, 2, 5, 0, 3))
        val rect = Rectangle.of(0, 10, 0, 20, 3)
        assertEquals(Rectangle.of(-1, 9, -5, 15, 3), trans.transform(rect, rect))
        assertEquals(Rectangle.of(1, 11, 5, 25, 3), trans.reverse(rect, rect))
        assertEquals(Rectangle.of(-1, 9, -5, 15, 3), trans.reversed().reverse(rect, rect))
        assertEquals(Rectangle.of(1, 11, 5, 25, 3), trans.reversed().transform(rect, rect))
        assertEquals(rect, trans.reverse(trans.transform(rect, rect), rect))
        assertEquals(rect, trans.transform(trans.reverse(rect, rect), rect))
    }

    @Test
    fun test_transPoint() {
        val trans = CropTransform(Rectangle.of(1, 2, 5, 0, 4))
        val point = Point.of(10, 20)
        val rect = Rectangle.of(-5, 5, -5, 5, 0)
        assertEquals(Point.of(9, 15), trans.transform(point, rect))
        assertEquals(Point.of(11, 25), trans.reverse(point, rect))
        assertEquals(Point.of(9, 15), trans.reversed().reverse(point, rect))
        assertEquals(Point.of(11, 25), trans.reversed().transform(point, rect))
        assertEquals(point, trans.reverse(trans.transform(point, rect), rect))
        assertEquals(point, trans.transform(trans.reverse(point, rect), rect))
    }

    @Test
    fun test_Image1() {
        val name = "Image1"
        val trans = CropTransform(Rectangle.of(0, 0, 0, 0, 0))
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image2() {
        val name = "Image2"
        val trans = CropTransform(Rectangle.of(20, 0, 0, 0, 0))
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image3() {
        val name = "Image3"
        val trans = CropTransform(Rectangle.of(0, 40, 0, 0, 10))
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image4() {
        val name = "Image4"
        val trans = CropTransform(Rectangle.of(0, 0, 10, 0, 20))
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image5() {
        val name = "Image5"
        val trans = CropTransform(Rectangle.of(0, 0, 0, 20, 20))
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image6() {
        val name = "Image6"
        val trans = CropTransform(Rectangle.of(20, 40, 10, 20, 20))
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }
}
