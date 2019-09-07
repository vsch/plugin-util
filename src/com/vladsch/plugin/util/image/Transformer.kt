package com.vladsch.plugin.util.image

import com.vladsch.plugin.util.forEachReversed

@Suppress("MemberVisibilityCanBePrivate")
open class Transformer constructor(val transforms: List<Transform>, val reversed: Boolean = false) : Transform {

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

    fun forEach(action: (Transform) -> Unit) {
        transforms.forEach { action.invoke(it) }
    }

    fun forEachReversed(action: (Transform) -> Unit) {
        transforms.forEachReversed { action.invoke(it) }
    }

    override fun reversed(): Transform {
        return Transformer(transforms, !reversed)
    }
}
