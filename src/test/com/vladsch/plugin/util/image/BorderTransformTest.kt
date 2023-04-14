package com.vladsch.plugin.util.image

import org.junit.Assert.assertEquals
import org.junit.Test
import java.awt.Color

class BorderTransformTest : ImageTest() {

    @Test
    fun test_transImage() {
        val trans = BorderTransform(1)
        val rect = Rectangle.of(0, 10, 0, 20, 0)
        assertEquals(Rectangle.of(0, 12, 0, 22, 1), trans.transformBounds(rect))
        assertEquals(Rectangle.of(0, 8, 0, 18, -1), trans.reverseBounds(rect))
        assertEquals(Rectangle.of(0, 12, 0, 22, 1), trans.reversed().reverseBounds(rect))
        assertEquals(Rectangle.of(0, 8, 0, 18, -1), trans.reversed().transformBounds(rect))
        assertEquals(rect, trans.transformBounds(trans.reverseBounds(rect)))
        assertEquals(rect, trans.reverseBounds(trans.transformBounds(rect)))
    }

    @Test
    fun test_transRect() {
        val trans = BorderTransform(1)
        val rect = Rectangle.of(0, 10, 0, 20, 1)
        assertEquals(Rectangle.of(1, 11, 1, 21, 1), trans.transform(rect, rect))
        assertEquals(Rectangle.of(-1, 9, -1, 19, 1), trans.reverse(rect, rect))
        assertEquals(Rectangle.of(1, 11, 1, 21, 1), trans.reversed().reverse(rect, rect))
        assertEquals(Rectangle.of(-1, 9, -1, 19, 1), trans.reversed().transform(rect, rect))
        assertEquals(rect, trans.reverse(trans.transform(rect, rect), rect))
        assertEquals(rect, trans.transform(trans.reverse(rect, rect), rect))
    }

    @Test
    fun test_transPoint() {
        val trans = BorderTransform(1)
        val point = Point.of(10, 20)
        val rectangle = Rectangle.of(-5, 5, -5, 5, 0)
        assertEquals(Point.of(11, 21), trans.transform(point, rectangle))
        assertEquals(Point.of(9, 19), trans.reverse(point, rectangle))
        assertEquals(Point.of(11, 21), trans.reversed().reverse(point, rectangle))
        assertEquals(Point.of(9, 19), trans.reversed().transform(point, rectangle))
        assertEquals(point, trans.reverse(trans.transform(point, rectangle), rectangle))
        assertEquals(point, trans.transform(trans.reverse(point, rectangle), rectangle))
    }

    @Test
    fun test_Image1() {
        val name = "Image1"
        @Suppress("UseJBColor")
        val trans = BorderTransform(0, 0, Color.BLACK, null)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image2() {
        val name = "Image2"
        @Suppress("UseJBColor")
        val trans = BorderTransform(0, 10, Color.BLACK, null)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image3() {
        val name = "Image3"
        val trans = BorderTransform(1, 10, null, null)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image4() {
        val name = "Image4"
        val trans = BorderTransform(1, 10, TRANSPARENT, null)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image5() {
        val name = "Image5"
        @Suppress("UseJBColor")
        val trans = BorderTransform(1, 0, Color.BLACK, null)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image6() {
        val name = "Image6"
        @Suppress("UseJBColor")
        val trans = BorderTransform(2, 0, Color.BLACK, null)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image7() {
        val name = "Image7"
        @Suppress("UseJBColor")
        val trans = BorderTransform(1, 10, Color.BLACK, null)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image8() {
        val name = "Image8"
        @Suppress("UseJBColor")
        val trans = BorderTransform(2, 20, Color.BLACK, null)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }
}
