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

@Suppress("MemberVisibilityCanBePrivate")
class Point private constructor(val x: Int, val y: Int) {

    fun clipBy(other: Rectangle): Point {
        val clipped = Point.of(
                Utils.rangeLimit(x, other.left, other.right),
                Utils.rangeLimit(y, other.left, other.right));

        return if (this == clipped) this else clipped
    }

    fun isInsideOf(other: Rectangle): Boolean {
        return clipBy(other) === this
    }

    fun translate(x: Int, y: Int): Point {
        return if (x != 0 || y != 0) Point.of(this.x + x, this.y + y) else this
    }

    fun scale(scale: Float): Point {
        return scale(scale, scale)
    }

    fun scale(x: Float, y: Float): Point {
        if (x != 1f || y != 1f) {
            val scaled = Point.of((this.x * x).toInt(), (this.y * y).toInt())
            if (scaled != this) return scaled
        }
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    override fun toString(): String {
        return "Point(x=$x, y=$y)"
    }

    companion object {
        val NULL = Point(0, 0)

        fun of(x: Int, y: Int): Point {
            return if (x == 0 && y == 0) NULL else Point(x, y)
        }
    }
}
