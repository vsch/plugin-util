package com.vladsch.plugin.util.image

import com.vladsch.plugin.util.forEachReversed
import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
open class TransformList constructor(val transforms: List<Transform>, val reversed: Boolean = false) : Transform {

    override fun andThen(transform: Transform): Transform {
        val transforms = ArrayList<Transform>(transforms)
        transforms.add(if (reversed) transform.reversed() else transform)
        return TransformList(transforms, reversed);
    }

    override fun isEmpty(): Boolean {
        return transforms.isEmpty() || transforms.all { it.isEmpty }
    }

    override fun transform(image: BufferedImage): BufferedImage {
        var result = image
        forEach { result = it.transform(result) }
        return result
    }

    override fun imageBorders(shape: DrawingShape): DrawingShape {
        var result: DrawingShape = shape
        forEach { result = it.imageBorders(result) }
        return result
    }

    override fun transformBounds(rectangle: Rectangle): Rectangle {
        var result: Rectangle = rectangle
        if (!reversed) forEach { result = it.transformBounds(result) }
        else forEachReversed { result = it.reverseBounds(result) }
        return result
    }

    override fun reverseBounds(rectangle: Rectangle): Rectangle {
        var result: Rectangle = rectangle
        if (reversed) forEach { result = it.transformBounds(result) }
        else forEachReversed { result = it.reverseBounds(result) }
        return result
    }

    override fun transform(rectangle: Rectangle, bounds: Rectangle): Rectangle {
        var result: Rectangle = rectangle
        var evolvingBounds: Rectangle = bounds
        if (!reversed) forEach {
            result = it.transform(result, evolvingBounds)
            evolvingBounds = it.transformBounds(evolvingBounds)
        }
        else forEachReversed {
            result = it.reverse(result, evolvingBounds)
            evolvingBounds = it.reverseBounds(evolvingBounds)
        }
        return result
    }

    override fun reverse(rectangle: Rectangle, bounds: Rectangle): Rectangle {
        var result: Rectangle = rectangle
        var evolvingBounds: Rectangle = bounds
        if (reversed) forEach {
            result = it.transform(result, evolvingBounds)
            evolvingBounds = it.transformBounds(evolvingBounds)
        }
        else forEachReversed {
            result = it.reverse(result, evolvingBounds)
            evolvingBounds = it.reverseBounds(evolvingBounds)
        }
        return result
    }

    override fun transform(point: Point, bounds: Rectangle): Point {
        var result: Point = point
        var evolvingBounds: Rectangle = bounds
        if (!reversed) forEach {
            result = it.transform(result, evolvingBounds)
            evolvingBounds = it.transformBounds(evolvingBounds)
        }
        else forEachReversed {
            result = it.reverse(result, evolvingBounds)
            evolvingBounds = it.reverseBounds(evolvingBounds)
        }
        return result
    }

    override fun reverse(point: Point, bounds: Rectangle): Point {
        var result: Point = point
        var evolvingBounds: Rectangle = bounds
        if (reversed) forEach {
            result = it.transform(result, evolvingBounds)
            evolvingBounds = it.transformBounds(evolvingBounds)
        }
        else forEachReversed {
            result = it.reverse(result, evolvingBounds)
            evolvingBounds = it.reverseBounds(evolvingBounds)
        }
        return result
    }

    fun forEach(action: (Transform) -> Unit) {
        transforms.forEach { action.invoke(it) }
    }

    fun forEachReversed(action: (Transform) -> Unit) {
        transforms.forEachReversed { action.invoke(it) }
    }

    override fun reversed(): Transform {
        return TransformList(transforms, !reversed)
    }
}
