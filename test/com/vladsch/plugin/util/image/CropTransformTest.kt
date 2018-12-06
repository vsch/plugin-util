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

import org.junit.Assert.assertEquals
import org.junit.Test

class CropTransformTest {

    @Test
    fun test_transImage() {
        val trans = CropTransform(Rectangle.of(1, 2, 5, 0))
        val rect = Rectangle.of(0, 10, 0, 20)
        assertEquals(Rectangle.of(0, 7, 0, 15), trans.transformImage(rect))
        assertEquals(Rectangle.of(0, 13, 0, 25), trans.reverseImage(rect))
        assertEquals(Rectangle.of(0, 7, 0, 15), trans.reversed().reverseImage(rect))
        assertEquals(Rectangle.of(0, 13, 0, 25), trans.reversed().transformImage(rect))
        assertEquals(rect, trans.transformImage(trans.reverseImage( rect)))
        assertEquals(rect, trans.reverseImage(trans.transformImage(rect)))
    }

    @Test
    fun test_transRect() {
        val trans = CropTransform(Rectangle.of(1, 2, 5, 0))
        val rect = Rectangle.of(0, 10, 0, 20)
        assertEquals(Rectangle.of(-1, 9, -5, 15), trans.transform(rect))
        assertEquals(Rectangle.of(1, 11, 5, 25), trans.reverse(rect))
        assertEquals(Rectangle.of(-1, 9, -5, 15), trans.reversed().reverse(rect))
        assertEquals(Rectangle.of(1, 11, 5, 25), trans.reversed().transform(rect))
        assertEquals(rect, trans.reverse(trans.transform(rect)))
        assertEquals(rect, trans.transform(trans.reverse(rect)))
    }

    @Test
    fun test_transPoint() {
        val trans = CropTransform(Rectangle.of(1, 2, 5, 0))
        val point = Point.of(10, 20)
        assertEquals(Point.of(9, 15), trans.transform(point))
        assertEquals(Point.of(11, 25), trans.reverse(point))
        assertEquals(Point.of(9, 15), trans.reversed().reverse(point))
        assertEquals(Point.of(11, 25), trans.reversed().transform(point))
        assertEquals(point, trans.reverse(trans.transform(point)))
        assertEquals(point, trans.transform(trans.reverse(point)))
    }
}
