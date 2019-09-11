package com.vladsch.plugin.util.image

import com.vladsch.plugin.util.rangeLimit
import java.awt.Color
import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class TransparencyTransform(val transparentColor: Color, val transparentTolerance: Int, val checkerSize: Int, val checkerColor1: Color?, val checkerColor2: Color?) : Transform by Transform.NULL {

    constructor(transparentColor: Color, transparentTolerance: Int) : this(transparentColor, transparentTolerance, -1, null, null)

    override fun transform(image: BufferedImage): BufferedImage {
        if (checkerSize > 0 && checkerColor1 != null && checkerColor2 != null) {
            // add checkered background under the image with transparency
            val transparentImage = ImageUtils.toTransparent(image, transparentColor, transparentTolerance.rangeLimit(0, 255))
            return ImageUtils.createCheckeredBackground(transparentImage, checkerSize, checkerColor1, checkerColor2);
        } else {
            return ImageUtils.toTransparent(image, transparentColor, transparentTolerance.rangeLimit(0, 255))
        }
    }

    override fun isEmpty(): Boolean {
        return false
    }

    override fun reversed(): TransparencyTransform {
        return this
    }
}
