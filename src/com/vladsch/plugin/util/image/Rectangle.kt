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

import com.vladsch.flexmark.util.Utils
import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class Rectangle private constructor(val x0: Int, val x1: Int, val y0: Int, val y1: Int) {

    val isInvertedY: Boolean
        get() = y0 > y1

    val isInvertedX: Boolean
        get() = x0 > x1

    val isNull: Boolean
        get() = x0 == x1 && y0 == y1

    val isAnyNull: Boolean
        get() = x0 == x1 || y0 == y1

    val isNullX: Boolean
        get() = x0 == x1

    val isNullY: Boolean
        get() = y0 == y1

    val left: Int
        get() = if (isInvertedX) x1 else x0

    val top: Int
        get() = if (isInvertedY) y1 else y0

    val right: Int
        get() = if (isInvertedX) x0 else x1

    val bottom: Int
        get() = if (isInvertedY) y0 else y1

    val spanX: Int
        get() = x1 - x0

    val spanY: Int
        get() = y1 - y0

    val width: Int
        get() = Math.abs(x1 - x0)

    val height: Int
        get() = Math.abs(y1 - y0)

    val normalized: Rectangle
        get() {
            val invX = isInvertedX
            val invY = isInvertedY
            return invert(invX, invY)
        }

    val square: Int
        get() = Math.min(width, height)

    fun invert(invX: Boolean, invY: Boolean): Rectangle {
        return when {
            invX && invY -> Rectangle(x1, x0, y1, y0)
            invX -> Rectangle(x1, x0, y0, y1)
            invY -> Rectangle(x0, x1, y1, y0)
            else -> this
        }
    }

    fun clipBy(other: Rectangle): Rectangle {
        val invX = isInvertedX
        val invY = isInvertedY

        val clipped = of(
                Utils.rangeLimit(left, other.left, other.right),
                Utils.rangeLimit(right, other.left, other.right),
                Utils.rangeLimit(top, other.top, other.bottom),
                Utils.rangeLimit(bottom, other.top, other.bottom))
                .invert(invX, invY)

        return when {
            this == clipped -> this
            other == clipped -> other
            else -> clipped
        }
    }

    fun isInsideOf(other: Rectangle): Boolean {
        return clipBy(other) === this
    }

    fun translate(x: Int, y: Int): Rectangle {
        return if (x != 0 || y != 0) {
            of(x0 + x, x1 + x, y0 + y, y1 + y)
        } else this
    }

    fun topLeftTo0(): Rectangle {
        return normalized.translate(-left, -top)
    }

    fun translateCenterTo0(): Rectangle {
        return normalized.translate(-(spanX) / 2, -(spanY) / 2)
    }
    
    fun nullIfInverted():Rectangle {
        return if (isInvertedX || isInvertedY) NULL else this
    }

    fun grow(border: Int): Rectangle {
        return grow(border, border)
    }

    fun grow(x: Int, y: Int): Rectangle {
        return grow(x, x, y, y)
    }

    fun grow(left: Int, right: Int, top: Int, bottom: Int): Rectangle {
        if (left != 0 || top != 0 || right != 0 || bottom != 0) {
            val invX = isInvertedX
            val invY = isInvertedY

            return of(this.left - left, this.right + right, this.top - top, this.bottom + bottom).invert(invX, invY)
        }

        return this
    }

    fun scale(scale: Float): Rectangle {
        return scale(scale, scale)
    }

    fun scale(x: Float, y: Float): Rectangle {
        if (x != 0f && y != 0f && (x != 1f || y != 1f)) {
            val scaled = of((x0 * x).toInt(), (x1 * x).toInt(), (y0 * y).toInt(), (y1 * y).toInt())
            if (scaled != this) {
                return scaled
            }
        }
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val rectangle = other as Rectangle?

        if (x0 != rectangle!!.x0) return false
        if (x1 != rectangle.x1) return false
        return if (y0 != rectangle.y0) false else y1 == rectangle.y1
    }

    override fun hashCode(): Int {
        var result = x0
        result = 31 * result + x1
        result = 31 * result + y0
        result = 31 * result + y1
        return result
    }

    override fun toString(): String {
        return "Rectangle(x0=$x0, x1=$x1, y0=$y0, y1=$y1)"
    }

    companion object {
        val NULL = Rectangle(0, 0, 0, 0)

        fun of(x0: Int, x1: Int, y0: Int, y1: Int): Rectangle {
            return if (x0 == 0 && x1 == 0 && y0 == 0 && y1 == 0) NULL else Rectangle(x0, x1, y0, y1)
        }

        fun of(image:BufferedImage): Rectangle {
            val x1 = image.width
            val y1 = image.height
            return if (x1 == 0 && y1 == 0) NULL else Rectangle(0, x1, 0, y1)
        }

        fun fromBottomRight(x0: Int, y0: Int, x1: Int, y1: Int): Rectangle {
            return if (x0 == 0 && x1 == 0 && y0 == 0 && y1 == 0) NULL else Rectangle(x0, x1, y0, y1)
        }

        fun fromWidthHeight(x0: Int, y0: Int, width: Int, height: Int): Rectangle {
            return if (x0 == 0 && width == 0 && y0 == 0 && height == 0) NULL else Rectangle(x0, x0 + width, y0, y0 + height)
        }
    }
}
