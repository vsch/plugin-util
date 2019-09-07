package com.vladsch.plugin.util.image

import java.awt.Color
import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class BorderImageTransform(borderWidth: Int, val borderColor: Color, val cornerRadius: Int) : BorderTransform(borderWidth), ImageTransform {
    override fun transform(image: BufferedImage): BufferedImage {
        if (borderWidth < 0 || cornerRadius == 0 && (borderWidth == 0 || borderColor.alpha == 0)) return image
        var bufferedImage = image
        if (cornerRadius > 0) bufferedImage = ImageUtils.makeRoundedCorner(bufferedImage, cornerRadius, borderWidth)
        return ImageUtils.addBorder(bufferedImage, borderColor, borderWidth, cornerRadius)
    }

    override fun reversed(): Transform {
        return BorderImageTransform(-borderWidth, borderColor, cornerRadius);
    }
}
