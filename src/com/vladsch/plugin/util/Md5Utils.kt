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

import com.intellij.openapi.vfs.VirtualFile
import java.security.DigestInputStream
import java.security.MessageDigest

class Md5Utils {
    companion object {
        @JvmStatic
        fun md5(virtualFile: VirtualFile): String {
            val md5 = MessageDigest.getInstance("MD5")

            val buffer = ByteArray(16384)
            val md5IS = DigestInputStream(virtualFile.inputStream, md5)
            while (md5IS.read(buffer) > 0);

            val sb = StringBuffer()
            for (byte in md5.digest()) {
                val toInt = (256 + byte.toInt()) and 255
                val hexString = Integer.toHexString(toInt)
                if (hexString.length < 2) sb.append("0")
                sb.append(hexString);
            }
            return sb.toString().toUpperCase()
        }

        @JvmStatic
        fun md5(text: String): String {
            val md5 = MessageDigest.getInstance("MD5")

            val buffer = text.toByteArray()
            md5.update(buffer)

            val sb = StringBuffer()
            for (byte in md5.digest()) {
                val toInt = (256 + byte.toInt()) and 255
                val hexString = Integer.toHexString(toInt)
                if (hexString.length < 2) sb.append("0")
                sb.append(hexString);
            }
            return sb.toString().toUpperCase()
        }
    }
}
