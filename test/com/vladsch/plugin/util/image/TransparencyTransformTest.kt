package com.vladsch.plugin.util.image

import org.junit.Assert.assertEquals
import org.junit.Test
import java.awt.Color

class TransparencyTransformTest : ImageTest() {

    @Test
    fun test_transImage() {
        val trans = TransparencyTransform(Color.BLACK, 0)
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
        val trans = TransparencyTransform(Color.BLACK, 0)
        val rect = Rectangle.of(0, 10, 0, 20, 3)
        assertEquals(Rectangle.of(0, 10, 0, 20, 3), trans.transform(rect))
        assertEquals(Rectangle.of(0, 10, 0, 20, 3), trans.reverse(rect))
        assertEquals(Rectangle.of(0, 10, 0, 20, 3), trans.reversed().reverse(rect))
        assertEquals(Rectangle.of(0, 10, 0, 20, 3), trans.reversed().transform(rect))
        assertEquals(rect, trans.reverse(trans.transform(rect)))
        assertEquals(rect, trans.transform(trans.reverse(rect)))
    }

    @Test
    fun test_transPoint() {
        val trans = TransparencyTransform(Color.BLACK, 0)
        val point = Point.of(10, 20)
        assertEquals(Point.of(10, 20), trans.transform(point))
        assertEquals(Point.of(10, 20), trans.reverse(point))
        assertEquals(Point.of(10, 20), trans.reversed().reverse(point))
        assertEquals(Point.of(10, 20), trans.reversed().transform(point))
        assertEquals(point, trans.reverse(trans.transform(point)))
        assertEquals(point, trans.transform(trans.reverse(point)))
    }

    @Test
    fun test_Image1() {
        val name = "Image1"
        val trans = TransparencyTransform(Color.BLACK, 0)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual("Cropped", name, actual)
    }

    @Test
    fun test_Image2() {
        val name = "Image2"
        val trans = TransparencyTransform(Color.BLACK, 32)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual("Cropped", name, actual)
    }

    @Test
    fun test_Image3() {
        val name = "Image3"
        val trans = TransparencyTransform(Color.BLACK, 64)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual("Cropped", name, actual)
    }

    @Test
    fun test_Image4() {
        val name = "Image4"
        val trans = TransparencyTransform(Color.BLACK, 128)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual("Cropped", name, actual)
    }

    @Test
    fun test_Image5() {
        val name = "Image5"
        val trans = TransparencyTransform(Color.BLACK, 256)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual("Cropped", name, actual)
    }

    @Test
    fun test_Image6() {
        val name = "Image6"
        val trans = TransparencyTransform(Color(0xE8FCE3), 4, 4, Color.GRAY, Color.WHITE)
        val image = getSourceImage("Image1")
        val actual = trans.transform(image)
        assertImagesEqual("Cropped", name, actual)
    }
}
