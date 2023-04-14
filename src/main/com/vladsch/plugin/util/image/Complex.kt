package com.vladsch.plugin.util.image

import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Complex private constructor(val r: Double, val i: Double) {

    constructor(p: Point) : this(p.x.toDouble(), p.y.toDouble())
    constructor(r: Int, i: Int) : this(r.toDouble(), i.toDouble())

    fun roundTo(value: Double = roundTo): Complex {
        return copyOf(this, r.roundTo(value), i.roundTo(value))
    }

    operator fun plus(o: Complex): Complex {
        return Complex(r + o.r, i + o.i)
    }

    operator fun minus(o: Complex): Complex {
        return Complex(r - o.r, i - o.i)
    }

    operator fun times(o: Complex): Complex {
        return Complex(r * o.r - i * o.i, r * o.i + i * o.r)
    }

    operator fun unaryMinus(): Complex {
        return Complex(if (r == 0.0) 0.0 else -r, if (i == 0.0) 0.0 else -i)
    }

    operator fun unaryPlus(): Complex {
        return this;
    }

    operator fun div(o: Complex): Complex {
        return times(o.inv())
    }

    infix fun dot(o: Complex): Double {
        return r * o.r + i * o.i
    }

    fun dot(): Double {
        return r * r + i * i
    }

    fun inv(): Complex {
        val den = dot()
        return Complex(r / den, -i / den)
    }

    fun norm(): Complex {
        val m = abs()
        return Complex(r / m, i / m)
    }

    fun abs(): Double {
        return sqrt(dot())
    }

    fun isUnit(): Boolean = dot().roundTo(roundTo) == 1.0

    fun isUnitI(): Boolean = (this.roundTo() == unitI)

    fun isUnitR(): Boolean = (this.roundTo() == unitR)

    fun isZero(): Boolean = (this.roundTo() == ZERO)

    fun toPoint(): Point = Point.of(r.toFloat(), i.toFloat())
    
    fun isAxisRot(): Boolean {
        val r = roundTo()
        return (r.isUnit() && (r.r == 1.0 || r.i == 1.0 || r.r == -1.0 || r.i == -1.0 )) 
    }

    override fun equals(other: Any?): Boolean {
        return other is Complex && r == other.r && i == other.i;
    }

    override fun toString(): String {
        return "Complex(r=$r, i=$i)"
    }

    override fun hashCode(): Int {
        var result = r.hashCode()
        result = 31 * result + i.hashCode()
        return result
    }

    companion object {

        @JvmField
        val ZERO: Complex = Complex(0.0, 0.0)
        @JvmField
        val unitR: Complex = Complex(1.0, 0.0)
        @JvmField
        val unitI: Complex = Complex(0.0, 1.0)
        const val roundTo: Double = 1.0E-6

        private fun copyOf(other: Complex, r: Double, i: Double): Complex {
            return if (other.r == r && other.i == i) other
            else of(r, i)
        }

        @JvmStatic
        fun of(r: Double, i: Double): Complex {
            val roundedR = r.roundTo(roundTo)
            val roundedI = i.roundTo(roundTo)
            return if (roundedR == 0.0 && roundedI == 0.0) ZERO
            else if (roundedR == 0.0 && roundedI == 1.0) unitI
            else if (roundedR == 1.0 && roundedI == 0.0) unitR
            else Complex(r, i)
        }

        @JvmStatic
        fun of(r: Int, i: Int): Complex {
            return of(r.toDouble(), i.toDouble());
        }

        @JvmStatic
        fun of(p: Point): Complex {
            return of(p.x.toDouble(), p.y.toDouble())
        }

        @JvmStatic
        fun rot(deg: Int): Complex {
            return rot(toRadians(deg.toDouble()))
        }

        @JvmStatic
        fun rot(rad: Double): Complex {
            return Complex(cos(rad), sin(rad))
        }
    }
}

fun Point.toComplex(): Complex = Complex.of(this)
