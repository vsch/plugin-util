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
