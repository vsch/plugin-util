/*
 *
 */

package com.vladsch.plugin.util.image

import kotlin.math.roundToLong

fun Float.roundTo(value: Float): Float {
    assert(value != 0f) { "roundTo argument cannot be 0" }
    return (this / value).roundToLong() * value
}
