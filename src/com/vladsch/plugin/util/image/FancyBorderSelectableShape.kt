package com.vladsch.plugin.util.image

import com.vladsch.flexmark.util.Utils
import java.awt.Color
import java.awt.image.BufferedImage

class FancyBorderSelectableShape(shapeType: ShapeType, rectangle: Rectangle, borderWidth: Int, borderColor: Color?, fillColor: Color?) : SimpleSelectableShape(shapeType, rectangle, borderWidth, borderColor, fillColor) {
    constructor(other: SimpleSelectableShape) : this(other.shapeType, other.rectangle, other.borderWidth, other.borderColor, other.fillColor)

    override fun transformedBy(transform: Transform): FancyBorderSelectableShape {
        return FancyBorderSelectableShape(super.transformedBy(transform))
    }

    override fun transformedBoundsBy(transform: Transform): FancyBorderSelectableShape {
        return FancyBorderSelectableShape(super.transformedBoundsBy(transform))
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

            return if (shapeType == ShapeType.OVAL) {
                val bufferedImageSel = ImageUtils.drawOval(surface, rectangle.intLeft, rectangle.intTop, rectangle.intWidth, rectangle.intHeight, Color.WHITE, borderWidthSel, dashes2, phaseScale * dashPhase + phaseOffset)
                ImageUtils.drawOval(bufferedImageSel, rectangle.intLeft, rectangle.intTop, rectangle.intWidth, rectangle.intHeight, Color.BLACK, borderWidthSel, dashes, phaseScale * dashPhase)
            } else {
                val bufferedImageSel = ImageUtils.drawRectangle(surface, rectangle.intLeft, rectangle.intTop, rectangle.intWidth, rectangle.intHeight, Color.WHITE, borderWidthSel, rectangle.intCornerRadius, dashes2, phaseScale * dashPhase + phaseOffset)
                ImageUtils.drawRectangle(bufferedImageSel, rectangle.intLeft, rectangle.intTop, rectangle.intWidth, rectangle.intHeight, Color.BLACK, borderWidthSel, rectangle.intCornerRadius, dashes, phaseScale * dashPhase)
            }
        } else {
            return super.drawShape(surface, false, 0f)
        }
    }
}
