/*
 * Copyright (c) 2015-2019 Vladimir Schneider <vladimir.schneider@gmail.com>, all rights reserved.
 *
 * This code is private property of the copyright holder and cannot be used without
 * having obtained a license or prior written permission of the copyright holder.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package com.vladsch.plugin.util.ui.highlight

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.ex.RangeHighlighterEx
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.editor.markup.TextAttributes
import com.vladsch.plugin.util.rangeLimit

class EditorRangeHighlighterSet(val editor: Editor) : MutableSet<RangeHighlighterEx> {
    private val rangeHighlighters = LinkedHashSet<RangeHighlighterEx>()

    private fun removeHighlighter(element: RangeHighlighter): Boolean {
        if (rangeHighlighters.contains(element)) {
            editor.markupModel.removeHighlighter(element)
            return true
        }
        return false
    }

    @JvmOverloads
    fun addRangeHighlighter(startOffset: Int,
        endOffset: Int,
        attributesKey: TextAttributesKey,
        highlighterLayer: Int = HighlighterLayer.ELEMENT_UNDER_CARET,
        targetArea: HighlighterTargetArea = HighlighterTargetArea.EXACT_RANGE
    ): RangeHighlighterEx {
        val attributes = editor.colorsScheme.getAttributes(attributesKey)
        return addRangeHighlighter(startOffset, endOffset, attributes, highlighterLayer, targetArea)
    }

    @JvmOverloads
    fun addRangeHighlighter(startOffset: Int,
        endOffset: Int,
        attributes: TextAttributes,
        highlighterLayer: Int = HighlighterLayer.ELEMENT_UNDER_CARET,
        targetArea: HighlighterTargetArea = HighlighterTargetArea.EXACT_RANGE
    ): RangeHighlighterEx {
        val rangeHighlighter = editor.markupModel.addRangeHighlighter(startOffset, endOffset.rangeLimit(0, editor.document.textLength), highlighterLayer, attributes, targetArea)
        rangeHighlighters.add(rangeHighlighter as RangeHighlighterEx)
        return rangeHighlighter
    }

    override fun add(element: RangeHighlighterEx): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addAll(elements: Collection<RangeHighlighterEx>): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clear() {
        rangeHighlighters.forEach { removeHighlighter(it) }
        rangeHighlighters.clear()
    }

    override fun iterator(): MutableIterator<RangeHighlighterEx> {
        return rangeHighlighters.iterator()
    }

    override fun remove(element: RangeHighlighterEx): Boolean {
        removeHighlighter(element)
        return rangeHighlighters.remove(element)
    }

    override fun removeAll(elements: Collection<RangeHighlighterEx>): Boolean {
        elements.forEach { removeHighlighter(it) }
        return rangeHighlighters.removeAll(elements)
    }

    override fun retainAll(elements: Collection<RangeHighlighterEx>): Boolean {
        val toRemove = rangeHighlighters.filter { elements.contains(it) }
        return removeAll(toRemove)
    }

    override val size: Int
        get() = rangeHighlighters.size

    override fun contains(element: RangeHighlighterEx): Boolean {
        return rangeHighlighters.contains(element)
    }

    override fun containsAll(elements: Collection<RangeHighlighterEx>): Boolean {
        return rangeHighlighters.containsAll(elements)
    }

    override fun isEmpty(): Boolean {
        return rangeHighlighters.isEmpty()
    }
}
