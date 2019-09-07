/*
 *
 */

package com.vladsch.plugin.util.image

import org.junit.Test
import java.awt.Color

class SimpleSelectableShapeTest : ImageTest() {

    @Test
    fun test_drawRect1() {
        val name = "drawRect1"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, null)
        val actual = shape.drawShape(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawRect2() {
        val name = "drawRect2"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, TRANSPARENT)
        val actual = shape.drawShape(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawRect3() {
        val name = "drawRect3"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, Color.RED)
        val actual = shape.drawShape(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawRect4() {
        val name = "drawRect4"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 1, Color.BLACK, null)
        val actual = shape.drawShape(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawRect5() {
        val name = "drawRect5"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, TRANSPARENT)
        val actual = shape.drawShape(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawRect6() {
        val name = "drawRect6"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 3, Color.BLACK, Color.WHITE)
        val actual = shape.drawShape(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawRect7() {
        val name = "drawRect7"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, Color.WHITE)
        val actual = shape.drawShape(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawRect8() {
        val name = "drawRect8"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, Color(164, 0, 128, 64))
        val actual = shape.drawShape(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawOval1() {
        val name = "drawOval1"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 0, Color.BLACK, null)
        val actual = shape.drawShape(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawOval2() {
        val name = "drawOval2"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 0, Color.BLACK, TRANSPARENT)
        val actual = shape.drawShape(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawOval3() {
        val name = "drawOval3"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 0, Color.BLACK, Color.RED)
        val actual = shape.drawShape(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawOval4() {
        val name = "drawOval4"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 1, Color.BLACK, null)
        val actual = shape.drawShape(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawOval5() {
        val name = "drawOval5"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 2, Color.BLACK, TRANSPARENT)
        val actual = shape.drawShape(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawOval6() {
        val name = "drawOval6"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 3, Color.BLACK, Color.WHITE)
        val actual = shape.drawShape(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawOval7() {
        val name = "drawOval7"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 2, Color.BLACK, Color.WHITE)
        val actual = shape.drawShape(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_drawOval8() {
        val name = "drawOval8"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 2, Color.BLACK, Color(164, 0, 128, 64))
        val actual = shape.drawShape(image)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchRect1() {
        val name = "punchRect1"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, null)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchRect2() {
        val name = "punchRect2"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, TRANSPARENT)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchRect3() {
        val name = "punchRect3"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, Color.RED)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchRect4() {
        val name = "punchRect4"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 1, Color.BLACK, null)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchRect5() {
        val name = "punchRect5"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, TRANSPARENT)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchRect6() {
        val name = "punchRect6"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 3, Color.BLACK, Color.WHITE)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchRect7() {
        val name = "punchRect7"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, Color.WHITE)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchRect8() {
        val name = "punchRect8"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, Color(164, 0, 128, 64))
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOval1() {
        val name = "punchOval1"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 0, Color.BLACK, null)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOval2() {
        val name = "punchOval2"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 0, Color.BLACK, TRANSPARENT)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOval3() {
        val name = "punchOval3"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 0, Color.BLACK, Color.RED)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOval4() {
        val name = "punchOval4"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 1, Color.BLACK, null)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOval5() {
        val name = "punchOval5"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 2, Color.BLACK, TRANSPARENT)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOval6() {
        val name = "punchOval6"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 3, Color.BLACK, Color.WHITE)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOval7() {
        val name = "punchOval7"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 2, Color.BLACK, Color.WHITE)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punchOval8() {
        val name = "punchOval8"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 2, Color.BLACK, Color(164, 0, 128, 64))
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        val actual = shape.punchOutShape(image, null, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_selectRect1() {
        val name = "selectRect1"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, null)

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

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, TRANSPARENT)
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

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, Color.RED)
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

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 1, Color.BLACK, null)
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

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, TRANSPARENT)
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

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 3, Color.BLACK, Color.WHITE)
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

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, Color.WHITE)
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

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, Color(164, 0, 128, 64))
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

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 0, Color.BLACK, null)
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

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 0, Color.BLACK, TRANSPARENT)
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

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 0, Color.BLACK, Color.RED)
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

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 1, Color.BLACK, null)
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

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 2, Color.BLACK, TRANSPARENT)
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

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 3, Color.BLACK, Color.WHITE)
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

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 2, Color.BLACK, Color.WHITE)
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

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 2, Color.BLACK, Color(164, 0, 128, 64))
        val actual1 = shape.drawShape(image, true, 1f); assertImagesEqual(name + "_1", actual1)
        val actual2 = shape.drawShape(image, true, 2f); assertImagesEqual(name + "_2", actual2)
        val actual3 = shape.drawShape(image, true, 3f); assertImagesEqual(name + "_3", actual3)
        val actual4 = shape.drawShape(image, true, 4f); assertImagesEqual(name + "_4", actual4)
    }

