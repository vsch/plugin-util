package com.vladsch.plugin.util.image

import java.awt.image.BufferedImage
import kotlin.math.roundToInt

@Suppress("MemberVisibilityCanBePrivate")
open class RotateTransform(val rotateBy: Int) : Transform {
    constructor(rotateByRad: Float) : this(Math.toDegrees(rotateByRad.toDouble()).roundToInt())
    
    override fun transform(image: BufferedImage): BufferedImage {
        return ImageUtils.rotateImage(image, rotateBy)
    }

    override fun isEmpty(): Boolean {
        return (rotateBy % 360 + 360) % 360 == 0
    }

    override fun transformBounds(rectangle: Rectangle): Rectangle {
        return rectangle.rotate(rotateBy, rectangle.center()).normalized.topLeftTo0()
    }

    override fun reverseBounds(rectangle: Rectangle): Rectangle {
        return rectangle.rotate(-rotateBy, rectangle.center()).normalized.topLeftTo0()
    }

    override fun transform(rectangle: Rectangle, bounds: Rectangle): Rectangle {
        // have to apply all transformations that were done the bounds, including translation to top/left
        return rectangle.rotateBounded(rotateBy, bounds)
    }

    override fun reverse(rectangle: Rectangle, bounds: Rectangle): Rectangle {
        // have to apply all transformations that were done the bounds, including translation to top/left
        return rectangle.rotateBounded(-rotateBy, bounds)
    }

    override fun transform(point: Point, bounds: Rectangle): Point {
        // have to apply all transformations that were done the bounds, including translation to top/left
        return point.rotateBounded(rotateBy, bounds)
    }

    override fun reverse(point: Point, bounds: Rectangle): Point {
        // have to apply all transformations that were done the bounds, including translation to top/left
        return point.rotateBounded(-rotateBy, bounds)
    }

    override fun reversed(): RotateTransform {
        return RotateTransform(-rotateBy)
    }
}
