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

data class RemoteContent(val urlKey: String, val url: String, val content: Any?, val timeStamp: Long, val error: String? = null, val fixUrl: String? = null) {
    fun withContent(content: Any?) = if (content == this.content) this else RemoteContent(urlKey, url, content, timeStamp, error, fixUrl)
    fun withError(error: String?, fixUrl: String?) = if (urlKey == this.urlKey && fixUrl == this.fixUrl) this else RemoteContent(urlKey, url, content, timeStamp, error, fixUrl)
}
