package com.vladsch.plugin.util.image

import java.awt.Color
import java.awt.image.BufferedImage

class RubberBandShape(shapeType: ShapeType, rectangle: Rectangle, borderWidth: Int, borderColor: Color?, fillColor: Color?) : SimpleShape(shapeType, rectangle, borderWidth, borderColor, fillColor) {
    constructor(other: SimpleShape) : this(other.shapeType, other.rectangle, other.borderWidth, other.borderColor, other.fillColor)

    override fun transformedBy(transform: Transform, bounds: Rectangle): RubberBandShape {
        return RubberBandShape(super.transformedBy(transform, bounds))
    }

    override fun transformedBoundsBy(transform: Transform): RubberBandShape {
        return RubberBandShape(super.transformedBoundsBy(transform))
    }

    override fun punchOutShape(surface: BufferedImage, outerFill: BufferedImage?, outerShape: DrawingShape, applyOuterFillToSurface: Boolean): BufferedImage {
        return surface
    }

    override fun drawShape(surface: BufferedImage, isSelected: Boolean, dashPhase: Float): BufferedImage {
        if (isEmpty || !isSelected) return surface
        return super.drawShape(surface, isSelected, dashPhase)
    }
}
