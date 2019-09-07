package com.vladsch.plugin.util.image

import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
open class CropTransform(val margins: Rectangle) : Transform {
    override fun transform(image: BufferedImage): BufferedImage {
        val imageBounds = Rectangle.of(image)
        if (margins.intX0 == 0 && margins.intX1 == 0 && margins.intY0 == 0 && margins.intY1 == 0 || transformBounds(imageBounds).clipBy(imageBounds).isAnyIntNull) return image
        var croppedImage = ImageUtils.cropImage(image, margins.intX0, margins.intX1, margins.intY0, margins.intY1)
        if (margins.radius > 0) {
            // round corners of cropped image
            croppedImage = ImageUtils.toBufferedImage(ImageUtils.makeRoundedCorner(croppedImage, margins.intCornerRadius, 0))
        }
        return croppedImage
    }

    override fun isEmpty(): Boolean {
        return margins.isNull
    }

    override fun transformBounds(rectangle: Rectangle): Rectangle {
        return rectangle.grow(-margins.x0, -margins.x1, -margins.y0, -margins.y1).nullIfInverted().topLeftTo0()
    }

    override fun reverseBounds(rectangle: Rectangle): Rectangle {
        return rectangle.grow(margins.x0, margins.x1, margins.y0, margins.y1).nullIfInverted().topLeftTo0()
    }

    override fun transform(rectangle: Rectangle): Rectangle {
        return rectangle.translate(-margins.x0, -margins.y0)
    }

    override fun reverse(rectangle: Rectangle): Rectangle {
        return rectangle.translate(margins.x0, margins.y0)
    }

    override fun transform(point: Point): Point {
        return point.translate(-margins.x0, -margins.y0)
    }

    override fun reverse(point: Point): Point {
        return point.translate(margins.x0, margins.y0)
    }

    override fun reversed(): CropTransform {
        return CropTransform(margins.scale(-1f))
    }
}
