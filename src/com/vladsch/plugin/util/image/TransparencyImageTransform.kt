package com.vladsch.plugin.util.image

import com.vladsch.plugin.util.rangeLimit
import java.awt.Color
import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class TransparencyImageTransform(val transparentColor: Color, val transparentTolerance: Int) : Transform by Transform.NULL, ImageTransform {

    override fun transform(image: BufferedImage): BufferedImage {
        return ImageUtils.toTransparent(image, transparentColor, transparentTolerance.rangeLimit(0, 255))
    }

    override fun reversed(): TransparencyImageTransform {
        return this
    }
}
