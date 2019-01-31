/*
 * Copyright (c) 2015-2019 Vladimir Schneider <vladimir.schneider@gmail.com>, all rights reserved.
 *
 * This code is private property of the copyright holder and cannot be used without
 * having obtained a license or prior written permission of the of the copyright holder.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package com.vladsch.plugin.util

import com.intellij.lang.ASTNode
import com.vladsch.flexmark.util.sequence.BasedSequence

fun Int.after(other: Int): Boolean = this > other
fun Int.afterStart(other: Int): Boolean = this > other
fun Int.afterEnd(other: Int): Boolean = this >= other
fun Int.at(other: Int): Boolean = this == other
fun Int.beforeStart(other: Int): Boolean = this <= other
fun Int.before(other: Int): Boolean = this < other
fun Int.beforeEnd(other: Int): Boolean = this < other

fun Int.afterStart(range: NodeSubRange): Boolean = this.afterStart(range.start)
fun Int.atStart(range: NodeSubRange): Boolean = this.at(range.start)
fun Int.beforeStart(range: NodeSubRange): Boolean = this.beforeStart(range.start)

fun Int.afterEnd(range: NodeSubRange): Boolean = this.afterEnd(range.end)
fun Int.atEnd(range: NodeSubRange): Boolean = this.at(range.end)
fun Int.beforeEnd(range: NodeSubRange): Boolean = this.beforeEnd(range.end)

fun Int.around(range: NodeSubRange): Boolean = this in range.start .. range.end
fun Int.inside(range: NodeSubRange): Boolean = this in (range.start + 1) .. (range.end - 1)

open class NodeSubRange(val start: Int, val end: Int, val text: CharSequence) {
    val length: Int get() = text.length
    init {
        assert(start in 0 .. end)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NodeSubRange) return false

        if (start != other.start) return false
        if (end != other.end) return false
        if (text != other.text) return false

        return true
    }

    override fun hashCode(): Int {
        var result = start
        result = 31 * result + end
        result = 31 * result + text.hashCode()
        return result
    }

    override fun toString(): String {
        return "SubRange(start=$start, end=$end, text=\"$text\")"
    }
}

class NodeRange(val node: ASTNode, start: Int, end: Int, text: CharSequence) : NodeSubRange(start, end, text) {
    val nonSpace:NodeSubRange
    val trailingSpace:NodeSubRange

    init {
        val spacePos = text.asBased().lastIndexOfAnyNot(BasedSequence.WHITESPACE_NO_EOL_CHARS).indexOrNull()?.plus(1) ?: end
        nonSpace = NodeSubRange(start, end - (text.length - spacePos), text.subSequence(0, spacePos))
        trailingSpace = NodeSubRange(nonSpace.end, end, text.subSequence(spacePos, length))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NodeRange) return false
        if (!super.equals(other)) return false

        if (node != other.node) return false
        if (nonSpace != other.nonSpace) return false
        if (trailingSpace != other.trailingSpace) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + node.hashCode()
        result = 31 * result + nonSpace.hashCode()
        result = 31 * result + trailingSpace.hashCode()
        return result
    }

    override fun toString(): String {
        return "NodeRange(node=$node, start=$start, end=$end, text=\"$text\", nonSpace=$nonSpace, trailingSpace=$trailingSpace)"
    }
}


