package com.vladsch.plugin.util.image

import org.junit.Assert.assertEquals
import org.junit.Test
import java.awt.Color

class BorderTransformTest : ImageTest() {

    @Test
    fun test_transImage() {
        val trans = BorderTransform(1)
        val rect = Rectangle.of(0, 10, 0, 20, 0)
        assertEquals(Rectangle.of(0, 12, 0, 22, 0), trans.transformBounds(rect))
        assertEquals(Rectangle.of(0, 8, 0, 18, 0), trans.reverseBounds(rect))
        assertEquals(Rectangle.of(0, 12, 0, 22, 0), trans.reversed().reverseBounds(rect))
        assertEquals(Rectangle.of(0, 8, 0, 18, 0), trans.reversed().transformBounds(rect))
        assertEquals(rect, trans.transformBounds(trans.reverseBounds(rect)))
        assertEquals(rect, trans.reverseBounds(trans.transformBounds(rect)))
    }

    @Test
    fun test_transRect() {
        val trans = BorderTransform(1)
        val rect = Rectangle.of(0, 10, 0, 20, 1)
        assertEquals(Rectangle.of(1, 11, 1, 21, 1), trans.transform(rect))
        assertEquals(Rectangle.of(-1, 9, -1, 19, 1), trans.reverse(rect))
        assertEquals(Rectangle.of(1, 11, 1, 21, 1), trans.reversed().reverse(rect))
        assertEquals(Rectangle.of(-1, 9, -1, 19, 1), trans.reversed().transform(rect))
        assertEquals(rect, trans.reverse(trans.transform(rect)))
        assertEquals(rect, trans.transform(trans.reverse(rect)))
    }

    @Test
    fun test_transPoint() {
        val trans = BorderTransform(1)
        val point = Point.of(10, 20)
        assertEquals(Point.of(11, 21), trans.transform(point))
        assertEquals(Point.of(9, 19), trans.reverse(point))
        assertEquals(Point.of(11, 21), trans.reversed().reverse(point))
        assertEquals(Point.of(9, 19), trans.reversed().transform(point))
        assertEquals(point, trans.reverse(trans.transform(point)))
        assertEquals(point, trans.transform(trans.reverse(point)))
    }

    @Test
    fun test_Image1() {
        val trans = BorderTransform(0, 0, Color.BLACK)
        val name = "Image1"
        val image = getSourceImage(name)
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image2() {
        val trans = BorderTransform(0, 10, Color.BLACK)
        val name = "Image2"
        val image = getSourceImage(name)
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image3() {
        val trans = BorderTransform(1, 10, null)
        val name = "Image3"
        val image = getSourceImage(name)
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image4() {
        val trans = BorderTransform(1, 10, TRANSPARENT)
        val name = "Image4"
        val image = getSourceImage(name)
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image5() {
        val trans = BorderTransform(1, 0, Color.BLACK)
        val name = "Image5"
        val image = getSourceImage(name)
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image6() {
        val trans = BorderTransform(2, 0, Color.BLACK)
        val name = "Image6"
        val image = getSourceImage(name)
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image7() {
        val trans = BorderTransform(1, 10, Color.BLACK)
        val name = "Image7"
        val image = getSourceImage(name)
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image8() {
        val trans = BorderTransform(2, 20, Color.BLACK)
        val name = "Image8"
        val image = getSourceImage(name)
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }
}
