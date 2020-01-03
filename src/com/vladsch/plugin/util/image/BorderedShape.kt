package com.vladsch.plugin.util.image

import com.vladsch.flexmark.util.misc.Utils
import java.awt.Color
import java.awt.image.BufferedImage

class BorderedShape(shapeType: ShapeType, rectangle: Rectangle, borderWidth: Int, borderColor: Color?, fillColor: Color?) : SimpleShape(shapeType, rectangle, borderWidth, borderColor, fillColor) {
    constructor(other: SimpleShape) : this(other.shapeType, other.rectangle, other.borderWidth, other.borderColor, other.fillColor)

    override fun transformedBy(transform: Transform): BorderedShape {
        return BorderedShape(super.transformedBy(transform))
    }

    override fun transformedBoundsBy(transform: Transform): BorderedShape {
        return BorderedShape(super.transformedBoundsBy(transform))
    }

    override fun drawShape(surface: BufferedImage, isSelected: Boolean, dashPhase: Float): BufferedImage {
        if (isEmpty) return surface

        if (isSelected) {
            val dashes: FloatArray
            val dashes2: FloatArray?
            val phaseScale: Float
            val phaseOffset: Float
            var borderWidthSel = borderWidth
            if (borderColor == null || borderWidthSel == 0) {
                borderWidthSel = 1
                dashes = floatArrayOf(4f, 7f, 2f, 7f)
                dashes2 = null
                phaseScale = 3.5f
                phaseOffset = 0f
            } else {
                borderWidthSel = Utils.minLimit(1, borderWidthSel)
                if (borderWidthSel < 4) {
                    dashes = floatArrayOf(borderWidthSel * 2.toFloat(), borderWidthSel * 22.toFloat())
                    dashes2 = floatArrayOf(borderWidthSel * 8.toFloat(), borderWidthSel * 16.toFloat())
                } else {
                    if (borderWidthSel < 7) {
                        dashes = floatArrayOf(borderWidthSel * 1.toFloat(), borderWidthSel * 11.toFloat())
                        dashes2 = floatArrayOf(borderWidthSel * 3.toFloat(), borderWidthSel * 9.toFloat())
                    } else {
                        dashes = floatArrayOf(borderWidthSel * 0.5f, borderWidthSel * 5.5f)
                        dashes2 = floatArrayOf(borderWidthSel * 1.5f, borderWidthSel * 4.5f)
                    }
                }
                phaseScale = 4f
                phaseOffset = (dashes2[0] - dashes[0]) / 2f
            }

            val rect = if (shapeType.isConstrained) rectangle.constrained() else rectangle
            return if (shapeType == ShapeType.OVAL) {
                val bufferedImageSel = ImageUtils.drawOval(surface, rect.intLeft, rect.intTop, rect.intWidth, rect.intHeight, Color.WHITE, borderWidthSel, dashes2, phaseScale * dashPhase + phaseOffset)
                ImageUtils.drawOval(bufferedImageSel, rect.intLeft, rect.intTop, rect.intWidth, rect.intHeight, Color.BLACK, borderWidthSel, dashes, phaseScale * dashPhase)
            } else {
                val bufferedImageSel = ImageUtils.drawRectangle(surface, rect.intLeft, rect.intTop, rect.intWidth, rect.intHeight, Color.WHITE, borderWidthSel, rect.intCornerRadius, dashes2, phaseScale * dashPhase + phaseOffset)
                ImageUtils.drawRectangle(bufferedImageSel, rect.intLeft, rect.intTop, rect.intWidth, rect.intHeight, Color.BLACK, borderWidthSel, rect.intCornerRadius, dashes, phaseScale * dashPhase)
            }
        } else {
            return super.drawShape(surface, false, 0f)
        }
    }
}
