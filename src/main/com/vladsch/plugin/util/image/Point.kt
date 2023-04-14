package com.vladsch.plugin.util.image

import com.vladsch.plugin.util.rangeLimit
import kotlin.math.roundToInt

@Suppress("MemberVisibilityCanBePrivate")
class Point private constructor(@JvmField val x: Float, @JvmField val y: Float) {

    val intX: Int
        get() = x.roundToInt()

    val intY: Int
        get() = y.roundToInt()

    fun clipBy(other: Rectangle): Point {
        val clipped = Point.of(
            x.rangeLimit(other.left, other.right),
            y.rangeLimit(other.top, other.bottom))

        return if (this == clipped) this else clipped
    }

    fun clipOut(other: Rectangle): Point {
        val clipped = Point.of(
            x.rangeLimit(other.left, other.right),
            y.rangeLimit(other.top, other.bottom))

        return if (this == clipped) this else NULL
    }

    val isNull: Boolean
        get() = this === NULL

    fun isInsideOf(other: Rectangle): Boolean {
        return clipBy(other) === this
    }

    fun translate(x: Int, y: Int): Point {
        return translate(x.toFloat(), y.toFloat())
    }

    fun translate(x: Float, y: Float): Point {
        return if (x != 0f || y != 0f) Point.of(this.x + x, this.y + y) else this
    }

    val round: Point
        get() = copy(Point.of(x.roundToInt(), y.roundToInt()))

    fun roundTo(value: Float): Point {
        return copyOf(this, x.roundTo(value), x.roundTo(value))
    }

    fun scale(scale: Float): Point {
        return scale(scale, scale)
    }

    fun scale(x: Float, y: Float): Point {
        if (x != 1f || y != 1f) {
            return copy(Point.of((this.x * x), (this.y * y)))
        }
        return this
    }

    fun copy(other: Point): Point {
        return if (this == other) this else other
    }

    fun isInside(rect: Rectangle): Boolean {
        return x >= rect.left && x < rect.right && y >= rect.top && y < rect.bottom
    }

    fun rotate(deg: Int, c: Point): Point {
        return rotate(Math.toRadians(deg.toDouble()).toFloat(), c.x, c.y)
    }

    fun rotate(deg: Int, x: Int, y: Int): Point {
        return rotate(Math.toRadians(deg.toDouble()).toFloat(), x.toFloat(), y.toFloat())
    }

    fun rotate(rad: Float, c: Point): Point {
        return rotate(rad, c.x, c.y)
    }

    fun rotate(rad: Float, x: Float, y: Float): Point {
        val rot = Complex.rot(rad.toDouble())
        val p = translate(-x, -y)
        val rotated = Complex.of(p).times(rot).toPoint()
        return rotated
    }

    fun rotateBounded(deg: Int, bounds: Rectangle): Point {
        return rotateBounded(Math.toRadians(deg.toDouble()).toFloat(), bounds)
    }

    fun rotateBounded(rad: Float, bounds: Rectangle): Point {
        // have to apply all transformations that were done to the bounds rectangle, including translation to top/left
        val boundsRotated = bounds.rotate(rad, bounds.center())
        val boundsNormalized = boundsRotated.normalized
        return rotate(rad, bounds.center())
            .invert(boundsRotated.isInvertedX, boundsRotated.isInvertedY, boundsRotated.center())
            .translate(-boundsNormalized.left, -boundsNormalized.top)
    }

    // flip x about cx and y about cy if respective invX invY are given
    fun invert(invX: Boolean, invY: Boolean, c: Point): Point {
        return invert(invX, invY, c.x, c.y)
    }

    fun invert(invX: Boolean, invY: Boolean, cx: Int, cy: Int): Point {
        return invert(invX, invY, cx.toFloat(), cy.toFloat())
    }

    fun invert(invX: Boolean, invY: Boolean, cx: Float, cy: Float): Point {
        return of(if (!invX) x else -(x - cx) + cx, if (!invY) y else -(y - cy) + cy)
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
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    override fun toString(): String {
        return "Point(x=$x, y=$y)"
    }

    companion object {

        @JvmField
        val NULL = Point(0f, 0f)

        private fun copyOf(other: Point, x: Float, y: Float): Point {
            return if (other.x == x && other.y == y) other
            else of(x, y)
        }

        @JvmStatic
        fun of(x: Float, y: Float): Point {
            return if (x == 0f && y == 0f) NULL else Point(x, y)
        }

        @JvmStatic
        fun of(x: Int, y: Int): Point {
            return Point.of(x.toFloat(), y.toFloat())
        }
    }
}

