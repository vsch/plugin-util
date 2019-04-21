package com.vladsch.plugin.util.image

import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class ScalingTransform(x: Float, y: Float, val interpolationType: Int) : ScaleTransform(x, y), DrawingTransform {
    override fun transform(image: BufferedImage): BufferedImage {
        if (x == 1f && y == 1f || x == 0f || y == 0f) return image
        return ImageUtils.scaleImage(image, (image.width * x).toInt(), (image.height * y).toInt(), interpolationType)
    }
}
