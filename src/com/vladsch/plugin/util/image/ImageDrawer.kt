package com.vladsch.plugin.util.image

import com.vladsch.plugin.util.nullIf
import java.awt.Color
import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class ImageDrawer constructor(val transformer: ImageTransform, val shapes: List<SelectableDrawableShape>) : ImageTransform, Transform by transformer {

    override fun transform(image: BufferedImage): BufferedImage {
        var result = transformer.transform(image)
        shapes.filter { !it.isEmpty }.forEach {
            result = it.transformedBy(transformer).drawShape(result)
        }
        return result
    }

    fun drawShapes(surface: BufferedImage, selectedIndex: Int, dashPhase: Float): BufferedImage? {
        return if (selectedIndex < 0 || shapes[selectedIndex].isEmpty) transform(surface) else drawShapes(surface, null, selectedIndex, dashPhase)
    }

    fun drawShapes(image: BufferedImage, outerFillColor: Color?, selectedIndex: Int, dashPhase: Float): BufferedImage {
        var outerFill: BufferedImage? = null
        val selectedShape = if (selectedIndex >= 0) shapes[selectedIndex] else null
        val filtered = shapes.filter { !it.isEmpty }
        var result = transformer.transform(image)
        var selectedFiltered: SelectableDrawableShape? = null
        val outerShape =
            if (outerFillColor == null) null
            else transformer.imageBorders(image)?.withFillColor(outerFillColor).nullIf { it.isEmpty }

        filtered.forEachIndexed { i, it ->
            val transformed = it.transformedBy(transformer)
            result = transformed.drawShape(result, false, 0f)

            if (it == selectedShape) {
                selectedFiltered = transformed
            }

            val selected = selectedFiltered
            if (i == filtered.lastIndex && selected != null) {
                result = selected.drawShape(result, true, dashPhase)
            }

            if (outerShape != null && !outerShape.isEmpty) {
                outerFill = transformed.punchOutShape(result, outerFill, outerShape, i == filtered.lastIndex)
            }
        }

        return outerFill ?: result
    }
}
