package com.vladsch.plugin.util.image

import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class CroppingTransform(margins: Rectangle) : CropTransform(margins), DrawingTransform {
    override fun transform(image: BufferedImage): BufferedImage {
        if (margins.isNull || transformImage(Rectangle.of(image)).isAnyNull) return image
        return ImageUtils.cropImage(image, margins.intX0, margins.intX1, margins.intY0, margins.intY1)
    }
    override fun reversed(): ImageTransform {
        return CroppingTransform(margins.scale(-1f));
    }
}
