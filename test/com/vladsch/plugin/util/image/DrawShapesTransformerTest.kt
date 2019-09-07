/*
 *
 */

package com.vladsch.plugin.util.image

import org.junit.Test
import java.awt.Color

class DrawShapesTransformerTest : ImageTest() {
    @Test
    fun test_drawShapes1() {
        val name = "drawShapes1"
        val image = getSourceImage("Image1")

        val scale = ScaleTransform(1f, 1f)
        val crop = CropTransform(Rectangle.of(0, 0, 0, 0, 0))
        val border = BorderTransform(0, 0, Color.RED)
        val trans = Transformer(listOf(crop, scale, border))

        val shape1 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(10, 50, 10, 30, 0), 2, Color.BLUE, null)
        val shape2 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(30, 80, 20, 60, 20), 2, Color.BLACK, Color(164, 0, 128, 64))
        val shape3 = FancyBorderSelectableShape(ShapeType.OVAL, Rectangle.of(40, 100, 20, 60, 10), 2, Color.BLACK, Color(255, 255, 128, 64))
        val shapes = DrawShapesTransformer(trans, listOf(shape1, shape2, shape3))

        val actual = shapes.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawShapes2() {
        val name = "drawShapes2"
        val image = getSourceImage("Image1")

        val scale = ScaleTransform(2f, 2f)
        val crop = CropTransform(Rectangle.of(0, 0, 0, 0, 0))
        val border = BorderTransform(0, 0, Color.RED)
        val trans = Transformer(listOf(crop, scale, border))

        val shape1 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(10, 50, 10, 30, 0), 2, Color.BLUE, null)
        val shape2 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(30, 80, 20, 60, 20), 2, Color.BLACK, Color(164, 0, 128, 64))
        val shape3 = FancyBorderSelectableShape(ShapeType.OVAL, Rectangle.of(40, 100, 20, 60, 10), 2, Color.BLACK, Color(255, 255, 128, 64))
        val shapes = DrawShapesTransformer(trans, listOf(shape1, shape2, shape3))

        val actual = shapes.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawShapes3() {
        val name = "drawShapes3"
        val image = getSourceImage("Image1")

        val scale = ScaleTransform(.5f, .5f)
        val crop = CropTransform(Rectangle.of(0, 0, 0, 0, 0))
        val border = BorderTransform(0, 0, Color.RED)
        val trans = Transformer(listOf(crop, scale, border))

        val shape1 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(10, 50, 10, 30, 0), 2, Color.BLUE, null)
        val shape2 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(30, 80, 20, 60, 20), 2, Color.BLACK, Color(164, 0, 128, 64))
        val shape3 = FancyBorderSelectableShape(ShapeType.OVAL, Rectangle.of(40, 100, 20, 60, 10), 2, Color.BLACK, Color(255, 255, 128, 64))
        val shapes = DrawShapesTransformer(trans, listOf(shape1, shape2, shape3))

        val actual = shapes.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawShapes4() {
        val name = "drawShapes4"
        val image = getSourceImage("Image1")

        val scale = ScaleTransform(1f, 1f)
        val crop = CropTransform(Rectangle.of(10, 10, 5, 5, 0))
        val border = BorderTransform(0, 0, Color.RED)
        val trans = Transformer(listOf(crop, scale, border))

        val shape1 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(10, 50, 10, 30, 0), 2, Color.BLUE, null)
        val shape2 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(30, 80, 20, 60, 20), 2, Color.BLACK, Color(164, 0, 128, 64))
        val shape3 = FancyBorderSelectableShape(ShapeType.OVAL, Rectangle.of(40, 100, 20, 60, 10), 2, Color.BLACK, Color(255, 255, 128, 64))
        val shapes = DrawShapesTransformer(trans, listOf(shape1, shape2, shape3))

        val actual = shapes.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawShapes5() {
        val name = "drawShapes5"
        val image = getSourceImage("Image1")

        val scale = ScaleTransform(2f, 2f)
        val crop = CropTransform(Rectangle.of(10, 10, 5, 5, 0))
        val border = BorderTransform(0, 0, Color.RED)
        val trans = Transformer(listOf(crop, scale, border))

        val shape1 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(10, 50, 10, 30, 0), 2, Color.BLUE, null)
        val shape2 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(30, 80, 20, 60, 20), 2, Color.BLACK, Color(164, 0, 128, 64))
        val shape3 = FancyBorderSelectableShape(ShapeType.OVAL, Rectangle.of(40, 100, 20, 60, 10), 2, Color.BLACK, Color(255, 255, 128, 64))
        val shapes = DrawShapesTransformer(trans, listOf(shape1, shape2, shape3))

        val actual = shapes.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawShapes6() {
        val name = "drawShapes6"
        val image = getSourceImage("Image1")

        val scale = ScaleTransform(.6f, .6f)
        val crop = CropTransform(Rectangle.of(10, 10, 5, 5, 0))
        val border = BorderTransform(0, 0, Color.RED)
        val trans = Transformer(listOf(crop, scale, border))

        val shape1 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(10, 50, 10, 30, 0), 2, Color.BLUE, null)
        val shape2 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(30, 80, 20, 60, 20), 2, Color.BLACK, Color(164, 0, 128, 64))
        val shape3 = FancyBorderSelectableShape(ShapeType.OVAL, Rectangle.of(40, 100, 20, 60, 10), 2, Color.BLACK, Color(255, 255, 128, 64))
        val shapes = DrawShapesTransformer(trans, listOf(shape1, shape2, shape3))

        val actual = shapes.transform(image)
        assertImagesEqual(name, actual)
    }


    @Test
    fun test_drawShapes7() {
        val name = "drawShapes7"
        val image = getSourceImage("Image1")

        val scale = ScaleTransform(1f, 1f)
        val crop = CropTransform(Rectangle.of(0, 0, 0, 0, 0))
        val border = BorderTransform(5, 20, Color.RED)
        val trans = Transformer(listOf(crop, scale, border))

        val shape1 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(10, 50, 10, 30, 0), 2, Color.BLUE, null)
        val shape2 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(30, 80, 20, 60, 20), 2, Color.BLACK, Color(164, 0, 128, 64))
        val shape3 = FancyBorderSelectableShape(ShapeType.OVAL, Rectangle.of(40, 100, 20, 60, 10), 2, Color.BLACK, Color(255, 255, 128, 64))
        val shapes = DrawShapesTransformer(trans, listOf(shape1, shape2, shape3))

        val actual = shapes.transform(image)
        assertImagesEqual(name, actual)
    }


    @Test
    fun test_drawShapes8() {
        val name = "drawShapes8"
        val image = getSourceImage("Image1")

        val scale = ScaleTransform(2f, 2f)
        val crop = CropTransform(Rectangle.of(5, 10, 5, 15, 0))
        val border = BorderTransform(5, 20, Color.RED)
        val trans = Transformer(listOf(crop, scale, border))

        val shape1 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(10, 50, 10, 30, 0), 2, Color.BLUE, null)
        val shape2 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(30, 80, 20, 60, 20), 2, Color.BLACK, Color(164, 0, 128, 64))
        val shape3 = FancyBorderSelectableShape(ShapeType.OVAL, Rectangle.of(40, 100, 20, 60, 10), 2, Color.BLACK, Color(255, 255, 128, 64))
        val shapes = DrawShapesTransformer(trans, listOf(shape1, shape2, shape3))

        val actual = shapes.transform(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOutShapes1() {
        val name = "punchOutShapes1"
        val image = getSourceImage("Image1")

        val scale = ScaleTransform(1f, 1f)
        val crop = CropTransform(Rectangle.of(0, 0, 0, 0, 0))
        val border = BorderTransform(0, 0, Color.RED)
        val trans = Transformer(listOf(crop, scale, border))

        val shape1 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(10, 50, 10, 30, 0), 2, Color.BLUE, null)
        val shape2 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(30, 80, 20, 60, 20), 2, Color.BLACK, Color(164, 0, 128, 64))
        val shape3 = FancyBorderSelectableShape(ShapeType.OVAL, Rectangle.of(40, 100, 20, 60, 10), 2, Color.BLACK, Color(255, 255, 128, 64))
        val shapes = DrawShapesTransformer(trans, listOf(shape1, shape2, shape3))

        val actual = shapes.drawShapes(image, TRANSLUCENT, 2, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOutShapes2() {
        val name = "punchOutShapes2"
        val image = getSourceImage("Image1")

        val scale = ScaleTransform(2f, 2f)
        val crop = CropTransform(Rectangle.of(0, 0, 0, 0, 0))
        val border = BorderTransform(0, 0, Color.RED)
        val trans = Transformer(listOf(crop, scale, border))

        val shape1 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(10, 50, 10, 30, 0), 2, Color.BLUE, null)
        val shape2 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(30, 80, 20, 60, 20), 2, Color.BLACK, Color(164, 0, 128, 64))
        val shape3 = FancyBorderSelectableShape(ShapeType.OVAL, Rectangle.of(40, 100, 20, 60, 10), 2, Color.BLACK, Color(255, 255, 128, 64))
        val shapes = DrawShapesTransformer(trans, listOf(shape1, shape2, shape3))

        val actual = shapes.drawShapes(image, TRANSLUCENT, 2, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOutShapes3() {
        val name = "punchOutShapes3"
        val image = getSourceImage("Image1")

        val scale = ScaleTransform(.5f, .5f)
        val crop = CropTransform(Rectangle.of(0, 0, 0, 0, 0))
        val border = BorderTransform(0, 0, Color.RED)
        val trans = Transformer(listOf(crop, scale, border))

        val shape1 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(10, 50, 10, 30, 0), 2, Color.BLUE, null)
        val shape2 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(30, 80, 20, 60, 20), 2, Color.BLACK, Color(164, 0, 128, 64))
        val shape3 = FancyBorderSelectableShape(ShapeType.OVAL, Rectangle.of(40, 100, 20, 60, 10), 2, Color.BLACK, Color(255, 255, 128, 64))
        val shapes = DrawShapesTransformer(trans, listOf(shape1, shape2, shape3))

        val actual = shapes.drawShapes(image, TRANSLUCENT, 2, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOutShapes4() {
        val name = "punchOutShapes4"
        val image = getSourceImage("Image1")

        val scale = ScaleTransform(1f, 1f)
        val crop = CropTransform(Rectangle.of(10, 10, 5, 5, 0))
        val border = BorderTransform(0, 0, Color.RED)
        val trans = Transformer(listOf(crop, scale, border))

        val shape1 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(10, 50, 10, 30, 0), 2, Color.BLUE, null)
        val shape2 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(30, 80, 20, 60, 20), 2, Color.BLACK, Color(164, 0, 128, 64))
        val shape3 = FancyBorderSelectableShape(ShapeType.OVAL, Rectangle.of(40, 100, 20, 60, 10), 2, Color.BLACK, Color(255, 255, 128, 64))
        val shapes = DrawShapesTransformer(trans, listOf(shape1, shape2, shape3))

        val actual = shapes.drawShapes(image, TRANSLUCENT, 2, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOutShapes5() {
        val name = "punchOutShapes5"
        val image = getSourceImage("Image1")

        val scale = ScaleTransform(2f, 2f)
        val crop = CropTransform(Rectangle.of(10, 10, 5, 5, 0))
        val border = BorderTransform(0, 0, Color.RED)
        val trans = Transformer(listOf(crop, scale, border))

        val shape1 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(10, 50, 10, 30, 0), 2, Color.BLUE, null)
        val shape2 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(30, 80, 20, 60, 20), 2, Color.BLACK, Color(164, 0, 128, 64))
        val shape3 = FancyBorderSelectableShape(ShapeType.OVAL, Rectangle.of(40, 100, 20, 60, 10), 2, Color.BLACK, Color(255, 255, 128, 64))
        val shapes = DrawShapesTransformer(trans, listOf(shape1, shape2, shape3))

        val actual = shapes.drawShapes(image, TRANSLUCENT, 2, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOutShapes6() {
        val name = "punchOutShapes6"
        val image = getSourceImage("Image1")

        val scale = ScaleTransform(.6f, .6f)
        val crop = CropTransform(Rectangle.of(10, 10, 5, 5, 0))
        val border = BorderTransform(0, 0, Color.RED)
        val trans = Transformer(listOf(crop, scale, border))

        val shape1 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(10, 50, 10, 30, 0), 2, Color.BLUE, null)
        val shape2 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(30, 80, 20, 60, 20), 2, Color.BLACK, Color(164, 0, 128, 64))
        val shape3 = FancyBorderSelectableShape(ShapeType.OVAL, Rectangle.of(40, 100, 20, 60, 10), 2, Color.BLACK, Color(255, 255, 128, 64))
        val shapes = DrawShapesTransformer(trans, listOf(shape1, shape2, shape3))

        val actual = shapes.drawShapes(image, TRANSLUCENT, 2, 0f)
        assertImagesEqual(name, actual)
    }


    @Test
    fun test_punchOutShapes7() {
        val name = "punchOutShapes7"
        val image = getSourceImage("Image1")

        val scale = ScaleTransform(1f, 1f)
        val crop = CropTransform(Rectangle.of(0, 0, 0, 0, 0))
        val border = BorderTransform(5, 20, Color.RED)
        val trans = Transformer(listOf(crop, scale, border))

        val shape1 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(10, 50, 10, 30, 0), 2, Color.BLUE, null)
        val shape2 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(30, 80, 20, 60, 20), 2, Color.BLACK, Color(164, 0, 128, 64))
        val shape3 = FancyBorderSelectableShape(ShapeType.OVAL, Rectangle.of(40, 100, 20, 60, 10), 2, Color.BLACK, Color(255, 255, 128, 64))
        val shapes = DrawShapesTransformer(trans, listOf(shape1, shape2, shape3))

        val actual = shapes.drawShapes(image, TRANSLUCENT, 2, 0f)
        assertImagesEqual(name, actual)
    }


    @Test
    fun test_punchOutShapes8() {
        val name = "punchOutShapes8"
        val image = getSourceImage("Image1")

        val scale = ScaleTransform(2f, 2f)
        val crop = CropTransform(Rectangle.of(5, 10, 5, 15, 0))
        val border = BorderTransform(5, 20, Color.RED)
        val trans = Transformer(listOf(crop, scale, border))

        val shape1 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(10, 50, 10, 30, 0), 2, Color.BLUE, null)
        val shape2 = FancyBorderSelectableShape(ShapeType.RECTANGLE, Rectangle.of(30, 80, 20, 60, 20), 2, Color.BLACK, Color(164, 0, 128, 64))
        val shape3 = FancyBorderSelectableShape(ShapeType.OVAL, Rectangle.of(40, 100, 20, 60, 10), 2, Color.BLACK, Color(255, 255, 128, 64))
        val shapes = DrawShapesTransformer(trans, listOf(shape1, shape2, shape3))

        val actual = shapes.drawShapes(image, TRANSLUCENT, 2, 0f)
        assertImagesEqual(name, actual)
    }

}
