package com.vladsch.plugin.util.image

import com.vladsch.plugin.util.nullIf
import java.awt.Color
import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class DrawShapesTransformer constructor(val transform: Transform, val shapes: List<DrawableShape>) : Transform by transform {

    var selectedIndex: Int = -1
    var outerFillColor: Color? = null
    var dashPhase: Float = 0f

    override fun transform(image: BufferedImage): BufferedImage {
        var outerFillImage: BufferedImage? = null
        val selectedShape = if (selectedIndex >= 0) shapes[selectedIndex] else null
        val filtered = shapes.filter { !it.isEmpty }
        var result = transform.transform(image)
        var selected: DrawableShape? = null
        val outerFillShape =
            if (outerFillColor == null) null
            else transform.imageBorders(DrawingShape(Rectangle.of(image), 0, null, outerFillColor)).nullIf { it.isEmpty }

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
