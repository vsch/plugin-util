package com.vladsch.plugin.util.image

import java.awt.Color
import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class CheckeredBackgroundTransform(val checkerSize: Int, val checkerColor1: Color, val checkerColor2: Color) : Transform by Transform.NULL {

    override fun transform(image: BufferedImage): BufferedImage {
        return ImageUtils.createCheckeredBackground(image, checkerSize, checkerColor1, checkerColor2);
    }

    override fun isEmpty(): Boolean {
        return false
    }

    override fun reversed(): CheckeredBackgroundTransform {
        return this
    }
}
