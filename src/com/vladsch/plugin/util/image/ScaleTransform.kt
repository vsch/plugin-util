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

import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
open class ScaleTransform(val x: Float, val y: Float) : ImageTransform {
    override fun transformImage(rectangle: Rectangle): Rectangle {
        return rectangle.scale(x, y).nullIfInverted()
    }

    override fun reverseImage(rectangle: Rectangle): Rectangle {
        if (x == 0f || y == 0f) return rectangle
        return rectangle.scale(1 / x, 1 / y).nullIfInverted()
    }

    override fun transform(rectangle: Rectangle): Rectangle {
        return rectangle.scale(x, y)
    }

    override fun reverse(rectangle: Rectangle): Rectangle {
        if (x == 0f || y == 0f) return rectangle
        return rectangle.scale(1 / x, 1 / y)
    }

    override fun transform(point: Point): Point {
        return point.scale(x, y)
    }

    override fun reverse(point: Point): Point {
        if (x == 0f || y == 0f) return point
        return point.scale(1 / x, 1 / y)
    }

    override fun reversed(): ImageTransform {
        if (x == 0f || y == 0f) return this
        return ScaleTransform(1 / x, 1 / y)
    }
}
