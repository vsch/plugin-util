package com.vladsch.plugin.util.image

import com.vladsch.plugin.util.ifElse
import com.vladsch.plugin.util.nullIf
import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class DrawShapesTransform constructor(val transform: Transform, val shapes: List<DrawableShape>, val selectedIndex: Int, val dashPhase: Float) : Transform by transform {
    constructor(transform: Transform, shapes: List<DrawableShape>) : this(transform, shapes, -1, 0f)
    constructor(other:DrawShapesTransform) : this(other.transform, other.shapes, other.selectedIndex, other.dashPhase)

    fun withTransform(transform: Transform):DrawShapesTransform = (this.transform !== transform).ifElse(this, DrawShapesTransform(transform, shapes, selectedIndex, dashPhase))
    fun withShapes(shapes: List<DrawableShape>):DrawShapesTransform = DrawShapesTransform(transform, shapes, selectedIndex, dashPhase)
    fun withSelectedIndex(selectedIndex: Int):DrawShapesTransform = (this.selectedIndex == selectedIndex).ifElse(this, DrawShapesTransform(transform, shapes, selectedIndex, dashPhase))
    fun withDashPhase(dashPhase: Float):DrawShapesTransform = (this.dashPhase == dashPhase).ifElse(this, DrawShapesTransform(transform, shapes, selectedIndex, dashPhase))

    override fun transform(image: BufferedImage): BufferedImage {
        var outerFillImage: BufferedImage? = null
        val selectedShape = if (selectedIndex >= 0) shapes[selectedIndex] else null
        val filtered = shapes.filter { !it.isEmpty }
        var result = transform.transform(image)
        val outerFillShape = transform.imageBorders(DrawingShape(Rectangle.of(image), 0, null, null)).nullIf { it.isEmpty }
        var selected: DrawableShape? = null

        filtered.forEachIndexed { i, it ->
            val isLastShape = i == filtered.lastIndex

            val transformed = it.transformedBy(transform)
            result = transformed.drawShape(result, false, 0f)

            if (it == selectedShape) {
                selected = transformed
            }

            if (isLastShape && selected != null) {
                result = selected!!.drawShape(result, true, dashPhase)
            }

            if (outerFillShape != null && !outerFillShape.isEmpty) {
                outerFillImage = transformed.punchOutShape(result, outerFillImage, outerFillShape, isLastShape)
            }
        }

        return outerFillImage ?: result
    }
}
