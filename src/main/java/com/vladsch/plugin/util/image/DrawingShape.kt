package com.vladsch.plugin.util.image

import java.awt.Color

open class DrawingShape(val rectangle: Rectangle, val borderWidth: Int, val borderColor: Color?, val fillColor: Color?) : TransformableShape {
    companion object {
        @JvmField
        val NULL = DrawingShape(Rectangle.NULL, 0,null,null)
    }

    override fun isEmpty(): Boolean {
        return rectangle.isAnyIntNull || (borderWidth <= 0 || borderColor == null || borderColor.alpha == 0) && (fillColor == null || fillColor.alpha == 0)
    }

    override fun transformedBy(transform: Transform, bounds: Rectangle): DrawingShape {
        return DrawingShape(transform.transform(rectangle, bounds), borderWidth, borderColor, fillColor)
    }

    override fun transformedBoundsBy(transform: Transform): DrawingShape {
        return DrawingShape(transform.transformBounds(rectangle), borderWidth, borderColor, fillColor)
    }

    fun withFillColor(fillColor: Color?): DrawingShape {
        return DrawingShape(rectangle, borderWidth, borderColor, fillColor)
    }

    fun withBorderColor(fillColor: Color?): DrawingShape {
        return DrawingShape(rectangle, borderWidth, borderColor, fillColor)
    }

    fun withBorderWidth(borderWidth: Int): DrawingShape {
        return DrawingShape(rectangle, borderWidth, borderColor, fillColor)
    }

    fun withMinBorderWidth(borderWidth: Int): DrawingShape {
        return if (this.borderWidth > borderWidth) DrawingShape(rectangle, borderWidth, borderColor, fillColor) else this
    }

    fun withMaxBorderWidth(borderWidth: Int): DrawingShape {
        return if (this.borderWidth < borderWidth) DrawingShape(rectangle, borderWidth, borderColor, fillColor) else this
    }

    fun withMinBorderWidth(borderWidth: Int, borderColor: Color?): DrawingShape {
        return if (this.borderWidth > borderWidth && this.borderColor != null && this.borderColor.alpha != 0) DrawingShape(rectangle, borderWidth, borderColor, fillColor) else this
    }

    fun withMaxBorderWidth(borderWidth: Int, borderColor: Color?): DrawingShape {
        return if (this.borderWidth < borderWidth && (this.borderColor == null || this.borderColor.alpha == 0)) DrawingShape(rectangle, borderWidth, borderColor, fillColor) else this
    }

    fun withNotNullBorderColor(borderColor: Color?): DrawingShape {
        return if (borderColor != null && borderColor.alpha != 0) DrawingShape(rectangle, borderWidth, borderColor, fillColor) else this
    }

    fun withNotNullFillColor(fillColor: Color?): DrawingShape {
        return if (fillColor != null && fillColor.alpha != 0) DrawingShape(rectangle, borderWidth, this.borderColor, fillColor) else this
    }
}
