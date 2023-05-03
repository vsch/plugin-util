package com.vladsch.plugin.util.image

import com.vladsch.plugin.util.minLimit
import com.vladsch.plugin.util.rangeLimit
import java.awt.image.BufferedImage
import java.lang.Math.toRadians
import kotlin.math.*

@Suppress("MemberVisibilityCanBePrivate")
open class Rectangle protected constructor(
    @JvmField val x0: Float,
    @JvmField val x1: Float,
    @JvmField val y0: Float,
    @JvmField val y1: Float,
    @JvmField val radius: Float
) {

    val isInvertedY: Boolean
        get() = y0 > y1

    val isInvertedX: Boolean
        get() = x0 > x1

    val isNull: Boolean
        get() = this === NULL || x0 == x1 && y0 == y1

    val isIntNull: Boolean
        get() = this === NULL || intX0 == intX1 && intY0 == intY1

    val isAnyNull: Boolean
        get() = x0 == x1 || y0 == y1

    val isAnyIntNull: Boolean
        get() = intX0 == intX1 || intY0 == intY1

    val isNullX: Boolean
        get() = x0 == x1

    val isNullY: Boolean
        get() = y0 == y1

    val isNullIntX: Boolean
        get() = intX0 == intX1

    val isNullIntY: Boolean
        get() = intY0 == intY1

    val left: Float
        get() = if (isInvertedX) x1 else x0

    val top: Float
        get() = if (isInvertedY) y1 else y0

    val right: Float
        get() = if (isInvertedX) x0 else x1

    val bottom: Float
        get() = if (isInvertedY) y0 else y1

    val width: Float
        get() = abs(x1 - x0)

    val height: Float
        get() = abs(y1 - y0)

    val spanX: Float
        get() = x1 - x0

    val spanY: Float
        get() = y1 - y0

    val square: Float
        get() = min(width, height)

    val squareSpanX: Float
        get() = if (isInvertedX) -square else square

    val squareSpanY: Float
        get() = if (isInvertedY) -square else square

    val intX0: Int
        get() = x0.roundToInt()

    val intX1: Int
        get() = x1.roundToInt()

    val intY0: Int
        get() = y0.roundToInt()

    val intY1: Int
        get() = y1.roundToInt()

    val intLeft: Int
        get() = left.roundToInt()

    val intTop: Int
        get() = top.roundToInt()

    val intRight: Int
        get() = right.roundToInt()

    val intBottom: Int
        get() = bottom.roundToInt()

    val intWidth: Int
        get() = width.roundToInt()

    val intHeight: Int
        get() = height.roundToInt()

    val intSpanX: Int
        get() = spanX.roundToInt()

    val intSpanY: Int
        get() = spanY.roundToInt()

    val intSquare: Int
        get() = square.roundToInt()

    val intSquareSpanX: Int
        get() = squareSpanX.roundToInt()

    val intSquareSpanY: Int
        get() = squareSpanY.roundToInt()

    val normalized: Rectangle
        get() {
            val invX = isInvertedX
            val invY = isInvertedY
            return invert(invX, invY)
        }

    val topLeft: Point get() = Point.of(left, top)
    val topRight: Point get() = Point.of(right, top)
    val bottomLeft: Point get() = Point.of(left, bottom)
    val bottomRight: Point get() = Point.of(right, bottom)

    val cornerRadius: Float
        get() = radius.minLimit(0f)

    val intCornerRadius: Int
        get() = radius.toInt().minLimit(0)

    val round: Rectangle
        get() = copyOf(this, x0.roundToInt(), x1.roundToInt(), y0.roundToInt(), y1.roundToInt(), radius.roundToInt())

    fun roundTo(value: Float): Rectangle {
        return copyOf(this, x0.roundTo(value), x1.roundTo(value), y0.roundTo(value), y1.roundTo(value), radius.roundTo(value))
    }

    fun invert(invX: Boolean, invY: Boolean): Rectangle {
        return when {
            invX && invY -> copyOf(this, x1, x0, y1, y0, radius)
            invX -> copyOf(this, x1, x0, y0, y1, radius)
            invY -> copyOf(this, x0, x1, y1, y0, radius)
            else -> this
        }
    }

    fun withRadius(radius: Float): Rectangle {
        return copyOf(this, x0, x1, y0, y1, radius)
    }

    fun withMaxRadius(radius: Float): Rectangle {
        return if (this.radius < radius) copyOf(this, x0, x1, y0, y1, radius) else this
    }

    fun withMinRadius(radius: Float): Rectangle {
        return if (this.radius > radius) copyOf(this, x0, x1, y0, y1, radius) else this
    }

    fun withRadius(radius: Int): Rectangle {
        return withRadius(radius.toFloat())
    }

    fun withMinRadius(radius: Int): Rectangle {
        return withMinRadius(radius.toFloat())
    }

    fun withMaxRadius(radius: Int): Rectangle {
        return withMaxRadius(radius.toFloat())
    }

    fun constrained(): Rectangle {
        return copyOf(this, x0, x0 + squareSpanX, y0, y0 + squareSpanY, radius)
    }

    fun constrained(enable: Boolean): Rectangle {
        return if (enable) copyOf(this, x0, x0 + squareSpanX, y0, y0 + squareSpanY, radius) else this
    }

    // TEST: needs tests
    fun clipBy(other: Rectangle): Rectangle {
        return inclusiveClipBy(other, false)
    }

    // TEST: needs tests
    fun inclusiveClipBy(other: Rectangle): Rectangle {
        return inclusiveClipBy(other, true)
    }

    // TEST: needs tests
    fun inclusiveClipBy(other: Rectangle, inclusive: Boolean): Rectangle {
        val invX = isInvertedX
        val invY = isInvertedY

        val normThis = normalized
        val normOther = other.normalized

        val clipped = normThis.normClipBy(normOther, inclusive)
            .invert(invX, invY)

        return when {
            this == clipped -> this
            other == clipped -> other
            else -> clipped
        }
    }

    private fun normClipBy(other: Rectangle, inclusive: Boolean): Rectangle {
        val rightMax = if (inclusive && other.width > 0) other.right - 1 else other.right
        val bottomMax = if (inclusive && other.height > 0) other.bottom - 1 else other.bottom
        val clipped = of(
            left.rangeLimit(other.left, rightMax),
            right.rangeLimit(other.left, rightMax),
            top.rangeLimit(other.top, bottomMax),
            bottom.rangeLimit(other.top, bottomMax),
            radius)

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
        return translate(x.toFloat(), y.toFloat())
    }

    fun translate(x: Float, y: Float): Rectangle {
        return if (x != 0f || y != 0f) of(x0 + x, x1 + x, y0 + y, y1 + y, radius) else this
    }

    fun topLeftTo0(): Rectangle {
        return normalized.translate(-left, -top)
    }

    fun center(): Point {
        return Point.of((x0 + x1) / 2f, (y0 + y1) / 2f)
    }

    fun translateCenterTo0(): Rectangle {
        val c = center()
        return normalized.translate(-c.x, -c.y)
    }

    fun nullIfInverted(): Rectangle {
        return if (isInvertedX || isInvertedY) NULL else this
    }

    fun shrink(border: Int): Rectangle {
        return grow(-border, -border, -border, -border)
    }

    fun shrink(x: Int, y: Int): Rectangle {
        return grow(-x, -x, -y, -y)
    }

    fun shrink(x0: Int, x1: Int, y0: Int, y1: Int): Rectangle {
        return grow(-x0, -x1, -y0, -y1)
    }

    fun grow(border: Int): Rectangle {
        return grow(border, border, border, border)
    }

    fun grow(x: Int, y: Int): Rectangle {
        return grow(x, x, y, y)
    }

    fun grow(left: Int, right: Int, top: Int, bottom: Int): Rectangle {
        return grow(left.toFloat(), right.toFloat(), top.toFloat(), bottom.toFloat())
    }

    fun shrink(border: Float): Rectangle {
        return grow(-border, -border, -border, -border)
    }

    fun shrink(x: Float, y: Float): Rectangle {
        return grow(-x, -x, -y, -y)
    }

    fun shrink(x0: Float, x1: Float, y0: Float, y1: Float): Rectangle {
        return grow(-x0, -x1, -y0, -y1)
    }

    fun grow(border: Float): Rectangle {
        return grow(border, border, border, border)
    }

    fun grow(x: Float, y: Float): Rectangle {
        return grow(x, x, y, y)
    }

    fun grow(left: Float, right: Float, top: Float, bottom: Float): Rectangle {
        if (left != 0f || top != 0f || right != 0f || bottom != 0f) {
            val invX = isInvertedX
            val invY = isInvertedY
            val avg = if (left == right && left == top && left == bottom) left else 0f

            return of(this.left - left, this.right + right, this.top - top, this.bottom + bottom, this.radius + avg).invert(invX, invY)
        }

        return this
    }

    fun scale(scale: Float): Rectangle {
        return scale(scale, scale)
    }

    fun scale(x: Float, y: Float): Rectangle {
        if (x != 0f && y != 0f && (x != 1f || y != 1f)) {
            val avgScale = if (x.absoluteValue == y.absoluteValue) x.absoluteValue else 1f
            return copyOf(this, (x0 * x), (x1 * x), (y0 * y), (y1 * y), if (avgScale == 0f || avgScale == 1f) radius else radius * avgScale)
        }
        return this
    }

    fun rotate(deg: Int, x: Int, y: Int): Rectangle {
        if (deg % 360 == 0) {
            return this;
        }
        return rotate(toRadians(deg.toDouble()).toFloat(), x.toFloat(), y.toFloat())
    }

    fun rotate(deg: Int, c: Point): Rectangle {
        if (deg % 360 == 0) {
            return this;
        }
        return rotate(toRadians(deg.toDouble()).toFloat(), c.x, c.y)
    }

    fun rotate(rad: Float, c: Point): Rectangle {
        return rotate(rad, c.x, c.y)
    }

    fun rotate(rad: Float, x: Float, y: Float): Rectangle {
        val rot = Complex.rot(rad.toDouble()).roundTo()
        if (rot.isUnitR()) return this

        val r = translate(-x, -y)
        val p0 = Complex.of(r.topLeft).times(rot).roundTo().toPoint()
        val p1 = Complex.of(r.topRight).times(rot).roundTo().toPoint()
        val p2 = Complex.of(r.bottomRight).times(rot).roundTo().toPoint()
        val p3 = Complex.of(r.bottomLeft).times(rot).roundTo().toPoint()
        val result = if (rot.isAxisRot()) {
            // special case, we leave the coordinates unmolested and use p0 as the topLeft and p2 as bottom right
            of(p0, p2, radius)
        } else {
            of(min(p0.x, min(p1.x, min(p2.x, p3.x))), max(p0.x, max(p1.x, max(p2.x, p3.x))), min(p0.y, min(p1.y, min(p2.y, p3.y))), max(p0.y, max(p1.y, max(p2.y, p3.y))), radius)
        }
        return result
    }

    fun rotateBounded(deg: Int, bounds: Rectangle): Rectangle {
        return rotateBounded(toRadians(deg.toDouble()).toFloat(), bounds)
    }

    fun rotateBounded(rad: Float, bounds: Rectangle): Rectangle {
        // have to apply all transformations that were done to the bounds rectangle, including translation to top/left
        val boundsRotated = bounds.rotate(rad, bounds.center())
        val boundsNormalized = boundsRotated.normalized
        return rotate(rad, bounds.center())
            .invert(boundsRotated.isInvertedX, boundsRotated.isInvertedY)
            .translate(-boundsNormalized.left, -boundsNormalized.top)
            .normalized
    }

    override fun toString(): String {
        return "Rectangle(x0=$x0, x1=$x1, y0=$y0, y1=$y1, radius=$radius)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rectangle

        if (x0 != other.x0) return false
        if (x1 != other.x1) return false
        if (y0 != other.y0) return false
        if (y1 != other.y1) return false
        if (radius != other.radius) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x0.hashCode()
        result = 31 * result + x1.hashCode()
        result = 31 * result + y0.hashCode()
        result = 31 * result + y1.hashCode()
        result = 31 * result + radius.hashCode()
        return result
    }

    companion object {

        @JvmField
        val NULL = Rectangle(0f, 0f, 0f, 0f, 0f)

        private fun copyOf(other: Rectangle, x0: Float, x1: Float, y0: Float, y1: Float, radius: Float): Rectangle {
            return if (other.x0 == x0 && other.x1 == x1 && other.y0 == y0 && other.y1 == y1 && radius == other.radius) other
            else of(x0, x1, y0, y1, radius)
        }

        private fun copyOf(other: Rectangle, x0: Int, x1: Int, y0: Int, y1: Int, radius: Int): Rectangle {
            return if (other.x0 == x0.toFloat() && other.x1 == x1.toFloat() && other.y0 == y0.toFloat() && other.y1 == y1.toFloat() && other.radius == radius.toFloat()) other
            else of(x0, x1, y0, y1, radius)
        }

        @JvmStatic
        fun of(x0: Float, x1: Float, y0: Float, y1: Float, radius: Float): Rectangle {
            return if (x0 == 0f && x1 == 0f && y0 == 0f && y1 == 0f && radius == 0f) NULL else Rectangle(x0, x1, y0, y1, radius)
        }

        @JvmStatic
        fun of(topLeft: Point, bottomRight: Point): Rectangle {
            return of(topLeft.x, bottomRight.x, topLeft.y, bottomRight.y, 0f)
        }

        @JvmStatic
        fun of(topLeft: Point, bottomRight: Point, radius: Float): Rectangle {
            return of(topLeft.x, bottomRight.x, topLeft.y, bottomRight.y, radius)
        }

        @JvmStatic
        fun fromBottomRight(left: Float, top: Float, right: Float, bottom: Float, radius: Float): Rectangle {
            return if (left == 0f && right == 0f && top == 0f && bottom == 0f && radius == 0f) NULL else Rectangle(left, right, top, bottom, radius)
        }

        @JvmStatic
        fun fromWidthHeight(left: Float, top: Float, width: Float, height: Float, radius: Float): Rectangle {
            return if (left == 0f && width == 0f && top == 0f && height == 0f && radius == 0f) NULL else Rectangle(left, left + width, top, top + height, radius)
        }

        @JvmStatic
        fun of(x0: Int, x1: Int, y0: Int, y1: Int, radius: Int): Rectangle {
            return of(x0.toFloat(), x1.toFloat(), y0.toFloat(), y1.toFloat(), radius.toFloat())
        }

        @JvmStatic
        fun fromBottomRight(left: Int, top: Int, right: Int, bottom: Int, radius: Int): Rectangle {
            return fromBottomRight(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), radius.toFloat())
        }

        @JvmStatic
        fun fromWidthHeight(left: Int, top: Int, width: Int, height: Int, radius: Int): Rectangle {
            return fromWidthHeight(left.toFloat(), top.toFloat(), width.toFloat(), height.toFloat(), radius.toFloat())
        }

        @JvmStatic
        fun of(image: BufferedImage): Rectangle {
            return of(0, image.width, 0, image.height, 0)
        }
    }
}
