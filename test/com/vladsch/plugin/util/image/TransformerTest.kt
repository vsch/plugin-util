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

class TransformerTest {

    @Test
    fun test_transImage() {
        val scale = ScaleTransform(2f, 5f)
        val crop = CropTransform(Rectangle.of(1, 2, 5, 0))
        val border = BorderTransform(1)
        val trans = Transformer(listOf(crop, scale, border))
        
        val rect = Rectangle.of(0, 10, 0, 20)
        assertEquals(border.transformImage(scale.transformImage(crop.transformImage(rect))), trans.transformImage(rect))
        assertEquals(crop.reverseImage(scale.reverseImage(border.reverseImage(rect))), trans.reverseImage(rect))
        assertEquals(border.transformImage(scale.transformImage(crop.transformImage(rect))), trans.reversed().reverseImage(rect))
        assertEquals(crop.reverseImage(scale.reverseImage(border.reverseImage(rect))), trans.reversed().transformImage(rect))
        assertEquals(rect, trans.transformImage(trans.reverseImage(rect)))
        assertEquals(rect, trans.reverseImage(trans.transformImage(rect)))
    }

    @Test
    fun test_transRect() {
        val scale = ScaleTransform(2f, 5f)
        val crop = CropTransform(Rectangle.of(1, 2, 5, 0))
        val border = BorderTransform(1)
        val trans = Transformer(listOf(crop, scale, border))
        
        val rect = Rectangle.of(0, 10, 0, 20)
        
        println("trans rect: $rect transformed: ${trans.transform(rect)}")
        println("trans rect: $rect reversed: ${trans.reverse(rect)}")
        
        assertEquals(border.transform(scale.transform(crop.transform(rect))), trans.transform(rect))
        assertEquals(crop.reverse(scale.reverse(border.reverse(rect))), trans.reverse(rect))
        assertEquals(border.transform(scale.transform(crop.transform(rect))), trans.reversed().reverse(rect))
        assertEquals(crop.reverse(scale.reverse(border.reverse(rect))), trans.reversed().transform(rect))
        assertEquals(rect, trans.reverse(trans.transform(rect)))
        // in general this will hold only if there is no rounding. In our case int it does not hold
        // assertEquals(rect, trans.transform(trans.reverse(rect)))
    }

    @Test
    fun test_transPoint() {
        val scale = ScaleTransform(2f, 5f)
        val crop = CropTransform(Rectangle.of(1, 2, 5, 0))
        val border = BorderTransform(1)
        val trans = Transformer(listOf(crop, scale, border))
        
        val point = Point.of(10, 20)
        assertEquals(border.transform(scale.transform(crop.transform(point))), trans.transform(point))
        assertEquals(crop.reverse(scale.reverse(border.reverse(point))), trans.reverse(point))
        assertEquals(border.transform(scale.transform(crop.transform(point))), trans.reversed().reverse(point))
        assertEquals(crop.reverse(scale.reverse(border.reverse(point))), trans.reversed().transform(point))
        assertEquals(point, trans.reverse(trans.transform(point)))
        // in general this will hold only if there is no rounding. In our case int it does not hold
        // assertEquals(point, trans.transform(trans.reverse(point)))
    }
}
        
