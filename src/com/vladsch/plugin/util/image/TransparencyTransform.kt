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

import com.vladsch.plugin.util.rangeLimit
import java.awt.Color
import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class TransparencyTransform(val transparentColor: Color, val transparentTolerance: Int) : DrawingTransform {

    override fun transform(image: BufferedImage): BufferedImage {
        return ImageUtils.toTransparent(image, transparentColor, transparentTolerance.rangeLimit(0, 255))
    }

    override fun transformImage(rectangle: Rectangle): Rectangle {
        return rectangle
    }

    override fun reverseImage(rectangle: Rectangle): Rectangle {
        return rectangle
    }

    override fun transform(rectangle: Rectangle): Rectangle {
        return rectangle
    }

    override fun reverse(rectangle: Rectangle): Rectangle {
        return rectangle
    }

    override fun transform(point: Point): Point {
        return point
    }

    override fun reverse(point: Point): Point {
        return point
    }

    override fun reversed(): ImageTransform {
        return this
    }
}
