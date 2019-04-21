package com.vladsch.plugin.util.image

@Suppress("MemberVisibilityCanBePrivate")
open class CropTransform(val margins: Rectangle) : ImageTransform {
    override fun transformImage(rectangle: Rectangle): Rectangle {
        return rectangle.grow(-margins.x0, -margins.x1, -margins.y0, -margins.y1).nullIfInverted().topLeftTo0()
    }

    override fun reverseImage(rectangle: Rectangle): Rectangle {
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

    override fun reversed(): ImageTransform {
        return CropTransform(margins.scale(-1f));
    }
}
