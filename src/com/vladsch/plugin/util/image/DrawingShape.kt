/*
 *
 */

package com.vladsch.plugin.util.image

import java.awt.Color

open class DrawingShape(val rectangle: Rectangle, val borderWidth: Int, val borderColor: Color?, val fillColor: Color?) : TransformableShape {
    override fun isEmpty(): Boolean {
        return rectangle.isAnyIntNull || (borderWidth == 0 || borderColor == null || borderColor.alpha == 0) && (fillColor == null || fillColor.alpha == 0)
    }

    override fun transformedBy(transform: Transform): TransformableShape {
        return DrawingShape(transform.transform(rectangle), borderWidth, borderColor, fillColor)
    }

    fun withFillColor(fillColor: Color?): DrawingShape {
        return DrawingShape(rectangle, borderWidth, borderColor, fillColor)
    }
}
