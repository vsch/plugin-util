package com.vladsch.plugin.util.image

import org.junit.Assert.assertEquals
import org.junit.Test
import java.awt.Color

class TransformListTest : ImageTest() {

    @Test
    fun test_transImage() {
        val scale = ScaleTransform(2f, 5f)
        val crop = CropTransform(Rectangle.of(1, 2, 5, 0, 0))
        val border = BorderTransform(1)
        val trans = TransformList(listOf(crop, scale, border))

        val rect = Rectangle.of(0, 10, 0, 20, 1)
        assertEquals(border.transformBounds(scale.transformBounds(crop.transformBounds(rect))), trans.transformBounds(rect))
        assertEquals(crop.reverseBounds(scale.reverseBounds(border.reverseBounds(rect))), trans.reverseBounds(rect))
        assertEquals(border.transformBounds(scale.transformBounds(crop.transformBounds(rect))), trans.reversed().reverseBounds(rect))
        assertEquals(crop.reverseBounds(scale.reverseBounds(border.reverseBounds(rect))), trans.reversed().transformBounds(rect))
        assertEquals(rect, trans.transformBounds(trans.reverseBounds(rect)).roundTo(0.00001f))
        assertEquals(rect, trans.reverseBounds(trans.transformBounds(rect)).roundTo(0.00001f))
    }

    @Test
    fun test_rotateImage() {
        val rotate = RotateTransform(90)
        val trans = TransformList(listOf(rotate))

        val rect = Rectangle.of(0, 10, 0, 20, 1)
        assertEquals(rotate.transform(rect, rect), trans.transform(rect, rect))
        assertEquals(rotate.reverse(rect, rect), trans.reverse(rect, rect))
        assertEquals(rotate.transform(rect, rect), trans.reversed().reverse(rect, rect))
        assertEquals(rotate.reverse(rect, rect), trans.reversed().transform(rect, rect))
        assertEquals(rect, trans.reverse(trans.transform(rect, rect), trans.transform(rect, rect)).roundTo(0.00001f))
        assertEquals(rect, trans.transform(trans.reverse(rect, rect), trans.reverse(rect, rect)).roundTo(0.00001f))
    }

    @Test
    fun test_transRect() {
        val scale = ScaleTransform(2f, 5f)
        val crop = CropTransform(Rectangle.of(1, 2, 5, 0, 0))
        val border = BorderTransform(1)
        val trans = TransformList(listOf(crop, scale, border))

        val rect = Rectangle.of(0, 10, 0, 20, 2)

        println("trans rect: $rect transformed: ${trans.transform(rect, rect)}")
        println("trans rect: $rect reversed: ${trans.reverse(rect, rect)}")

        assertEquals(border.transform(scale.transform(crop.transform(rect, rect), rect), rect), trans.transform(rect, rect))
        assertEquals(crop.reverse(scale.reverse(border.reverse(rect, rect), rect), rect), trans.reverse(rect, rect))
        assertEquals(border.transform(scale.transform(crop.transform(rect, rect), rect), rect), trans.reversed().reverse(rect, rect))
        assertEquals(crop.reverse(scale.reverse(border.reverse(rect, rect), rect), rect), trans.reversed().transform(rect, rect))
        assertEquals(rect, trans.reverse(trans.transform(rect, rect), trans.transform(rect, rect)).roundTo(0.00001f))
        assertEquals(rect, trans.transform(trans.reverse(rect, rect), trans.reverse(rect, rect)).roundTo(0.00001f))
    }

    @Test
    fun test_rotateRect() {
        val rotate = RotateTransform(90)
        val trans = TransformList(listOf(rotate))

        val rect = Rectangle.of(0, 10, 0, 20, 2)

        println("trans rect: $rect transformed: ${trans.transform(rect, rect)}")
        println("trans rect: $rect reversed: ${trans.reverse(rect, rect)}")

        assertEquals(rotate.transform(rect, rect), trans.transform(rect, rect))
        assertEquals(rotate.reverse(rect, rect), trans.reverse(rect, rect))
        assertEquals(rotate.transform(rect, rect), trans.reversed().reverse(rect, rect))
        assertEquals(rotate.reverse(rect, rect), trans.reversed().transform(rect, rect))

        assertEquals(rect, trans.reverse(trans.transform(rect, rect), trans.transform(rect, rect)).roundTo(0.00001f))
        assertEquals(rect, trans.transform(trans.reverse(rect, rect), trans.reverse(rect, rect)).roundTo(0.00001f))
    }

