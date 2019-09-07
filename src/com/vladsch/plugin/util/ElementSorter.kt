package com.vladsch.plugin.util

import com.intellij.psi.PsiElement

class ElementSorter {
    companion object {
        @JvmStatic
        fun <T : PsiElement> sorted(elements: Collection<T>): List<T> {
            return elements.sortedWith(Comparator<T> { o1, o2 ->
                if (o1.textOffset == o2.textOffset) o1.textLength.compareTo(o2.textLength)
                else o1.textOffset.compareTo(o2.textOffset)
            })
        }
    }
}
