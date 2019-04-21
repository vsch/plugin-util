package com.vladsch.plugin.util.image

@Suppress("MemberVisibilityCanBePrivate")
open class BorderTransform(val borderWidth: Int) : ImageTransform {

    constructor(borderWidth: Float) : this(borderWidth.toInt())

    override fun transformImage(rectangle: Rectangle): Rectangle {
        return rectangle
    }

    override fun reverseImage(rectangle: Rectangle): Rectangle {
        return rectangle
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

    override fun reversed(): ImageTransform {
        return BorderTransform(-borderWidth);
    }
}
