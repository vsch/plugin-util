package com.vladsch.plugin.util.image

import org.junit.Assert.assertSame
import org.junit.Test

class ComplexTest {

    @Test
    fun test_NULL() {
        assertSame(Complex.ZERO, Complex.of(0, 0))
        assertSame(Complex.ZERO, Complex.of(0.0, 0.0))
    }

    @Test
    fun test_Unit() {
        assert(Complex.unitR * (-Complex.unitR) == -Complex.unitR)
        assert(-Complex.unitI * Complex.unitI == Complex.unitR)
        assert(Complex.unitI * Complex.unitI == -Complex.unitR)
    }

    @Test
    fun test_times() {
        val complex0 = Complex.of(1, 0)
        val complex1 = Complex.of(0, 1)
        val complex2 = Complex.of(0, -1)
        val complex3 = Complex.of(-1, 0)

        assertSame(complex0, Complex.unitR)
        assertSame(complex1, Complex.unitI)
        assert(complex1 * complex1 == complex3)
        assert(complex0 * complex0 == complex0)
        assert(complex1 * complex0 == complex1)
        assert(complex2 * complex0 == complex2)
        assert(complex3 * complex0 == complex3)
        assert(complex0 * complex3 == complex3)
        assert(complex1 * complex3 == complex2)
        assert(complex2 * complex3 == complex1)
        assert(complex3 * complex3 == complex0)
    }

    @Test
    fun test_rotate() {
        val rot90 = Complex.rot(90)
        val rot_90 = Complex.rot(-90)
        val rot45 = Complex.rot(45)
        val rot_45 = Complex.rot(-45)
        val rot30 = Complex.rot(30)
        val rot_30 = Complex.rot(-30)

        assert((rot90 * rot90).roundTo() == -Complex.unitR)
        assert((rot90 * rot_90).roundTo() == Complex.unitR)
        assert((rot45 * rot45).roundTo() == rot90.roundTo())
        assert((rot45 * rot_45).roundTo() == Complex.unitR)
        assert((rot30 * rot30 * rot30).roundTo() == rot90.roundTo())
        assert((rot30 * rot_30).roundTo() == Complex.unitR)
    }
}
