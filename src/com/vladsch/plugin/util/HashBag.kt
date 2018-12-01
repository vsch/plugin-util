/*
 * Copyright (c) 2016-2018 Vladimir Schneider <vladimir.schneider@gmail.com>
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package com.vladsch.plugin.util

import java.util.*

open class HashBag<T : Any>() : Bag<T> {
    val elementCounts = HashMap<T, Int>()

    override fun get(element: T): Int {
        return count(element)
    }

    override fun add(element: T): Int {
        val stat = elementCounts[element]
        if (stat == null) {
            elementCounts[element] = 1
            return 1
        } else {
            elementCounts[element] = stat + 1
            return stat + 1
        }
    }

    override fun addAll(elements: Collection<T>?) {
        if (elements != null) {
            for (element in elements) {
                add(element)
            }
        }
    }

    override fun remove(element: T): Boolean {
        val stat = elementCounts[element]
        if (stat != null) {
            elementCounts[element] = stat - 1
            return true
        }
        return false
    }

    override fun removeAll(elements: Collection<T>?) {
        if (elements != null) {
            for (element in elements) {
                remove(element)
            }
        }
    }

    override fun clear() {
        elementCounts.clear()
    }

    override fun count(vararg elements: T): Int {
        var count = 0
        for (element in elements) {
            count += elementCounts[element] ?: 0
        }
        return count
    }

    override fun size(): Int {
        var size = 0
        for (count in elementCounts.values) {
            size += count
        }
        return size
    }
}
