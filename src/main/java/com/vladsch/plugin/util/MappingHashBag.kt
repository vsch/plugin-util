package com.vladsch.plugin.util

import java.util.function.Function

open class MappingHashBag<E : Any, T : Any>(val mapper: Function<E, T>) : MappingBag<E, T> {
    val elementCounts = HashBag<T>()

    override fun map(element: E): T {
        return mapper.apply(element)
    }

    override fun add(element: E): Int {
        return elementCounts.add(map(element))
    }

    override fun addAll(elements: Collection<E>?) {
        if (elements != null) {
            for (element in elements) {
                add(element)
            }
        }
    }

    override fun remove(element: E): Boolean {
        return elementCounts.remove(map(element))
    }

    override fun removeAll(elements: Collection<E>?) {
        if (elements != null) {
            for (element in elements) {
                remove(element)
            }
        }
    }

    override fun clear() {
        elementCounts.clear()
    }

    override fun count(vararg elements: E): Int {
        var count = 0
        for (element in elements) {
            count += elementCounts.count(map(element))
        }
        return count
    }

    override fun countMapped(vararg mapped: T): Int {
        return elementCounts.count(*mapped)
    }

    override fun get(element: E): Int {
        return elementCounts[mapper.apply(element)]
    }

    override fun size(): Int {
        return elementCounts.size()
    }
}
