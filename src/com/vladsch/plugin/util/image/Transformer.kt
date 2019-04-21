package com.vladsch.plugin.util.image

import com.vladsch.plugin.util.forEachReversed
import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class Transformer constructor(val transforms: List<ImageTransform>, val reversed: Boolean = false) : ImageTransform, DrawingTransform {

    override fun transform(image: BufferedImage): BufferedImage {
        var result = image
        forEach { if (it is DrawingTransform) result = it.transform(result) }
        return result
    }

    override fun transformImage(rectangle: Rectangle): Rectangle {
        var result: Rectangle = rectangle
        if (!reversed) forEach { result = it.transformImage(result) }
        else forEachReversed { result = it.reverseImage(result) }
        return result
    }

    override fun reverseImage(rectangle: Rectangle): Rectangle {
        var result: Rectangle = rectangle
        if (reversed) forEach { result = it.transformImage(result) }
        else forEachReversed { result = it.reverseImage(result) }
        return result
    }

    override fun transform(rectangle: Rectangle): Rectangle {
        var result: Rectangle = rectangle
        if (!reversed) forEach { result = it.transform(result) }
        else forEachReversed { result = it.reverse(result) }
        return result
    }

    override fun reverse(rectangle: Rectangle): Rectangle {
        var result: Rectangle = rectangle
        if (reversed) forEach { result = it.transform(result) }
        else forEachReversed { result = it.reverse(result) }
        return result
    }

    override fun transform(point: Point): Point {
        var result: Point = point
        if (!reversed) forEach { result = it.transform(result) }
        else forEachReversed { result = it.reverse(result) }
        return result
    }

    override fun reverse(point: Point): Point {
        var result: Point = point
        if (reversed) forEach { result = it.transform(result) }
        else forEachReversed { result = it.reverse(result) }
        return result
    }

    fun forEach(action: (ImageTransform) -> Unit) {
        transforms.forEach { action.invoke(it) }
    }

    fun forEachReversed(action: (ImageTransform) -> Unit) {
        transforms.forEachReversed { action.invoke(it) }
    }

    override fun reversed(): ImageTransform {
        return Transformer(transforms, !reversed)
    }
}
