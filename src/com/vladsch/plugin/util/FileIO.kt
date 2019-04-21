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

