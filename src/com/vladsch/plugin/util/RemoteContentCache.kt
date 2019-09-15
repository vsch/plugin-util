package com.vladsch.plugin.util

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.net.HttpConfigurable
import org.jetbrains.ide.PooledThreadExecutor
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class RemoteContentCache(
    var maxFetches: Int,
    var remoteInvalidUntilFetched: Boolean,
    var remoteContentCacheExpiration: Int,
    var outputRemoteFetchExceptions: Boolean,
    private val onContentChange: (() -> Unit)?
) {

    private val remoteContentCache = ConcurrentHashMap<String, RemoteContent>()
    private var lastContentScan = Long.MIN_VALUE
    private var lastContentUpdate = Long.MIN_VALUE

    private val pendingLock = Object()
    private val pendingRemoteContentFetch = LinkedHashMap<String, RemoteContent>()
    private val fetchingRemoteContent = LinkedHashMap<String, Boolean>()
    private val fetchesInProgress = AtomicInteger(0)

    companion object {
        private val LOG = Logger.getInstance("com.vladsch.idea.multimarkdown.remote")
        const val MOVED_PERMANENTLY = "301: Moved Permanently"
        fun isRemoteContentUrl(url: String) = url.startsWith("http:") || url.startsWith("https:")
        fun cacheUrlKey(url: String) = url.removeSuffix("/") //if (url.startsWith("http:")) url.substring("http:".length) else url.substring("https:".length)
        fun removeUrlProtocol(url: String) = if (url.startsWith("http:")) url.substring("http:".length) else url.substring("https:".length)
    }

    fun clearCache() {
        val currentTimeMillis = System.currentTimeMillis()
        remoteContentCache.clear()
        pendingRemoteContentFetch.clear()
        lastContentScan = currentTimeMillis

        onContentChange?.invoke()
        lastContentUpdate = currentTimeMillis
    }

    fun fetchRemoteContent(url: String, cacheContent: Boolean): RemoteContent? {
        if (isRemoteContentUrl(url)) {
            val cachedContent = getCachedContent(url, cacheContent)
            if (cachedContent != null) {
                if (cachedContent.fixUrl != null && cachedContent.fixUrl != cachedContent.url) {
                    val oldRemoteContent = remoteContentCache[cachedContent.fixUrl]
                    if (oldRemoteContent != null && oldRemoteContent.fixUrl == url) {
                        // messed up, reporting moved to in a loop, ignore
                    } else {
                        return fetchRemoteContent(cachedContent.fixUrl, cacheContent)
                    }
                }
                return cachedContent
            }

            // schedule fetch for later
            val useCacheUrl = cacheUrlKey(url)
            synchronized(pendingLock) {
                val remoteContent = pendingRemoteContentFetch[useCacheUrl]
                if (remoteContent == null && !fetchingRemoteContent.containsKey(useCacheUrl) || cacheContent && remoteContent != null && remoteContent.content == null) {
                    pendingRemoteContentFetch[useCacheUrl] = RemoteContent(useCacheUrl, url, if (cacheContent) "" else null, System.currentTimeMillis())
                }
            }

            val fetches = fetchesInProgress.incrementAndGet()
            if (fetches <= maxFetches) {
                PooledThreadExecutor.INSTANCE.submit {
                    fetchPendingContent()
                    val pending = fetchesInProgress.decrementAndGet()
                    if (pending == 0) {
                        ApplicationManager.getApplication().invokeLater {
                            onContentChange?.invoke()
                            lastContentUpdate = System.currentTimeMillis()
                        }
                    }
                }
            } else {
                // too many, decrement
                fetchesInProgress.decrementAndGet()
            }

            // return empty so no error until actual content is loaded
            if (!remoteInvalidUntilFetched) return RemoteContent(useCacheUrl, url, "", System.currentTimeMillis())
        }
        return null
    }

    private fun fetchPendingContent() {
        while (true) {
            var entry: Map.Entry<String, RemoteContent>? = null
            synchronized(pendingLock) {
                if (!pendingRemoteContentFetch.isEmpty()) {
                    entry = pendingRemoteContentFetch.entries.first()
                    pendingRemoteContentFetch.remove(entry!!.key)
                }
            }

            if (entry == null) break

            getRemoteContent(entry!!.value.url, entry!!.value.content != null)
        }
    }

    // get cached content or null
    fun getCachedContent(url: String, cacheContent: Boolean, allowRedirect: Boolean = false): RemoteContent? {
        if (isRemoteContentUrl(url)) {
            val useCacheUrl = cacheUrlKey(url)

            val currentTimeMillis = System.currentTimeMillis()

            if (remoteContentCacheExpiration == 0) {
                // no caching
                remoteContentCache.clear()
            } else {
                if (lastContentScan == Long.MIN_VALUE) {
                    lastContentScan = currentTimeMillis
                } else if (lastContentScan + remoteContentCacheExpiration < currentTimeMillis) {
                    // remove old content
                    var toRemove: ArrayList<RemoteContent>? = null
                    for (cachedContent in remoteContentCache.values) {
                        if (cachedContent.timeStamp + remoteContentCacheExpiration >= currentTimeMillis) {
                            if (toRemove == null) toRemove = ArrayList<RemoteContent>()
                            toRemove.add(cachedContent)
                        }
                    }

                    toRemove?.forEach { it -> remoteContentCache.remove(it.urlKey) }

                    lastContentScan = currentTimeMillis
                }
            }

            val cachedContent = remoteContentCache[useCacheUrl]
            if (cachedContent != null) {
                if (cachedContent.timeStamp + remoteContentCacheExpiration < currentTimeMillis || cacheContent && /*!cachedContent.noContent &&*/ (cachedContent.content is CharSequence && cachedContent.content.isEmpty())) {
                    // either stale or did not cache content
                    remoteContentCache.remove(useCacheUrl)
                } else {
                    synchronized(pendingLock) {
                        pendingRemoteContentFetch.remove(useCacheUrl)
                    }

                    if (allowRedirect && cachedContent.fixUrl != null && cachedContent.fixUrl != cachedContent.url) {
                        val oldRemoteContent = remoteContentCache[cachedContent.fixUrl]
                        if (oldRemoteContent != null && oldRemoteContent.fixUrl == url) {
                            // messed up, reporting moved to in a loop, ignore
                        } else {
                            return getCachedContent(cachedContent.fixUrl, cacheContent, true)
                        }
                    }

                    return cachedContent
                }
            }
        }
        return null
    }

    // immediately get content such as for completion
    fun getRemoteContent(url: String, cacheContent: Boolean): RemoteContent? {
        if (isRemoteContentUrl(url)) {
            val remoteContent = getCachedContent(url, cacheContent)
            if (remoteContent != null) {
                if (remoteContent.error == MOVED_PERMANENTLY && remoteContent.fixUrl != null && remoteContent.fixUrl != url) {
                    // load the right URL
                    return getRemoteContent(remoteContent.fixUrl, cacheContent)
                }
                return remoteContent
            }

            val useCacheUrl = cacheUrlKey(url)

            synchronized(pendingLock) {
                fetchingRemoteContent[useCacheUrl] = true
            }

            val inputStream: InputStream?
            try {
                val httpConfigurable = ApplicationManager.getApplication().getComponent(HttpConfigurable::class.java)
                val urlConnection = if (httpConfigurable.USE_HTTP_PROXY) httpConfigurable.openConnection(url) else URL(url).openConnection()

                urlConnection.doOutput = false
                urlConnection.doInput = true
                //                urlConnection.setRequestProperty("Content-Type", "text/html;")
                urlConnection.connect()

                val httpConnection = urlConnection as? HttpURLConnection
                var movedTo: String? = null

                if (httpConnection != null && httpConnection.responseCode == 301) {
                    // moved permanently
                    movedTo = urlConnection.getHeaderField("Location").removeSuffix("/")

                    // TODO: ??? make it not expire naturally so we don't fetch it again unless cache cleared manually???
                    val oldRemoteContent = remoteContentCache[movedTo]
                    if (oldRemoteContent == null || oldRemoteContent.fixUrl != url) {
                        val newRemoteContent = RemoteContent(useCacheUrl, url, null, System.currentTimeMillis(), MOVED_PERMANENTLY, movedTo)
                        remoteContentCache[useCacheUrl] = newRemoteContent
                        if (url == movedTo) return newRemoteContent

                        // get the correct URL
                        val redirectedContent = getRemoteContent(movedTo, cacheContent)

                        if (redirectedContent?.fixUrl != null && redirectedContent.fixUrl == newRemoteContent.url) {
                            //  redirected to the original, let's reverse the two and leave it as is
                            if (removeUrlProtocol(redirectedContent.url) == removeUrlProtocol(newRemoteContent.url) && newRemoteContent.url.startsWith("https:")) {
                                // make non-https have error and https url not have error
                                remoteContentCache[redirectedContent.urlKey] = redirectedContent.withError(newRemoteContent.error, newRemoteContent.fixUrl)
                                remoteContentCache[newRemoteContent.urlKey] = newRemoteContent.withContent(redirectedContent.content).withError(null, null)
                                return newRemoteContent
                            } else {
                                remoteContentCache[redirectedContent.urlKey] = redirectedContent.withError(null, null)
                                remoteContentCache[newRemoteContent.urlKey] = newRemoteContent.withContent(redirectedContent.content)
                                return newRemoteContent
                            }
                        }
                        return redirectedContent
                        //} else {
                        // messed up, reporting moved to in a loop, ignore and just load it
                    }
                }

                try {
                    inputStream = urlConnection.getInputStream()

                    try {
                        val reader = BufferedReader(InputStreamReader(inputStream))
                        val sb = StringBuilder()

                        while (true) {
                            val line = reader.readLine() ?: break
                            sb.append(line).append('\n')
                        }

                        val content = sb.toString()
                        var newRemoteContent = RemoteContent(useCacheUrl, url, if (cacheContent) content else "", System.currentTimeMillis())
                        if (movedTo != null) {
                            // add error
                            newRemoteContent = newRemoteContent.withError(MOVED_PERMANENTLY, movedTo)
                        }

                        if (remoteContentCacheExpiration > 0) {
                            remoteContentCache[useCacheUrl] = newRemoteContent
                        }
                        return newRemoteContent
                    } catch (e: IOException) {
                        if (outputRemoteFetchExceptions) e.printStackTrace()
                        if (LOG.isDebugEnabled) LOG.debug(e)
                    } finally {
                        try {
                            inputStream.close()
                        } catch (e: IOException) {
                            if (outputRemoteFetchExceptions) e.printStackTrace()
                            if (LOG.isDebugEnabled) LOG.debug(e)
                        }
                    }
                } catch (e: IOException) {
                    // exists
                    if (outputRemoteFetchExceptions) e.printStackTrace()
                    if (LOG.isDebugEnabled) LOG.debug(e)
                    if (e !is FileNotFoundException) {
                        val newRemoteContent = RemoteContent(useCacheUrl, url, " ", System.currentTimeMillis())
                        if (remoteContentCacheExpiration > 0) {
                            remoteContentCache[useCacheUrl] = newRemoteContent
                        }
                        return newRemoteContent
                    }
                }
            } catch (e: Throwable) {
                if (outputRemoteFetchExceptions) e.printStackTrace()
                if (LOG.isDebugEnabled) LOG.debug(e)
            } finally {
                synchronized(pendingLock) {
                    fetchingRemoteContent.remove(useCacheUrl)
                }
            }

            // had error, mark it as such to prevent trying to load content
            if (remoteContentCacheExpiration > 0) {
                val newRemoteContent = RemoteContent(useCacheUrl, url, null, System.currentTimeMillis())
                remoteContentCache[useCacheUrl] = newRemoteContent
            }
        }
        return null
    }

    fun replaceCachedContent(url: String, content: Any) {
        val useCacheKey = cacheUrlKey(url)

        val cachedContent = remoteContentCache[useCacheKey]
        if (cachedContent != null) {
            remoteContentCache[useCacheKey] = RemoteContent(useCacheKey, url, content, cachedContent.timeStamp)
        }
    }

    fun getTotalUsed(): Long {
        var used = 0L
        for ((key, remoteContent) in remoteContentCache) {
            used += key.length
            if (remoteContent.content != null) {
                if (remoteContent.content is CharSequence) {
                    used += remoteContent.content.length
                } else {
                    // must be map of string->string, guestimate size from two strings
                    @Suppress("UNCHECKED_CAST")
                    (remoteContent.content as? Map<String, String>)?.forEach { entry ->
                        used += entry.key.length + entry.value.length
                    }
                }
            }
        }
        return used
    }

    fun getTotalLinks(): Int {
        return remoteContentCache.size
    }

    fun outputDetails(extra: String?) {
        if (outputRemoteFetchExceptions) {
            if (extra != null) {
                println(extra)
                if (LOG.isDebugEnabled) LOG.debug(extra)
            }

            for (entry in remoteContentCache) {
                val contentType: String
                var contentSize: Int = 0
                var totalSize: Int = entry.key.length
                val content = entry.value.content
                val message: String

                when (content) {
                    is String -> {
                        contentType = "String"
                        contentSize += content.length
                        message = String.format("    cachedKey: %s, contentType: %s, totalSize: %,d, contentSize: %,d", entry.key, contentType, totalSize + contentSize, contentSize)
                    }
                    else -> {
                        contentType = "Anchor List"
                        @Suppress("UNCHECKED_CAST")
                        val count = (content as? Map<String, String>)?.size ?: 0
                        @Suppress("UNCHECKED_CAST")
                        (content as? Map<String, String>)?.forEach { it ->
                            contentSize += it.key.length + it.value.length
                        }
                        message = String.format("    cachedKey: %s, contentType: %s, totalSize: %,d, count: %,d, contentSize: %,d", entry.key, contentType, totalSize + contentSize, count, contentSize)
                    }
                }

                println(message)
                if (LOG.isDebugEnabled) LOG.debug(message)
            }
        }
    }
}

