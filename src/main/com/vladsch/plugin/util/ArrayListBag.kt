package com.vladsch.plugin.util

import java.util.*
import java.util.function.Function

open class ArrayListBag<E : Any, T : Any>(val mapper: Function<E, T>) : MutableList<E>, Function<E, T> by mapper {
    constructor(mapper: (E) -> T) : this(Function { mapper(it) })

    val elementList = ArrayList<E>()
    val elementBag = MappingHashBag<E, T>(mapper)

    override fun add(index: Int, element: E) {
        elementList.add(index, element)
        elementBag.add(element)
    }

    override fun add(element: E): Boolean {
        val result = elementList.add(element)
        elementBag.add(element)
        return result
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        val result = elementList.addAll(index, elements)
        if (result) {
            elementBag.addAll(elements)
        }
        return result
    }

    override fun addAll(elements: Collection<E>): Boolean {
        val result = elementList.addAll(elements)
        if (result) {
            elementBag.addAll(elements)
        }
        return result
    }

    fun count(vararg elements: E): Int {
        return elementBag.count(*elements)
    }

    fun countMapped(vararg types: T): Int {
        return elementBag.countMapped(*types)
    }

    override val size: Int get() = elementList.size

    override fun clear() {
        elementList.clear()
        elementBag.clear()
    }

    protected class CountingIterator<E : Any>(private val iterator: MutableListIterator<E>, private val bag: Bag<E>) : MutableListIterator<E> {
        override fun hasPrevious(): Boolean {
            return iterator.hasPrevious()
        }

        override fun nextIndex(): Int {
            return iterator.nextIndex()
        }

        override fun previous(): E {
            return iterator.previous()
        }

        override fun previousIndex(): Int {
            return iterator.previousIndex()
        }

        override fun add(element: E) {
            iterator.add(element)
            bag.add(element)
        }

        override fun hasNext(): Boolean {
            return iterator.hasNext()
        }

        override fun next(): E {
            return iterator.next()
        }

        private fun current(): E? {
            if (hasNext()) {
                next()
                return previous()
            } else if (hasPrevious()) {
                previous()
                return next()
            }
            return null
        }

        override fun remove() {
            val current = current()
            remove()
            if (current != null) bag.remove(current)
        }

        override fun set(element: E) {
            val current = current()
            if (current != null) bag.remove(current)
            set(element)
            bag.add(element)
        }
    }

    override fun listIterator(): MutableListIterator<E> {
        return CountingIterator<E>(elementList.listIterator(), elementBag)
    }

    override fun listIterator(index: Int): MutableListIterator<E> {
        return CountingIterator<E>(elementList.listIterator(index), elementBag)
    }

    override fun iterator(): MutableIterator<E> {
        return CountingIterator<E>(elementList.listIterator(), elementBag)
    }

    override fun remove(element: E): Boolean {
        if (elementBag.remove(element)) {
            elementList.remove(element)
            return true
        }
        return false
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        val result = elementList.removeAll(elements)
        if (result) elementBag.removeAll(elements)
        return result
    }

    override fun removeAt(index: Int): E {
        val result = elementList.removeAt(index)
        elementBag.remove(result)
        return result
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        val result = elementList.retainAll(elements)
        if (result) elementBag.addAll(elements)
        return result
    }

    override fun set(index: Int, element: E): E {
        val result = elementList.set(index, element)
        elementBag.remove(result)
        elementBag.add(element)
        return result
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<E> {
        val result = ArrayListBag(mapper)
        result.addAll(elementList.subList(fromIndex, toIndex))
        return result
    }

    override fun contains(element: E): Boolean {
        return elementBag[element] > 0
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        for (element in elements) {
            if (elementBag[element] == 0) return false
        }
        return true
    }

    override fun get(index: Int): E {
        return elementList[index]
    }

    override fun indexOf(element: E): Int {
        if (contains(element)) return elementList.indexOf(element)
        return -1
    }

    override fun isEmpty(): Boolean {
        return elementList.isEmpty()
    }

    override fun lastIndexOf(element: E): Int {
        if (contains(element)) return elementList.indexOf(element)
        return -1
    }
}
