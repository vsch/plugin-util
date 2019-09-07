package com.vladsch.plugin.util.image

import com.vladsch.flexmark.util.Utils
import java.awt.Color
import java.awt.image.BufferedImage

open class SimpleSelectableShape(val shapeType: ShapeType, rectangle: Rectangle, borderWidth: Int, borderColor: Color?, fillColor: Color?) : DrawingShape(rectangle, borderWidth, borderColor, fillColor), SelectableDrawableShape {
    override fun isEmpty(): Boolean {
        return rectangle.isAnyIntNull || (borderWidth == 0 || borderColor == null || borderColor.alpha == 0) && (fillColor == null || fillColor.alpha == 0)
    }

    override fun transformedBy(transform: Transform): SimpleSelectableShape {
        return SimpleSelectableShape(shapeType, transform.transform(rectangle), borderWidth, borderColor, fillColor)
    }

    override fun transformedBoundsBy(transform: Transform): SimpleSelectableShape {
        return SimpleSelectableShape(shapeType, transform.transformBounds(rectangle), borderWidth, borderColor, fillColor)
    }

    override fun punchOutShape(surface: BufferedImage, outerFill: BufferedImage?, outerShape: DrawingShape, applyOuterFillToSurface: Boolean): BufferedImage {
        if (isEmpty) return surface

        return if (shapeType == ShapeType.OVAL) {
            ImageUtils.punchOuterHighlightOval(surface, outerFill, rectangle.intLeft, rectangle.intTop, rectangle.intWidth, rectangle.intHeight, borderWidth, outerShape.fillColor, outerShape.borderWidth, outerShape.rectangle.intCornerRadius, applyOuterFillToSurface)
        } else {
            ImageUtils.punchOuterHighlightRectangle(surface, outerFill, rectangle.intLeft, rectangle.intTop, rectangle.intWidth, rectangle.intHeight, borderWidth, rectangle.intCornerRadius, outerShape.fillColor, outerShape.borderWidth, outerShape.rectangle.intCornerRadius, applyOuterFillToSurface)
        }
    }

    override fun drawShape(surface: BufferedImage, isSelected: Boolean, dashPhase: Float): BufferedImage {
        if (isEmpty) return surface

        if (isSelected) {
            val borderWidthSel =
                if (borderWidth == 0) 1
                else Utils.minLimit(1, borderWidth)

            return if (shapeType == ShapeType.OVAL) {
                val bufferedImageSel = ImageUtils.drawHighlightOval(surface, rectangle.intLeft, rectangle.intTop, rectangle.intWidth, rectangle.intHeight, Color.WHITE, borderWidthSel, null)
                ImageUtils.drawOval(bufferedImageSel, rectangle.intLeft, rectangle.intTop, rectangle.intWidth, rectangle.intHeight, null, borderWidthSel, DASHES, 1.5f * dashPhase)
            } else {
                val bufferedImageSel = ImageUtils.drawRectangle(surface, rectangle.intLeft, rectangle.intTop, rectangle.intWidth, rectangle.intHeight, Color.WHITE, borderWidthSel, rectangle.intCornerRadius)
                ImageUtils.drawRectangle(bufferedImageSel, rectangle.intLeft, rectangle.intTop, rectangle.intWidth, rectangle.intHeight, null, borderWidthSel, rectangle.intCornerRadius, DASHES, 1.5f * dashPhase)
            }
        } else {
            return if (shapeType == ShapeType.OVAL) {
                ImageUtils.drawHighlightOval(surface, rectangle.intLeft, rectangle.intTop, rectangle.intWidth, rectangle.intHeight, borderColor, borderWidth, fillColor)
            } else {
                ImageUtils.drawHighlightRectangle(surface, rectangle.intLeft, rectangle.intTop, rectangle.intWidth, rectangle.intHeight, borderColor, borderWidth, rectangle.intCornerRadius, fillColor)
            }
        }
    }

    companion object {
        val DASHES = floatArrayOf(2f, 3f)
    }
}
