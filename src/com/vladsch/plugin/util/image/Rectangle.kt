package com.vladsch.plugin.util.image

import com.vladsch.plugin.util.rangeLimit
import java.awt.image.BufferedImage
import kotlin.math.roundToInt

@Suppress("MemberVisibilityCanBePrivate")
open class Rectangle protected constructor(
        @JvmField val x0: Float,
        @JvmField val x1: Float,
        @JvmField val y0: Float,
        @JvmField val y1: Float
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
        get() = Math.abs(x1 - x0)

    val height: Float
        get() = Math.abs(y1 - y0)

    val spanX: Float
        get() = x1 - x0

    val spanY: Float
        get() = y1 - y0

    val square: Float
        get() = Math.min(width, height)

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

    val round: Rectangle
        get() = Rectangle.copyOf(this, x0.roundToInt(), x1.roundToInt(), y0.roundToInt(), y1.roundToInt())

    fun invert(invX: Boolean, invY: Boolean): Rectangle {
        return when {
            invX && invY -> Rectangle.of(x1, x0, y1, y0)
            invX -> Rectangle.of(x1, x0, y0, y1)
            invY -> Rectangle.of(x0, x1, y1, y0)
            else -> this
        }
    }

    fun constrained(): Rectangle {
        return Rectangle.copyOf(this, x0, x0 + squareSpanX, y0, y0 + squareSpanY)
    }

    fun constrained(enable: Boolean): Rectangle {
        return if (enable) Rectangle.copyOf(this, x0, x0 + squareSpanX, y0, y0 + squareSpanY) else this
    }

    fun clipBy(other: Rectangle): Rectangle {
        val invX = isInvertedX
        val invY = isInvertedY

        val clipped = Rectangle.of(
                left.rangeLimit(other.left, other.right),
                right.rangeLimit(other.left, other.right),
                top.rangeLimit(other.top, other.bottom),
                bottom.rangeLimit(other.top, other.bottom))
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
        return translate(x.toFloat(), y.toFloat())
    }

    fun translate(x: Float, y: Float): Rectangle {
        return if (x != 0f || y != 0f) Rectangle.of(x0 + x, x1 + x, y0 + y, y1 + y) else this
    }

    fun topLeftTo0(): Rectangle {
        return normalized.translate(-left, -top)
    }

    fun translateCenterTo0(): Rectangle {
        return normalized.translate(-(spanX) / 2, -(spanY) / 2)
    }

    fun nullIfInverted(): Rectangle {
        return if (isInvertedX || isInvertedY) NULL else this
    }

    open fun shrink(border: Int): Rectangle {
        return grow(-border, -border, -border, -border)
    }

    fun shrink(x: Int, y: Int): Rectangle {
        return grow(-x, -x, -y, -y)
    }

    fun shrink(x0: Int, x1: Int, y0: Int, y1: Int): Rectangle {
        return grow(-x0, -x1, -y0, -y1)
    }

    open fun grow(border: Int): Rectangle {
        return grow(border, border, border, border)
    }

    fun grow(x: Int, y: Int): Rectangle {
        return grow(x, x, y, y)
    }

    fun grow(left: Int, right: Int, top: Int, bottom: Int): Rectangle {
        return grow(left.toFloat(), right.toFloat(), top.toFloat(), bottom.toFloat())
    }

    open fun shrink(border: Float): Rectangle {
        return grow(-border, -border, -border, -border)
    }

    fun shrink(x: Float, y: Float): Rectangle {
        return grow(-x, -x, -y, -y)
    }

    fun shrink(x0: Float, x1: Float, y0: Float, y1: Float): Rectangle {
        return grow(-x0, -x1, -y0, -y1)
    }

    open fun grow(border: Float): Rectangle {
        return grow(border, border, border, border)
    }

    fun grow(x: Float, y: Float): Rectangle {
        return grow(x, x, y, y)
    }

    fun grow(left: Float, right: Float, top: Float, bottom: Float): Rectangle {
        if (left != 0f || top != 0f || right != 0f || bottom != 0f) {
            val invX = isInvertedX
            val invY = isInvertedY

            return Rectangle.of(this.left - left, this.right + right, this.top - top, this.bottom + bottom).invert(invX, invY)
        }

        return this
    }

    fun scale(scale: Float): Rectangle {
        return scale(scale, scale)
    }

    fun scale(x: Float, y: Float): Rectangle {
        if (x != 0f && y != 0f && (x != 1f || y != 1f)) {
            return Rectangle.copyOf(this, (x0 * x), (x1 * x), (y0 * y), (y1 * y))
        }
        return this
    }

    override fun toString(): String {
        return "Rectangle(x0=$x0, x1=$x1, y0=$y0, y1=$y1)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rectangle

        if (x0 != other.x0) return false
        if (x1 != other.x1) return false
        if (y0 != other.y0) return false
        if (y1 != other.y1) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x0.hashCode()
        result = 31 * result + x1.hashCode()
        result = 31 * result + y0.hashCode()
        result = 31 * result + y1.hashCode()
        return result
    }

    companion object {
        @JvmField
        val NULL = Rectangle(0f, 0f, 0f, 0f)

        private fun copyOf(other: Rectangle, x0: Float, x1: Float, y0: Float, y1: Float): Rectangle {
            return if (other.x0 == x0 && other.x1 == x1 && other.y0 == y0 && other.y1 == y1) other
            else Rectangle.of(x0, x1, y0, y1)
        }

        @JvmStatic
        fun of(x0: Float, x1: Float, y0: Float, y1: Float): Rectangle {
            return if (x0 == 0f && x1 == 0f && y0 == 0f && y1 == 0f) NULL else Rectangle(x0, x1, y0, y1)
        }

        @JvmStatic
        fun fromBottomRight(left: Float, top: Float, right: Float, bottom: Float): Rectangle {
            return if (left == 0f && right == 0f && top == 0f && bottom == 0f) NULL else Rectangle(left, right, top, bottom)
        }

        @JvmStatic
        fun fromWidthHeight(left: Float, top: Float, width: Float, height: Float): Rectangle {
            return if (left == 0f && width == 0f && top == 0f && height == 0f) NULL else Rectangle(left, left + width, top, top + height)
        }

        private fun copyOf(other: Rectangle, x0: Int, x1: Int, y0: Int, y1: Int): Rectangle {
            return if (other.x0 == x0.toFloat() && other.x1 == x1.toFloat() && other.y0 == y0.toFloat() && other.y1 == y1.toFloat()) other
            else Rectangle.of(x0, x1, y0, y1)
        }

        @JvmStatic
        fun of(x0: Int, x1: Int, y0: Int, y1: Int): Rectangle {
            return Rectangle.of(x0.toFloat(), x1.toFloat(), y0.toFloat(), y1.toFloat())
        }

        @JvmStatic
        fun of(image: BufferedImage): Rectangle {
            return Rectangle.of(0, image.width, 0, image.height)
        }

        @JvmStatic
        fun fromBottomRight(left: Int, top: Int, right: Int, bottom: Int): Rectangle {
            return Rectangle.fromBottomRight(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
        }

        @JvmStatic
        fun fromWidthHeight(left: Int, top: Int, width: Int, height: Int): Rectangle {
            return Rectangle.fromWidthHeight(left.toFloat(), top.toFloat(), width.toFloat(), height.toFloat())
        }
    }
}
