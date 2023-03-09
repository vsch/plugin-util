package com.vladsch.plugin.util

data class RemoteContent(val urlKey: String, val url: String, val content: Any?, val timeStamp: Long, val error: String? = null, val fixUrl: String? = null) {
    fun withContent(content: Any?) = if (content == this.content) this else RemoteContent(urlKey, url, content, timeStamp, error, fixUrl)
    fun withError(error: String?, fixUrl: String?) = if (urlKey == this.urlKey && fixUrl == this.fixUrl) this else RemoteContent(urlKey, url, content, timeStamp, error, fixUrl)
}
