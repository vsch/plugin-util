package com.vladsch.plugin.util.image

import com.vladsch.plugin.util.rangeLimit
import java.awt.Color
import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class TransparencyTransform(val transparentColor: Color, val transparentTolerance: Int) : ImageTransform {

    override fun transform(image: BufferedImage): BufferedImage {
        return ImageUtils.toTransparent(image, transparentColor, transparentTolerance.rangeLimit(0, 255))
    }

    override fun transformImage(rectangle: Rectangle): Rectangle {
        return rectangle
    }

    override fun reverseImage(rectangle: Rectangle): Rectangle {
        return rectangle
    }

    override fun transform(rectangle: Rectangle): Rectangle {
        return rectangle
    }

    override fun reverse(rectangle: Rectangle): Rectangle {
        return rectangle
    }

    override fun transform(point: Point): Point {
        return point
    }

    override fun reverse(point: Point): Point {
        return point
    }

    override fun reversed(): Transform {
        return this
    }
}
