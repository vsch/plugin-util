package com.vladsch.plugin.util.image

import org.junit.Test
import java.awt.Color

class BorderedSelectableShapeTest : ImageTest() {

    @Test
    fun test_drawRect1() {
        val name = "drawRect1"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, null)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawRect2() {
        val name = "drawRect2"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, TRANSPARENT)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawRect3() {
        val name = "drawRect3"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, Color.RED)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawRect4() {
        val name = "drawRect4"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 1, Color.BLACK, null)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawRect5() {
        val name = "drawRect5"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, TRANSPARENT)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawRect6() {
        val name = "drawRect6"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 3, Color.BLACK, Color.WHITE)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawRect7() {
        val name = "drawRect7"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, Color.WHITE)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawRect8() {
        val name = "drawRect8"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, Color(164, 0, 128, 64))
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawOval1() {
        val name = "drawOval1"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.OVAL, rect, 0, Color.BLACK, null)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawOval2() {
        val name = "drawOval2"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.OVAL, rect, 0, Color.BLACK, TRANSPARENT)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawOval3() {
        val name = "drawOval3"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.OVAL, rect, 0, Color.BLACK, Color.RED)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawOval4() {
        val name = "drawOval4"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.OVAL, rect, 1, Color.BLACK, null)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawOval5() {
        val name = "drawOval5"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.OVAL, rect, 2, Color.BLACK, TRANSPARENT)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawOval6() {
        val name = "drawOval6"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.OVAL, rect, 3, Color.BLACK, Color.WHITE)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawOval7() {
        val name = "drawOval7"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.OVAL, rect, 2, Color.BLACK, Color.WHITE)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawOval8() {
        val name = "drawOval8"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.OVAL, rect, 2, Color.BLACK, Color(164, 0, 128, 64))
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchRect1() {
        val name = "punchRect1"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, null)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchRect2() {
        val name = "punchRect2"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, TRANSPARENT)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchRect3() {
        val name = "punchRect3"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, Color.RED)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchRect4() {
        val name = "punchRect4"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 1, Color.BLACK, null)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchRect5() {
        val name = "punchRect5"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, TRANSPARENT)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchRect6() {
        val name = "punchRect6"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 3, Color.BLACK, Color.WHITE)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchRect7() {
        val name = "punchRect7"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, Color.WHITE)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchRect8() {
        val name = "punchRect8"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, Color(164, 0, 128, 64))
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOval1() {
        val name = "punchOval1"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.OVAL, rect, 0, Color.BLACK, null)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOval2() {
        val name = "punchOval2"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.OVAL, rect, 0, Color.BLACK, TRANSPARENT)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOval3() {
        val name = "punchOval3"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.OVAL, rect, 0, Color.BLACK, Color.RED)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOval4() {
        val name = "punchOval4"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.OVAL, rect, 1, Color.BLACK, null)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOval5() {
        val name = "punchOval5"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.OVAL, rect, 2, Color.BLACK, TRANSPARENT)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOval6() {
        val name = "punchOval6"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.OVAL, rect, 3, Color.BLACK, Color.WHITE)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOval7() {
        val name = "punchOval7"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.OVAL, rect, 2, Color.BLACK, Color.WHITE)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOval8() {
        val name = "punchOval8"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.OVAL, rect, 2, Color.BLACK, Color(164, 0, 128, 64))
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_selectRect1() {
        val name = "selectRect1"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, null)

        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectRect2() {
        val name = "selectRect2"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, TRANSPARENT)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectRect3() {
        val name = "selectRect3"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, Color.RED)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectRect4() {
        val name = "selectRect4"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 1, Color.BLACK, null)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectRect5() {
        val name = "selectRect5"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, TRANSPARENT)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectRect6() {
        val name = "selectRect6"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 3, Color.BLACK, Color.WHITE)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectRect7() {
        val name = "selectRect7"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, Color.WHITE)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectRect8() {
        val name = "selectRect8"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, Color(164, 0, 128, 64))
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectOval1() {
        val name = "selectOval1"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.OVAL, rect, 0, Color.BLACK, null)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectOval2() {
        val name = "selectOval2"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.OVAL, rect, 0, Color.BLACK, TRANSPARENT)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectOval3() {
        val name = "selectOval3"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.OVAL, rect, 0, Color.BLACK, Color.RED)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectOval4() {
        val name = "selectOval4"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.OVAL, rect, 1, Color.BLACK, null)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectOval5() {
        val name = "selectOval5"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.OVAL, rect, 2, Color.BLACK, TRANSPARENT)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectOval6() {
        val name = "selectOval6"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.OVAL, rect, 3, Color.BLACK, Color.WHITE)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectOval7() {
        val name = "selectOval7"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.OVAL, rect, 2, Color.BLACK, Color.WHITE)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectOval8() {
        val name = "selectOval8"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.OVAL, rect, 2, Color.BLACK, Color(164, 0, 128, 64))
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_drawSquare1() {
        val name = "drawSquare1"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 0, Color.BLACK, null)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawSquare2() {
        val name = "drawSquare2"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 0, Color.BLACK, TRANSPARENT)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawSquare3() {
        val name = "drawSquare3"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 0, Color.BLACK, Color.RED)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawSquare4() {
        val name = "drawSquare4"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 1, Color.BLACK, null)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawSquare5() {
        val name = "drawSquare5"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 2, Color.BLACK, TRANSPARENT)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawSquare6() {
        val name = "drawSquare6"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 3, Color.BLACK, Color.WHITE)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawSquare7() {
        val name = "drawSquare7"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 2, Color.BLACK, Color.WHITE)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawSquare8() {
        val name = "drawSquare8"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 2, Color.BLACK, Color(164, 0, 128, 64))
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawCircle1() {
        val name = "drawCircle1"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 0, Color.BLACK, null)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawCircle2() {
        val name = "drawCircle2"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 0, Color.BLACK, TRANSPARENT)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawCircle3() {
        val name = "drawCircle3"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 0, Color.BLACK, Color.RED)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawCircle4() {
        val name = "drawCircle4"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 1, Color.BLACK, null)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawCircle5() {
        val name = "drawCircle5"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 2, Color.BLACK, TRANSPARENT)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawCircle6() {
        val name = "drawCircle6"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 3, Color.BLACK, Color.WHITE)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawCircle7() {
        val name = "drawCircle7"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 2, Color.BLACK, Color.WHITE)
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawCircle8() {
        val name = "drawCircle8"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 2, Color.BLACK, Color(164, 0, 128, 64))
        val actual = shape.drawShape(image, false, 0f)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchSquare1() {
        val name = "punchSquare1"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 0, Color.BLACK, null)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchSquare2() {
        val name = "punchSquare2"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 0, Color.BLACK, TRANSPARENT)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchSquare3() {
        val name = "punchSquare3"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 0, Color.BLACK, Color.RED)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchSquare4() {
        val name = "punchSquare4"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 1, Color.BLACK, null)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchSquare5() {
        val name = "punchSquare5"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 2, Color.BLACK, TRANSPARENT)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchSquare6() {
        val name = "punchSquare6"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 3, Color.BLACK, Color.WHITE)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchSquare7() {
        val name = "punchSquare7"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 2, Color.BLACK, Color.WHITE)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchSquare8() {
        val name = "punchSquare8"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 2, Color.BLACK, Color(164, 0, 128, 64))
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchCircle1() {
        val name = "punchCircle1"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 0, Color.BLACK, null)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchCircle2() {
        val name = "punchCircle2"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 0, Color.BLACK, TRANSPARENT)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchCircle3() {
        val name = "punchCircle3"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 0, Color.BLACK, Color.RED)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchCircle4() {
        val name = "punchCircle4"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 1, Color.BLACK, null)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchCircle5() {
        val name = "punchCircle5"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 2, Color.BLACK, TRANSPARENT)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchCircle6() {
        val name = "punchCircle6"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 3, Color.BLACK, Color.WHITE)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchCircle7() {
        val name = "punchCircle7"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 2, Color.BLACK, Color.WHITE)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchCircle8() {
        val name = "punchCircle8"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 2, Color.BLACK, Color(164, 0, 128, 64))
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_selectSquare1() {
        val name = "selectSquare1"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 0, Color.BLACK, null)

        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectSquare2() {
        val name = "selectSquare2"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 0, Color.BLACK, TRANSPARENT)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectSquare3() {
        val name = "selectSquare3"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 0, Color.BLACK, Color.RED)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectSquare4() {
        val name = "selectSquare4"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 1, Color.BLACK, null)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectSquare5() {
        val name = "selectSquare5"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 2, Color.BLACK, TRANSPARENT)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectSquare6() {
        val name = "selectSquare6"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 3, Color.BLACK, Color.WHITE)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectSquare7() {
        val name = "selectSquare7"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 2, Color.BLACK, Color.WHITE)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectSquare8() {
        val name = "selectSquare8"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.SQUARE, rect, 2, Color.BLACK, Color(164, 0, 128, 64))
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectCircle1() {
        val name = "selectCircle1"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 0, Color.BLACK, null)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectCircle2() {
        val name = "selectCircle2"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 0, Color.BLACK, TRANSPARENT)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectCircle3() {
        val name = "selectCircle3"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 0, Color.BLACK, Color.RED)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectCircle4() {
        val name = "selectCircle4"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 1, Color.BLACK, null)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectCircle5() {
        val name = "selectCircle5"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 2, Color.BLACK, TRANSPARENT)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectCircle6() {
        val name = "selectCircle6"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 3, Color.BLACK, Color.WHITE)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectCircle7() {
        val name = "selectCircle7"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 2, Color.BLACK, Color.WHITE)
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_selectCircle8() {
        val name = "selectCircle8"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = BorderedShape(ShapeType.CIRCLE, rect, 2, Color.BLACK, Color(164, 0, 128, 64))
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }
}
