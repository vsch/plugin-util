package com.vladsch.plugin.util.image

import com.vladsch.flexmark.util.Utils
import java.awt.Color
import java.awt.image.BufferedImage

open class SimpleShape(val shapeType: ShapeType, rectangle: Rectangle, borderWidth: Int, borderColor: Color?, fillColor: Color?) : DrawingShape(rectangle, borderWidth, borderColor, fillColor), DrawableShape {
    override fun isEmpty(): Boolean {
        return rectangle.isAnyIntNull || (borderWidth == 0 || borderColor == null || borderColor.alpha == 0) && (fillColor == null || fillColor.alpha == 0)
    }

    override fun transformedBy(transform: Transform): SimpleShape {
        return SimpleShape(shapeType, transform.transform(rectangle), borderWidth, borderColor, fillColor)
    }

    override fun transformedBoundsBy(transform: Transform): SimpleShape {
        return SimpleShape(shapeType, transform.transformBounds(rectangle), borderWidth, borderColor, fillColor)
    }

    override fun punchOutShape(surface: BufferedImage, outerFill: BufferedImage?, outerShape: DrawingShape, applyOuterFillToSurface: Boolean): BufferedImage {
        if (isEmpty || outerShape.fillColor == null || outerShape.fillColor.alpha == 0) return surface

        val rect = if (shapeType.isConstrained) rectangle.constrained() else rectangle
        return if (shapeType == ShapeType.OVAL) {
            ImageUtils.punchOuterHighlightOval(surface, outerFill, rect.intLeft, rect.intTop, rect.intWidth, rect.intHeight, borderWidth, outerShape.fillColor, outerShape.borderWidth, outerShape.rectangle.intCornerRadius, applyOuterFillToSurface)
        } else {
            ImageUtils.punchOuterHighlightRectangle(surface, outerFill, rect.intLeft, rect.intTop, rect.intWidth, rect.intHeight, borderWidth, rect.intCornerRadius, outerShape.fillColor, outerShape.borderWidth, outerShape.rectangle.intCornerRadius, applyOuterFillToSurface)
        }
    }

    override fun drawShape(surface: BufferedImage, isSelected: Boolean, dashPhase: Float): BufferedImage {
        if (isEmpty) return surface

        val rect = if (shapeType.isConstrained) rectangle.constrained() else rectangle
        if (isSelected) {
            val borderWidthSel = Utils.minLimit(1, borderWidth)

            return if (shapeType == ShapeType.OVAL) {
                val bufferedImageSel = ImageUtils.drawHighlightOval(surface, rect.intLeft, rect.intTop, rect.intWidth, rect.intHeight, Color.WHITE, borderWidthSel, null)
                ImageUtils.drawOval(bufferedImageSel, rect.intLeft, rect.intTop, rect.intWidth, rect.intHeight, Color.BLACK, borderWidthSel, DASHES, 1.5f * dashPhase)
            } else {
                val bufferedImageSel = ImageUtils.drawRectangle(surface, rect.intLeft, rect.intTop, rect.intWidth, rect.intHeight, Color.WHITE, borderWidthSel, rect.intCornerRadius)
                ImageUtils.drawRectangle(bufferedImageSel, rect.intLeft, rect.intTop, rect.intWidth, rect.intHeight, Color.BLACK, borderWidthSel, rect.intCornerRadius, DASHES, 1.5f * dashPhase)
            }
        } else {
            return if (shapeType == ShapeType.OVAL) {
                ImageUtils.drawHighlightOval(surface, rect.intLeft, rect.intTop, rect.intWidth, rect.intHeight, borderColor, borderWidth, fillColor)
            } else {
                ImageUtils.drawHighlightRectangle(surface, rect.intLeft, rect.intTop, rect.intWidth, rect.intHeight, borderColor, borderWidth, rect.intCornerRadius, fillColor)
            }
        }
    }

    // TEST: add tests
    override fun isInside(point: Point?): Boolean {
        if (point == null) return false

        val rect: Rectangle = rectangle
            .grow(if (borderWidth > 0 && borderColor != null && borderColor.alpha > 0) borderWidth / 2f else 0f)
            .constrained(shapeType.isConstrained)

        if (!rect.isAnyIntNull) {
            if (shapeType.isOval) {
                val cw = rect.intWidth / 2f
                val ch = rect.intHeight / 2f
                val cx: Float = point.intX - (rect.intLeft + cw)
                val cy: Float = point.intY - (rect.intTop + ch)
                val f = cx / cw * (cx / cw) + cy / ch * (cy / ch)
                return f < 1.0
            } else {
                return point.isInside(rect)
            }
        }
        return false
    }

    companion object {
        val DASHES = floatArrayOf(2f, 3f)
    }
}
