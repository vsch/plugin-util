package com.vladsch.plugin.util.image

import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
open class TranslationTransform(val x: Float, val y: Float) : Transform {

    constructor(x: Int, y: Int) : this(x.toFloat(), y.toFloat())

    override fun transform(image: BufferedImage): BufferedImage {
        return image
    }

    override fun isEmpty(): Boolean {
        return x == 0f && y == 0f
    }

    override fun transformBounds(rectangle: Rectangle): Rectangle {
        return rectangle
    }

    override fun reverseBounds(rectangle: Rectangle): Rectangle {
        return rectangle
    }

    override fun transform(rectangle: Rectangle, bounds: Rectangle): Rectangle {
        return rectangle.translate(x, y)
    }

    override fun reverse(rectangle: com.vladsch.plugin.util.image.Rectangle, bounds: com.vladsch.plugin.util.image.Rectangle): Rectangle {
        return rectangle.translate(-x, -y)
    }

    override fun transform(point: com.vladsch.plugin.util.image.Point, bounds: com.vladsch.plugin.util.image.Rectangle): Point {
        return point.translate(x, y)
    }

    override fun reverse(point: com.vladsch.plugin.util.image.Point, bounds: com.vladsch.plugin.util.image.Rectangle): Point {
        return point.translate(-x, -y)
    }

    override fun reversed(): TranslationTransform {
        if (x == 0f || y == 0f) return this
        return TranslationTransform(-x, -y)
    }
}
