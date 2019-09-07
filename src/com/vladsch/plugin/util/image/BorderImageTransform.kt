package com.vladsch.plugin.util.image

import java.awt.Color
import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class BorderImageTransform(borderWidth: Int, val cornerRadius: Int, val borderColor: Color) : BorderTransform(borderWidth), ImageTransform {

    constructor(transform: BorderTransform, borderColor: Color, cornerRadius: Int) : this(transform.borderWidth, cornerRadius, borderColor)

    override fun transform(image: BufferedImage): BufferedImage {
        if (borderWidth < 0 || cornerRadius == 0 && (borderWidth == 0 || borderColor.alpha == 0)) return image
        var bufferedImage = image
        if (cornerRadius > 0) bufferedImage = ImageUtils.makeRoundedCorner(bufferedImage, cornerRadius, borderWidth)
        return ImageUtils.addBorder(bufferedImage, borderColor, borderWidth, cornerRadius)
    }

    override fun imageBorders(image: BufferedImage): DrawingShape? {
        return DrawingShape(super.transformBounds(Rectangle.of(image).withRadius(cornerRadius)), borderWidth, borderColor, null)
    }

    override fun reversed(): BorderImageTransform {
        return BorderImageTransform(-borderWidth, cornerRadius, borderColor)
    }
}