    @Test
    fun test_punch2xRect1() {
        val name = "punch2xRect1"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, null)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        var actual = shape.punchOutShape(image, null, outerFill, false)
        actual = shape.transformedBy(TranslateTransform(40, 20)).punchOutShape(image, actual, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punch2xRect2() {
        val name = "punch2xRect2"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, TRANSPARENT)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        var actual = shape.punchOutShape(image, null, outerFill, false)
        actual = shape.transformedBy(TranslateTransform(40, 20)).punchOutShape(image, actual, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punch2xRect3() {
        val name = "punch2xRect3"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 0, Color.BLACK, Color.RED)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        var actual = shape.punchOutShape(image, null, outerFill, false)
        actual = shape.transformedBy(TranslateTransform(40, 20)).punchOutShape(image, actual, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punch2xRect4() {
        val name = "punch2xRect4"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 1, Color.BLACK, null)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        var actual = shape.punchOutShape(image, null, outerFill, false)
        actual = shape.transformedBy(TranslateTransform(40, 20)).punchOutShape(image, actual, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punch2xRect5() {
        val name = "punch2xRect5"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, TRANSPARENT)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        var actual = shape.punchOutShape(image, null, outerFill, false)
        actual = shape.transformedBy(TranslateTransform(40, 20)).punchOutShape(image, actual, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punch2xRect6() {
        val name = "punch2xRect6"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 3, Color.BLACK, Color.WHITE)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        var actual = shape.punchOutShape(image, null, outerFill, false)
        actual = shape.transformedBy(TranslateTransform(40, 20)).punchOutShape(image, actual, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punch2xRect7() {
        val name = "punch2xRect7"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, Color.WHITE)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        var actual = shape.punchOutShape(image, null, outerFill, false)
        actual = shape.transformedBy(TranslateTransform(40, 20)).punchOutShape(image, actual, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punch2xRect8() {
        val name = "punch2xRect8"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = SimpleSelectableShape(ShapeType.RECTANGLE, rect, 2, Color.BLACK, Color(164, 0, 128, 64))
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        var actual = shape.punchOutShape(image, null, outerFill, false)
        actual = shape.transformedBy(TranslateTransform(40, 20)).punchOutShape(image, actual, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punch2xOval1() {
        val name = "punch2xOval1"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 0, Color.BLACK, null)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        var actual = shape.punchOutShape(image, null, outerFill, false)
        actual = shape.transformedBy(TranslateTransform(40, 20)).punchOutShape(image, actual, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punch2xOval2() {
        val name = "punch2xOval2"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 0, Color.BLACK, TRANSPARENT)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        var actual = shape.punchOutShape(image, null, outerFill, false)
        actual = shape.transformedBy(TranslateTransform(40, 20)).punchOutShape(image, actual, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punch2xOval3() {
        val name = "punch2xOval3"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 0, Color.BLACK, Color.RED)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        var actual = shape.punchOutShape(image, null, outerFill, false)
        actual = shape.transformedBy(TranslateTransform(40, 20)).punchOutShape(image, actual, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punch2xOval4() {
        val name = "punch2xOval4"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 1, Color.BLACK, null)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        var actual = shape.punchOutShape(image, null, outerFill, false)
        actual = shape.transformedBy(TranslateTransform(40, 20)).punchOutShape(image, actual, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punch2xOval5() {
        val name = "punch2xOval5"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 0)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 2, Color.BLACK, TRANSPARENT)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        var actual = shape.punchOutShape(image, null, outerFill, false)
        actual = shape.transformedBy(TranslateTransform(40, 20)).punchOutShape(image, actual, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punch2xOval6() {
        val name = "punch2xOval6"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 10)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 3, Color.BLACK, Color.WHITE)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        var actual = shape.punchOutShape(image, null, outerFill, false)
        actual = shape.transformedBy(TranslateTransform(40, 20)).punchOutShape(image, actual, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punch2xOval7() {
        val name = "punch2xOval7"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 2, Color.BLACK, Color.WHITE)
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        var actual = shape.punchOutShape(image, null, outerFill, false)
        actual = shape.transformedBy(TranslateTransform(40, 20)).punchOutShape(image, actual, outerFill, true)
        assertImagesEqual(name, actual)
    }

    @Test
    fun test_punch2xOval8() {
        val name = "punch2xOval8"
        val image = getSourceImage("Image1")
        val rect = Rectangle.of(10, 100, 20, 60, 20)

        val shape = SimpleSelectableShape(ShapeType.OVAL, rect, 2, Color.BLACK, Color(164, 0, 128, 64))
        val outerFill = DrawingShape(Rectangle.NULL, 0, null, TRANSLUCENT)
        var actual = shape.punchOutShape(image, null, outerFill, false)
        actual = shape.transformedBy(TranslateTransform(40, 20)).punchOutShape(image, actual, outerFill, true)
        assertImagesEqual(name, actual)
    }
}