    @Test
    fun test_transPoint() {
        val scale = ScaleTransform(2f, 5f)
        val crop = CropTransform(Rectangle.of(1, 2, 5, 0, 0))
        val border = BorderTransform(1)
        val trans = TransformList(listOf(crop, scale, border))

        val point = Point.of(10, 20)
        val rect = Rectangle.of(0, 50, 0, 50, 0)
        assertEquals(border.transform(scale.transform(crop.transform(point, rect), rect), rect), trans.transform(point, rect))
        assertEquals(crop.reverse(scale.reverse(border.reverse(point, rect), rect), rect), trans.reverse(point, rect))
        assertEquals(border.transform(scale.transform(crop.transform(point, rect), rect), rect), trans.reversed().reverse(point, rect))
        assertEquals(crop.reverse(scale.reverse(border.reverse(point, rect), rect), rect), trans.reversed().transform(point, rect))
        assertEquals(point, trans.reverse(trans.transform(point, rect), trans.transform(rect, rect)))
    }

    @Test
    fun test_rotatePoint() {
        val rotate = RotateTransform(-90)
        val trans = TransformList(listOf(rotate))
        val rect = Rectangle.of(0, 50, 0, 50, 0)

        val point = rect.center()

        assertEquals(rotate.transform(point, rect), trans.transform(point, rect))
        assertEquals(rotate.reverse(point, rect), trans.reverse(point, rect))
        assertEquals(rotate.transform(point, rect), trans.reversed().reverse(point, rect))
        assertEquals(rotate.reverse(point, rect), trans.reversed().transform(point, rect))
        
        System.out.println(point)
        System.out.println(trans.transform(point, rect))
        System.out.println(trans.reverse(trans.transform(point, rect), trans.transform(rect, rect)))
        
        assertEquals(point, trans.transform(point, rect))
        assertEquals(point, trans.reverse(trans.transform(point, rect), trans.transform(rect, rect)))
    }

    @Test
    fun test_rotatePoint2() {
        val rotate = RotateTransform(-90)
        val trans = TransformList(listOf(rotate))
        val rect = Rectangle.of(0, 50, 0, 50, 0)

        val point = Point.of(10, 20)
        assertEquals(rotate.transform(point, rect), trans.transform(point, rect))
        assertEquals(rotate.reverse(point, rect), trans.reverse(point, rect))
        assertEquals(rotate.transform(point, rect), trans.reversed().reverse(point, rect))
        assertEquals(rotate.reverse(point, rect), trans.reversed().transform(point, rect))
        assertEquals(point, trans.reverse(trans.transform(point, rect), trans.transform(rect, rect)))
    }

    @Test
    fun test_Image1() {
        val name = "image1"
        val scale = ScaleTransform(2f, 5f)
        val crop = CropTransform(Rectangle.of(15, 20, 5, 10, 0))
        val border = BorderTransform(2, 0, Color.RED, null)
        val trans = TransformList(listOf(crop, scale, border))

        val image = getSourceImage("image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image2() {
        val name = "image2"
        val scale = ScaleTransform(.5f, .5f)
        val crop = CropTransform(Rectangle.of(16, 21, 6, 11, 1))
        val border = BorderTransform(3, 10, Color.RED, null)
        val trans = TransformList(listOf(crop, scale, border))

        val image = getSourceImage("image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image3() {
        val name = "image3"
        val scale = ScaleTransform(1f, 1f)
        val crop = CropTransform(Rectangle.of(17, 22, 7, 12, 2))
        val border = BorderTransform(3, 20, Color.RED, null)
        val trans = TransformList(listOf(crop, scale, border))

        val image = getSourceImage("image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image4() {
        val name = "image4"
        val scale = ScaleTransform(1.5f, 1.5f)
        val crop = CropTransform(Rectangle.of(18, 23, 8, 13, 3))
        val border = BorderTransform(1, 10, Color.RED, null)
        val trans = TransformList(listOf(crop, scale, border))

        val image = getSourceImage("image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image5() {
        val name = "image5"
        val rotate = RotateTransform(90)
        val trans = TransformList(listOf(rotate))

        val image = getSourceImage("image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image6() {
        val name = "image6"
        val rotate = RotateTransform(-90)
        val trans = TransformList(listOf(rotate))

        val image = getSourceImage("image1")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image7() {
        val name = "image7"
        val rotate = RotateTransform(90)
        val trans = TransformList(listOf(rotate))

        val image = getSourceImage("image2")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image8() {
        val name = "image8"
        val rotate = RotateTransform(-90)
        val trans = TransformList(listOf(rotate))

        val image = getSourceImage("image2")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image9() {
        val name = "image9"
        val rotate = RotateTransform(30)
        val trans = TransformList(listOf(rotate))

        val image = getSourceImage("image2")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_Image10() {
        val name = "image10"
        val rotate = RotateTransform(-30)
        val trans = TransformList(listOf(rotate))

        val image = getSourceImage("image2")
        val actual = trans.transform(image)
        assertImagesEqual(name, actual)
    }
}

