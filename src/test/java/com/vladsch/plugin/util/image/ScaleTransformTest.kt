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
        assertEquals(Rectangle.of(0, 20, 0, 100, 1), trans.transform(rect))
        assertEquals(Rectangle.of(0, 5, 0, 4, 1), trans.reverse(rect))
        assertEquals(Rectangle.of(0, 20, 0, 100, 1), trans.reversed().reverse(rect))
        assertEquals(Rectangle.of(0, 5, 0, 4, 1), trans.reversed().transform(rect))
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
