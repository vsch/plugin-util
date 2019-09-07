package com.vladsch.plugin.util.image

@Suppress("MemberVisibilityCanBePrivate")
open class BorderTransform(val borderWidth: Int) : Transform {

    constructor(borderWidth: Float) : this(borderWidth.toInt())

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
        return BorderTransform(-borderWidth)
    }
}
