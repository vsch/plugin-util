/*
 *
 */

package com.vladsch.plugin.util.image

import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
open class TranslateTransform(val x: Float, val y: Float) : Transform {

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

    override fun transform(rectangle: Rectangle): Rectangle {
        return rectangle.translate(x, y)
    }

    override fun reverse(rectangle: Rectangle): Rectangle {
        return rectangle.translate(-x, -y)
    }

    override fun transform(point: Point): Point {
        return point.translate(x, y)
    }

    override fun reverse(point: Point): Point {
        return point.translate(-x, -y)
    }

    override fun reversed(): TranslateTransform {
        if (x == 0f || y == 0f) return this
        return TranslateTransform(-x, -y)
    }
}
