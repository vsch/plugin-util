package com.vladsch.plugin.util.image

import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class ScaleImageTransform(x: Float, y: Float, val interpolationType: Int) : ScaleTransform(x, y), ImageTransform {

    constructor(scale: Float, interpolationType: Int) : this(scale, scale, interpolationType)

    constructor(transform: ScaleTransform, interpolationType: Int) : this(transform.x, transform.y, interpolationType)

    override fun transform(image: BufferedImage): BufferedImage {
        if (x == 1f && y == 1f || x == 0f || y == 0f) return image
        return ImageUtils.scaleImage(image, (image.width * x).toInt(), (image.height * y).toInt(), interpolationType)
    }

    override fun reversed(): ScaleImageTransform {
        return ScaleImageTransform(super.reversed(), interpolationType)
    }
}
