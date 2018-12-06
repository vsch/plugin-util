/*
 * Copyright (c) 2016-2018 Vladimir Schneider <vladimir.schneider@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.vladsch.plugin.util.image

import com.vladsch.plugin.util.forEachReversed
import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class Transformer constructor(val transforms: List<ImageTransform>, val reversed: Boolean = false) : ImageTransform {
    override fun transform(image: BufferedImage): BufferedImage {
        var result = image
        forEach { result = it.transform(result) }
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

    private fun forEach(action: (ImageTransform) -> Unit) {
        transforms.forEach { action.invoke(it) }
    }

    private fun forEachReversed(action: (ImageTransform) -> Unit) {
        transforms.forEachReversed { action.invoke(it) }
    }

    override fun reversed(): ImageTransform {
        return Transformer(transforms, !reversed)
    }
}
