package com.vladsch.plugin.util.image

@Suppress("MemberVisibilityCanBePrivate")
open class ScaleTransform(val x: Float, val y: Float) : Transform {
    override fun transformImage(rectangle: Rectangle): Rectangle {
        return rectangle.scale(x, y).nullIfInverted()
    }

    override fun reverseImage(rectangle: Rectangle): Rectangle {
        if (x == 0f || y == 0f) return rectangle
        return rectangle.scale(1 / x, 1 / y).nullIfInverted()
    }

    override fun transform(rectangle: Rectangle): Rectangle {
        return rectangle.scale(x, y)
    }

    override fun reverse(rectangle: Rectangle): Rectangle {
        if (x == 0f || y == 0f) return rectangle
        return rectangle.scale(1 / x, 1 / y)
    }

    override fun transform(point: Point): Point {
        return point.scale(x, y)
    }

    override fun reverse(point: Point): Point {
        if (x == 0f || y == 0f) return point
        return point.scale(1 / x, 1 / y)
    }

    override fun reversed(): Transform {
        if (x == 0f || y == 0f) return this
        return ScaleTransform(1 / x, 1 / y)
    }
}
