package com.vladsch.plugin.util.image

import com.vladsch.plugin.util.rangeLimit
import java.awt.Color
import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class TransparencyTransform(val transparentColor: Color, val transparentTolerance: Int) : Transform by Transform.NULL {

    override fun transform(image: BufferedImage): BufferedImage {
        return ImageUtils.toTransparent(image, transparentColor, transparentTolerance.rangeLimit(0, 255))
    }

    override fun isEmpty(): Boolean {
        return false
    }

    override fun reversed(): TransparencyTransform {
        return this
    }
}
