package com.vladsch.plugin.util.image

import org.junit.Assert.assertEquals
import org.junit.Test

class ScaleTransformTest : ImageTest() {

    @Test
    fun test_transImage() {
        val trans = ScaleTransform(2f, 5f)
        val rect = Rectangle.of(0, 10, 0, 20, 0)
        assertEquals(Rectangle.of(0, 20, 0, 100, 0), trans.transformBounds(rect))
        assertEquals(Rectangle.of(0, 5, 0, 4, 0), trans.reverseBounds(rect))
        assertEquals(Rectangle.of(0, 20, 0, 100, 0), trans.reversed().reverseBounds(rect))
        assertEquals(Rectangle.of(0, 5, 0, 4, 0), trans.reversed().transformBounds(rect))
        assertEquals(rect, trans.transformBounds(trans.reverseBounds(rect)))
        assertEquals(rect, trans.reverseBounds(trans.transformBounds(rect)))
    }

    @Test
    fun test_transRect() {
        val trans = ScaleTransform(2f, 5f)
        val rect = Rectangle.of(0, 10, 0, 20, 1)
        assertEquals(Rectangle.of(0f, 20f, 0f, 100f, 1f), trans.transform(rect, rect))
        assertEquals(Rectangle.of(0f, 5f, 0f, 4f, 1f), trans.reverse(rect, rect))
        assertEquals(Rectangle.of(0f, 20f, 0f, 100f, 1f), trans.reversed().reverse(rect, rect))
        assertEquals(Rectangle.of(0f, 5f, 0f, 4f, 1f), trans.reversed().transform(rect, rect))
        assertEquals(rect, trans.reverse(trans.transform(rect, rect), rect))
        assertEquals(rect, trans.transform(trans.reverse(rect, rect), rect))
    }

    @Test
    fun test_transPoint() {
        val trans = ScaleTransform(2f, 5f)
        val point = Point.of(10, 20)
        val rect = Rectangle.of(-5, 5, -5, 5, 0)
        assertEquals(Point.of(20, 100), trans.transform(point, rect))
        assertEquals(Point.of(5, 4), trans.reverse(point, rect))
        assertEquals(Point.of(20, 100), trans.reversed().reverse(point, rect))
        assertEquals(Point.of(5, 4), trans.reversed().transform(point, rect))
        assertEquals(point, trans.reverse(trans.transform(point, rect), rect))
        assertEquals(point, trans.transform(trans.reverse(point, rect), rect))
    }

    @Test
    fun test_Image1() {
        val name = "Image1"
        val trans = ScaleTransform(0, 0)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image2() {
        val name = "Image2"
        val trans = ScaleTransform(0f, 0f)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image3() {
        val name = "Image3"
        val trans = ScaleTransform(1, 1)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image4() {
        val name = "Image4"
        val trans = ScaleTransform(1f, 1f)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image5() {
        val name = "Image5"
        val trans = ScaleTransform(2f, 2f)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image6() {
        val name = "Image6"
        val trans = ScaleTransform(0.5f, 0.5f)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }
}
