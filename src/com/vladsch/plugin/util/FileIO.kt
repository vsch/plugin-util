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

import com.vladsch.flexmark.util.FileUtil
import java.io.*

fun getResourceFiles(resourceClass: Class<*>, path: String, prefixPath: Boolean = false): List<String> {
    val filenames = ArrayList<String>()

    getResourceAsStream(resourceClass, path)?.use { inputStream ->
        BufferedReader(InputStreamReader(inputStream)).use { br ->
            while (true) {
                val resource = br.readLine() ?: break
                if (prefixPath) {
                    filenames.add(path + File.separator + resource)
                } else {
                    filenames.add(resource)
                }
            }
        }
    }

    return filenames
}

fun StringBuilder.streamAppend(inputStream: InputStream) {
    BufferedReader(InputStreamReader(inputStream)).use { br ->
        while (true) {
            val resource = br.readLine() ?: break
            this.append(resource).append('\n')
        }
    }
}

fun getResourceAsString(resourceClass: Class<*>, path: String): String {
    val sb = StringBuilder()

    getResourceAsStream(resourceClass, path)?.use { inputStream ->
        sb.streamAppend(inputStream)
    }

    return sb.toString()
}

fun getFileContent(file: File): String {
    val inputStream = FileInputStream(file)
    val sb = StringBuilder()
    sb.streamAppend(inputStream)
    return sb.toString()
}

fun getResourceAsStream(resourceClass: Class<*>, resource: String): InputStream? {
    try {
        val inputStream = resourceClass.getResourceAsStream(resource)
        inputStream.available()
        return inputStream
    } catch (e: Exception) {

    }
    return null
}

fun File.isChildOf(ancestor: File): Boolean = FileUtil.isChildOf(this, ancestor)

val File.nameOnly: String get() = FileUtil.getNameOnly(this)

val File.dotExtension: String get() = FileUtil.getDotExtension(this)

val File.pathSlash: String get() = FileUtil.pathSlash(this)

operator fun File.plus(name: String): File = FileUtil.plus(this, name)

