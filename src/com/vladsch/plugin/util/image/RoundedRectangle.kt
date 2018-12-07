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

import com.vladsch.plugin.util.minLimit
import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class RoundedRectangle private constructor(x0: Float, x1: Float, y0: Float, y1: Float, val radius: Float) : Rectangle(x0, x1, y0, y1) {

    override fun shrink(border: Int): RoundedRectangle {
        return of(grow(-border, -border, -border, -border), radius - border)
    }

    override fun grow(border: Int): RoundedRectangle {
        return of(grow(border, border, border, border), radius + border)
    }

    override fun shrink(border: Float): RoundedRectangle {
        return of(grow(-border, -border, -border, -border), radius - border)
    }

    override fun grow(border: Float): RoundedRectangle {
        return of(grow(border, border, border, border), radius + border)
    }

    val cornerRadius: Float
        get() = radius.minLimit(0f)

    val intCornerRadius: Int
        get() = radius.toInt().minLimit(0)

    companion object {
        @JvmField
        val NULL = RoundedRectangle(0f, 0f, 0f, 0f, 0f)

        @JvmStatic
        fun of(rectangle: Rectangle, cornerRadius: Float): RoundedRectangle {
            return if (rectangle === NULL) NULL else RoundedRectangle(rectangle.x0, rectangle.x1, rectangle.y0, rectangle.y1, cornerRadius)
        }

        @JvmStatic
        fun of(x0: Float, x1: Float, y0: Float, y1: Float, cornerRadius: Float): RoundedRectangle {
            return if (x0 == 0f && x1 == 0f && y0 == 0f && y1 == 0f) RoundedRectangle.NULL else RoundedRectangle(x0, x1, y0, y1, cornerRadius)
        }

        @JvmStatic
        fun of(image: BufferedImage, cornerRadius: Float): RoundedRectangle {
            return of(Rectangle.of(image), cornerRadius)
        }

        @JvmStatic
        fun fromBottomRight(left: Float, top: Float, right: Float, bottom: Float, cornerRadius: Float): RoundedRectangle {
            return of(fromBottomRight(left, top, right, bottom), cornerRadius)
        }

        @JvmStatic
        fun fromWidthHeight(left: Float, top: Float, width: Float, height: Float, cornerRadius: Float): RoundedRectangle {
            return of(fromWidthHeight(left, top, width, height), cornerRadius)
        }

        @JvmStatic
        fun of(rectangle: Rectangle, cornerRadius: Int): RoundedRectangle {
            return of(rectangle, cornerRadius.toFloat())
        }

        @JvmStatic
        fun of(x0: Int, x1: Int, y0: Int, y1: Int, cornerRadius: Int): RoundedRectangle {
            return of(x0.toFloat(), x1.toFloat(), y0.toFloat(), y1.toFloat(), cornerRadius.toFloat())
        }

        @JvmStatic
        fun of(image: BufferedImage, cornerRadius: Int): RoundedRectangle {
            return of(Rectangle.of(image), cornerRadius.toFloat())
        }

        @JvmStatic
        fun fromBottomRight(left: Int, top: Int, right: Int, bottom: Int, cornerRadius: Int): RoundedRectangle {
            return of(fromBottomRight(left, top, right, bottom), cornerRadius)
        }

        @JvmStatic
        fun fromWidthHeight(left: Int, top: Int, width: Int, height: Int, cornerRadius: Int): RoundedRectangle {
            return of(fromWidthHeight(left, top, width, height), cornerRadius)
        }
    }
}
