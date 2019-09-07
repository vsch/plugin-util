package com.vladsch.plugin.util.image

import java.awt.Color
import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
open class BorderTransform(val borderWidth: Int, val cornerRadius: Int, val borderColor: Color?) : Transform {

    constructor(borderWidth: Int) : this(borderWidth, 0, null)
    constructor(borderWidth: Int, cornerRadius: Int) : this(borderWidth, cornerRadius, null)
    constructor(borderWidth: Float, cornerRadius: Float, borderColor: Color) : this(borderWidth.toInt(), cornerRadius.toInt(), borderColor)
    constructor(borderWidth: Float, cornerRadius: Float) : this(borderWidth.toInt(), cornerRadius.toInt())
    constructor(borderWidth: Float) : this(borderWidth.toInt())

    override fun transform(image: BufferedImage): BufferedImage {
        if (borderWidth <= 0 || borderColor == null || borderColor.alpha == 0) return image

        var bufferedImage = image
        if (cornerRadius > 0) bufferedImage = ImageUtils.makeRoundedCorner(bufferedImage, cornerRadius, borderWidth)
        return ImageUtils.addBorder(bufferedImage, borderColor, borderWidth, cornerRadius)
    }

    override fun isEmpty(): Boolean {
        return borderWidth == 0 || cornerRadius == 0 || borderColor == null || borderColor.alpha == 0
    }

    override fun imageBorders(shape: DrawingShape): DrawingShape {
        val drawingShape = DrawingShape(transformBounds(shape.rectangle).withMaxRadius(cornerRadius), borderWidth, borderColor, shape.fillColor)
        return drawingShape.withMaxBorderWidth(shape.borderWidth, shape.borderColor)
    }

    override fun transformBounds(rectangle: Rectangle): Rectangle {
        return rectangle.grow(borderWidth).topLeftTo0()
    }

    override fun reverseBounds(rectangle: Rectangle): Rectangle {
        return rectangle.grow(-borderWidth).topLeftTo0()
    }

    override fun transform(rectangle: Rectangle): Rectangle {
        return rectangle.translate(borderWidth, borderWidth)
    }

    override fun reverse(rectangle: Rectangle): Rectangle {
        return rectangle.translate(-borderWidth, -borderWidth)
    }

    override fun transform(point: Point): Point {
        return point.translate(borderWidth, borderWidth)
    }

    override fun reverse(point: Point): Point {
        return point.translate(-borderWidth, -borderWidth)
    }

    override fun reversed(): BorderTransform {
        return BorderTransform(-borderWidth, cornerRadius, borderColor)
    }
}
