package com.vladsch.plugin.util.image

import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class CropImageTransform(margins: Rectangle) : CropTransform(margins), ImageTransform {

    constructor(transform: CropTransform) : this(transform.margins)

    override fun transform(image: BufferedImage): BufferedImage {
        if (margins.isNull || transformBounds(Rectangle.of(image)).isAnyNull) return image
        return ImageUtils.cropImage(image, margins.intX0, margins.intX1, margins.intY0, margins.intY1)
    }

    override fun reversed(): CropImageTransform {
        return CropImageTransform(super.reversed())
    }
}
