package com.vladsch.plugin.util.image

import java.awt.Color
import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
open class BorderTransform(val borderWidth: Int, val cornerRadius: Int, val borderColor: Color?, val fillColor: Color?) : Transform {
    constructor(borderWidth: Int) : this(borderWidth, 0, null, null)
    constructor(borderWidth: Int, cornerRadius: Int) : this(borderWidth, cornerRadius, null, null)
    constructor(borderWidth: Float, cornerRadius: Float, borderColor: Color) : this(borderWidth.toInt(), cornerRadius.toInt(), borderColor, null)
    constructor(borderWidth: Float, cornerRadius: Float) : this(borderWidth.toInt(), cornerRadius.toInt())
    constructor(borderWidth: Float) : this(borderWidth.toInt())

    override fun transform(image: BufferedImage): BufferedImage {
        var bufferedImage = image

        if (cornerRadius > 0) bufferedImage = ImageUtils.makeRoundedCorner(bufferedImage, cornerRadius, borderWidth)
        if (borderWidth > 0 && borderColor != null && borderColor.alpha != 0) bufferedImage = ImageUtils.addBorder(bufferedImage, borderColor, borderWidth, cornerRadius)
        return bufferedImage
    }

    override fun isEmpty(): Boolean {
        return borderWidth == 0 || cornerRadius == 0 || borderColor == null || borderColor.alpha == 0
    }

    override fun imageBorders(shape: DrawingShape): DrawingShape {
        val drawingShape = DrawingShape(transformBounds(shape.rectangle).withMaxRadius(cornerRadius), borderWidth, borderColor, fillColor)
        return drawingShape.withMaxBorderWidth(shape.borderWidth, shape.borderColor).withNotNullFillColor(shape.fillColor)
    }

    override fun transformBounds(rectangle: Rectangle): Rectangle {
        return rectangle.grow(borderWidth).topLeftTo0()
    }

    override fun reverseBounds(rectangle: Rectangle): Rectangle {
        return rectangle.grow(-borderWidth).topLeftTo0()
    }

    override fun transform(rectangle: Rectangle, bounds: Rectangle): Rectangle {
        return rectangle.translate(borderWidth, borderWidth)
    }

    override fun reverse(rectangle: com.vladsch.plugin.util.image.Rectangle, bounds: com.vladsch.plugin.util.image.Rectangle): Rectangle {
        return rectangle.translate(-borderWidth, -borderWidth)
    }

    override fun transform(point: com.vladsch.plugin.util.image.Point, bounds: com.vladsch.plugin.util.image.Rectangle): Point {
        return point.translate(borderWidth, borderWidth)
    }

    override fun reverse(point: com.vladsch.plugin.util.image.Point, bounds: com.vladsch.plugin.util.image.Rectangle): Point {
        return point.translate(-borderWidth, -borderWidth)
    }

    override fun reversed(): BorderTransform {
        return BorderTransform(-borderWidth, cornerRadius, borderColor, null)
    }
}
