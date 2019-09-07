package com.vladsch.plugin.util.image

import java.awt.Color
import java.awt.image.BufferedImage

class RubberBandSelectedShape(shapeType: ShapeType, rectangle: Rectangle, borderWidth: Int, borderColor: Color?, fillColor: Color?) : SimpleSelectableShape(shapeType, rectangle, borderWidth, borderColor, fillColor) {
    constructor(other: SimpleSelectableShape) : this(other.shapeType, other.rectangle, other.borderWidth, other.borderColor, other.fillColor)

    override fun transformedBy(transform: Transform): RubberBandSelectedShape {
        return RubberBandSelectedShape(super.transformedBy(transform))
    }

    override fun transformedBoundsBy(transform: Transform): RubberBandSelectedShape {
        return RubberBandSelectedShape(super.transformedBoundsBy(transform))
    }

    override fun drawShape(surface: BufferedImage): BufferedImage {
        return surface
    }

    override fun punchOutShape(surface: BufferedImage, outerFill: BufferedImage?, outerShape: DrawingShape, applyOuterFillToSurface: Boolean): BufferedImage {
        return surface
    }

    override fun drawShape(surface: BufferedImage, isSelected: Boolean, dashPhase: Float): BufferedImage {
        if (isEmpty || !isSelected) return surface
            return super.drawShape(surface,isSelected,dashPhase)
    }
}
