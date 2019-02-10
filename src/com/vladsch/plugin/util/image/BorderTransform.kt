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

@Suppress("MemberVisibilityCanBePrivate")
open class BorderTransform(val borderWidth: Int) : ImageTransform {

    constructor(borderWidth: Float) : this(borderWidth.toInt())

    override fun transformImage(rectangle: Rectangle): Rectangle {
        return rectangle
    }

    override fun reverseImage(rectangle: Rectangle): Rectangle {
        return rectangle
    }

    override fun transform(rectangle: Rectangle): Rectangle {
        return rectangle.translate(borderWidth, borderWidth)
    }

    override fun reverse(rectangle: Rectangle): Rectangle {
        return rectangle.translate(-borderWidth, -borderWidth)
    }

    override fun transform(point: Point): Point {
        return point.translate(borderWidth, borderWidth)
    }

    override fun reverse(point: Point): Point {
        return point.translate(-borderWidth, -borderWidth)
    }

    override fun reversed(): ImageTransform {
        return BorderTransform(-borderWidth);
    }
}