package com.vladsch.plugin.util.image

import org.junit.Assert.assertEquals
import org.junit.Test

class TranslationTransformTest : ImageTest() {

    @Test
    fun test_transImage() {
        val trans = TranslationTransform(2, -3)
        val rect = Rectangle.of(0, 10, 0, 20, 2)
        assertEquals(Rectangle.of(0, 10, 0, 20, 2), trans.transformBounds(rect))
        assertEquals(Rectangle.of(0, 10, 0, 20, 2), trans.reverseBounds(rect))
        assertEquals(Rectangle.of(0, 10, 0, 20, 2), trans.reversed().reverseBounds(rect))
        assertEquals(Rectangle.of(0, 10, 0, 20, 2), trans.reversed().transformBounds(rect))
        assertEquals(rect, trans.transformBounds(trans.reverseBounds(rect)))
        assertEquals(rect, trans.reverseBounds(trans.transformBounds(rect)))
    }

    @Test
    fun test_transRect() {
        val trans = TranslationTransform(2, -3)
        val rect = Rectangle.of(0, 10, 0, 20, 3)
        assertEquals(Rectangle.of(-2, 8, -3, 17, 3), trans.transform(rect, rect))
        assertEquals(Rectangle.of(2, 12, 3, 23, 3), trans.reverse(rect, rect))
        assertEquals(Rectangle.of(-2, 8, -3, 17, 3), trans.reversed().reverse(rect, rect))
        assertEquals(Rectangle.of(2, 12, 3, 23, 3), trans.reversed().transform(rect, rect))
        assertEquals(rect, trans.reverse(trans.transform(rect, rect), rect))
        assertEquals(rect, trans.transform(trans.reverse(rect, rect), rect))
    }

    @Test
    fun test_transPoint() {
        val trans = TranslationTransform(2, -3)
        val point = Point.of(10, 20)
        val rect = Rectangle.of(-5, 5, -5, 5, 0)
        assertEquals(Point.of(8, 17), trans.transform(point, rect))
        assertEquals(Point.of(12, 23), trans.reverse(point, rect))
        assertEquals(Point.of(8, 17), trans.reversed().reverse(point, rect))
        assertEquals(Point.of(12, 23), trans.reversed().transform(point, rect))
        assertEquals(point, trans.reverse(trans.transform(point, rect), rect))
        assertEquals(point, trans.transform(trans.reverse(point, rect), rect))
    }

    @Test
    fun test_Image1() {
        val name = "Image1"
        val trans = TranslationTransform(2, -3)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image2() {
        val name = "Image2"
        val trans = TranslationTransform(2, -3)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image3() {
        val name = "Image3"
        val trans = TranslationTransform(2, -3)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image4() {
        val name = "Image4"
        val trans = TranslationTransform(2, -3)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }
}
