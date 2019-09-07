package com.vladsch.plugin.util.image

import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
open class ScaleTransform(val x: Float, val y: Float, val interpolationType: Int) : Transform {

    constructor(x: Int, y: Int, interpolationType: Int) : this(x.toFloat(), y.toFloat(), interpolationType)

    constructor(x: Float, y: Float) : this(x, y, 0)
    constructor(scale: Float, interpolationType: Int) : this(scale, scale, interpolationType)
    constructor(scale: Int, interpolationType: Int) : this(scale.toFloat(), scale.toFloat(), interpolationType)
    constructor(scale: Int) : this(scale, scale, 0)

    override fun transform(image: BufferedImage): BufferedImage {
        if (isEmpty || x < 0f && y < 0f) return image
        return ImageUtils.scaleImage(image, (image.width * x).toInt(), (image.height * y).toInt(), interpolationType)
    }

    override fun isEmpty(): Boolean {
        return x == 1f && y == 1f || x == 0f || y == 0f
    }

    override fun transformBounds(rectangle: Rectangle): Rectangle {
        if (isEmpty) return rectangle
        return rectangle.scale(x, y).nullIfInverted().topLeftTo0()
    }

    override fun reverseBounds(rectangle: Rectangle): Rectangle {
        if (isEmpty) return rectangle
        return rectangle.scale(1 / x, 1 / y).nullIfInverted().topLeftTo0()
    }

    override fun transform(rectangle: Rectangle): Rectangle {
        if (isEmpty) return rectangle
        return rectangle.scale(x, y)
    }

    override fun reverse(rectangle: Rectangle): Rectangle {
        if (isEmpty) return rectangle
        return rectangle.scale(1 / x, 1 / y)
    }

    override fun transform(point: Point): Point {
        if (isEmpty) return point
        return point.scale(x, y)
    }

    override fun reverse(point: Point): Point {
        if (isEmpty) return point
        return point.scale(1 / x, 1 / y)
    }

    override fun reversed(): ScaleTransform {
        if (isEmpty) return this
        return ScaleTransform(1 / x, 1 / y, interpolationType)
    }
}
