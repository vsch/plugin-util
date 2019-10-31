/*
 *
 */
package com.vladsch.plugin.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.vladsch.flexmark.util.sequence.BasedSequence
import com.vladsch.flexmark.util.sequence.BasedSequenceImpl
import org.jdom.Element
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.function.Function

fun <T : Any> Any?.ifNotNull(eval: () -> T?): T? = if (this == null) null else eval()

fun <T : Any, R : Any> T?.ifNull(nullResult: R, elseEval: (R, T) -> R): R = if (this == null) nullResult else elseEval(nullResult, this)

fun <T : String?> T.nullIfEmpty(): T? = if (this != null && this.isNotEmpty()) this else null
fun <T : String?> T.nullIfBlank(): T? = if (this != null && !this.isBlank()) this else null

fun <T : Collection<*>?> T.nullIfEmpty(): T? = if (this != null && !this.isEmpty()) this else null
fun <T : Map<*, *>?> T.nullIfEmpty(): T? = if (this != null && !this.isEmpty()) this else null

fun <T : Any?> T.nullIf(nullIfValue: T): T? = if (this == null || this == nullIfValue) null else this
fun <T : Any?> T.nullIf(nullIfValue: Boolean): T? = if (this == null || nullIfValue) null else this
fun <T : Any> T?.nullIf(condition: (T) -> Boolean): T? = if (this == null || condition.invoke(this)) null else this

fun <T : Any, R : Any> T?.nullOr(transform: (T) -> R): R? = if (this == null) null else transform.invoke(this)

fun Boolean?.toInt(): Int = if (this != true) 0 else 1
fun <T : Any?> Boolean.ifElse(ifTrue: T, ifFalse: T): T = if (this) ifTrue else ifFalse
fun <T : Any?> Boolean.ifElse(ifTrue: () -> T, ifFalse: () -> T): T = if (this) ifTrue() else ifFalse()
fun <T : Any?> Boolean.ifElse(ifTrue: T, ifFalse: () -> T): T = if (this) ifTrue else ifFalse()
fun <T : Any?> Boolean.ifElse(ifTrue: () -> T, ifFalse: T): T = if (this) ifTrue() else ifFalse

fun <T : Any?> T?.isIn(vararg list: T): Boolean = this != null && this in list
fun <T : Any?> T?.ifNullOr(condition: Boolean, altValue: T): T = if (this == null || condition) altValue else this
fun <T : Any?> T?.ifNullOrNot(condition: Boolean, altValue: T): T = if (this == null || !condition) altValue else this
inline fun <T : Any?> T?.ifNullOr(condition: (T) -> Boolean, altValue: T): T = if (this == null || condition(this)) altValue else this
inline fun <T : Any?> T?.ifNullOrNot(condition: (T) -> Boolean, altValue: T): T = if (this == null || !condition(this)) altValue else this

fun String?.ifNullOrEmpty(altValue: String): String = if (this == null || this.isEmpty()) altValue else this
fun String?.ifNullOrBlank(altValue: String): String = if (this == null || this.isBlank()) altValue else this
fun String?.wrapWith(prefixSuffix: Char): String = wrapWith(prefixSuffix, prefixSuffix)
fun String?.wrapWith(prefix: Char, suffix: Char): String = if (this == null || this.isEmpty()) "" else prefix + this + suffix
fun String?.wrapWith(prefixSuffix: String): String = wrapWith(prefixSuffix, prefixSuffix)
fun String?.wrapWith(prefix: String, suffix: String): String = if (this == null || this.isEmpty()) "" else prefix + this + suffix
fun String?.suffixWith(suffix: Char): String = suffixWith(suffix, false)
fun String?.suffixWith(suffix: Char, ignoreCase: Boolean): String = if (this != null && !isEmpty() && !endsWith(suffix, ignoreCase)) plus(suffix) else orEmpty()
fun String?.suffixWith(suffix: String): String = suffixWith(suffix, false)
fun String?.suffixWith(suffix: String, ignoreCase: Boolean): String = if (this != null && !isEmpty() && suffix.isNotEmpty() && !endsWith(suffix, ignoreCase)) plus(suffix) else orEmpty()
fun String?.prefixWith(prefix: Char): String = prefixWith(prefix, false)
fun String?.prefixWith(prefix: Char, ignoreCase: Boolean): String = if (this != null && !isEmpty() && !startsWith(prefix, ignoreCase)) prefix.plus(this) else orEmpty()
fun String?.prefixWith(prefix: String): String = prefixWith(prefix, false)
fun String?.prefixWith(prefix: String, ignoreCase: Boolean): String = if (this != null && !isEmpty() && prefix.isNotEmpty() && !startsWith(prefix, ignoreCase)) prefix.plus(this) else orEmpty()
fun String?.endsWith(vararg needles: String): Boolean = endsWith(false, *needles)

fun String?.endsWith(ignoreCase: Boolean, vararg needles: String): Boolean {
    if (this == null) return false

    for (needle in needles) {
        if (endsWith(needle, ignoreCase)) {
            return true
        }
    }
    return false
}

fun String?.startsWith(vararg needles: String): Boolean = startsWith(false, *needles)

fun String?.startsWith(ignoreCase: Boolean, vararg needles: String): Boolean {
    if (this == null) return false

    for (needle in needles) {
        if (startsWith(needle, ignoreCase)) {
            return true
        }
    }
    return false
}

fun String?.count(char: Char, startIndex: Int = 0, endIndex: Int = Integer.MAX_VALUE): Int {
    if (this == null) return 0

    var count = 0
    var pos = startIndex
    val lastIndex = Math.min(length, endIndex)
    while (pos in 0 .. lastIndex) {
        pos = indexOf(char, pos)
        if (pos < 0) break
        count++
        pos++
    }
    return count
}

fun String?.count(char: String, startIndex: Int = 0, endIndex: Int = Integer.MAX_VALUE): Int {
    if (this == null) return 0

    var count = 0
    var pos = startIndex
    val lastIndex = Math.min(length, endIndex)
    while (pos in 0 .. lastIndex) {
        pos = indexOf(char, pos)
        if (pos < 0) break
        count++
        pos++
    }
    return count
}

fun String?.urlDecode(charSet: String? = null): String {
    return try {
        URLDecoder.decode(this, charSet ?: "UTF-8")
    } catch (e: UnsupportedEncodingException) {
        //e.printStackTrace()
        orEmpty()
    } catch (e: IllegalArgumentException) {
        //        e.printStackTrace()
        orEmpty()
    }
}

fun String?.urlEncode(charSet: String? = null): String {
    return try {
        URLEncoder.encode(this, charSet ?: "UTF-8")
    } catch (e: UnsupportedEncodingException) {
        //e.printStackTrace()
        orEmpty()
    }
}

fun String?.extractLeadingDigits(): Pair<Int?, String> {
    if (this == null) return Pair(null, "")

    val text = this
    var value: Int? = null
    var start = 0
    for (i in text.indices) {
        val c = text[i]
        if (!c.isDigit()) break
        val digit = c - '0'
        value = (value ?: 0) * 10 + digit
        start = i + 1
    }
    return Pair(value, text.substring(start))
}

fun String?.ifEmpty(arg: String): String = if (this != null && this.isNotEmpty()) this else arg

fun String?.ifEmpty(ifEmptyArg: String, ifNotEmptyArg: String): String = if (this == null || this.isEmpty()) ifEmptyArg else ifNotEmptyArg

fun String?.ifEmptyNullArgs(ifEmptyArg: String?, ifNotEmptyArg: String?): String? = if (this == null || this.isEmpty()) ifEmptyArg else ifNotEmptyArg

fun String?.ifEmpty(arg: () -> String): String = if (this != null && this.isNotEmpty()) this else arg()

fun String?.ifEmpty(ifEmptyArg: () -> String?, ifNotEmptyArg: () -> String?): String? = if (this == null || this.isEmpty()) ifEmptyArg() else ifNotEmptyArg()

fun String?.removeStart(prefix: Char): String = this?.removePrefix(prefix.toString()) ?: ""

fun <T> Collection<T>.stringSorted(stringer: Function<T, String>): List<T> = this.sortedBy { stringer.apply(it) }

fun String?.removeStart(prefix: String): String = this?.removePrefix(prefix) ?: ""

fun String?.removeEnd(prefix: Char): String = this?.removeSuffix(prefix.toString()) ?: ""

fun String?.removeEnd(prefix: String): String = this?.removeSuffix(prefix) ?: ""

fun String?.regexGroup(): String = "(?:" + this.orEmpty() + ")"

fun splicer(delimiter: String): (accum: String, elem: String) -> String = { accum, elem -> accum + delimiter + elem }

fun skipEmptySplicer(delimiter: String): (accum: String, elem: String) -> String = { accum, elem -> if (elem.isEmpty()) accum else accum + delimiter + elem }

fun StringBuilder.regionMatches(thisOffset: Int, other: String, otherOffset: Int, length: Int, ignoreCase: Boolean = false): Boolean {
    for (i in 0 until length) {
        if (!this[i + thisOffset].equals(other[i + otherOffset], ignoreCase)) return false
    }
    return true
}

fun StringBuilder.endsWith(suffix: String, ignoreCase: Boolean = false): Boolean =
    this.length >= suffix.length && this.regionMatches(this.length - suffix.length, suffix, 0, suffix.length, ignoreCase)

fun StringBuilder.startsWith(prefix: String, ignoreCase: Boolean = false): Boolean =
    this.length >= prefix.length && this.regionMatches(0, prefix, 0, prefix.length, ignoreCase)

fun Array<String>.splice(delimiter: String): String {
    val result = StringBuilder(this.size * (delimiter.length + 10))
    var first = true
    for (elem in this) {
        if (elem.isNotEmpty()) {
            if (!first && !elem.startsWith(delimiter) && !result.endsWith(delimiter)) result.append(delimiter)
            else first = false
            result.append(elem)
        }
    }

    return result.toString()
}

fun List<String?>.splice(delimiter: String, skipNullOrEmpty: Boolean = true): String {
    val result = StringBuilder(this.size * (delimiter.length + 10))
    var first = true
    for (elem in this) {
        if (elem != null && elem.isNotEmpty() || !skipNullOrEmpty) {
            if (!first && (!skipNullOrEmpty || !elem.startsWith(delimiter) && !result.endsWith(delimiter))) result.append(delimiter)
            else first = false
            result.append(elem.orEmpty())
        }
    }

    return result.toString()
}

fun Collection<String?>.splice(delimiter: String, skipNullOrEmpty: Boolean = true): String {
    val result = StringBuilder(this.size * (delimiter.length + 10))
    var first = true
    for (elem in this) {
        if (elem != null && elem.isNotEmpty() || !skipNullOrEmpty) {
            if (!first && (!skipNullOrEmpty || !elem.startsWith(delimiter) && !result.endsWith(delimiter))) result.append(delimiter)
            else first = false
            result.append(elem.orEmpty())
        }
    }

    return result.toString()
}

fun Iterator<String>.splice(delimiter: String, skipEmpty: Boolean = true): String {
    val result = StringBuilder(10 * (delimiter.length + 10))
    var first = true
    for (elem in this) {
        if (elem.isNotEmpty() || !skipEmpty) {
            if (!first && (!skipEmpty || !elem.startsWith(delimiter) && !result.endsWith(delimiter))) result.append(delimiter)
            else first = false
            result.append(elem)
        }
    }

    return result.toString()
}

fun String?.appendDelim(delimiter: String, vararg args: String): String = arrayListOf<String?>(this.orEmpty(), *args).splice(delimiter, true)

fun String.removeAnyPrefix(vararg prefixes: String): String {
    for (prefix in prefixes) {
        if (this.startsWith(prefix)) {
            return this.substring(prefix.length)
        }
    }
    return this
}

fun String.removePrefixIncluding(delimiter: String): String {
    val pos = this.indexOf(delimiter)
    if (pos != -1) {
        return this.substring(pos + delimiter.length)
    }
    return this
}

fun CharSequence.asBased(): BasedSequence = BasedSequenceImpl.of(this)

fun Int.indexOrNull(): Int? = if (this < 0) null else this

fun Int.indexOr(defaultValue: Int): Int = if (this < 0) defaultValue else this

fun CharSequence.contains(char: Char?): Boolean = char != null && this.indexOf(char) != -1

private val SPACES = " ".repeat(256)
fun StringBuilder.appendSpaces(count: Int): StringBuilder {
    if (count > 0) {

        val length = SPACES.length
        var i = count

        while (i > length) {
            append(SPACES.subSequence(0, i.maxLimit(length)))
            i -= length
        }

        if (i > 0) append(SPACES.subSequence(0, i))
    }
    return this
}

fun spaces(count: Int): String = SPACES.substring(0, count)

fun StringBuilder.append(c: Char, count: Int): StringBuilder {
    var i = count
    while (i > 0) {
        append(c)
        i--
    }
    return this
}

fun StringBuilder.append(c: CharSequence, count: Int): StringBuilder {
    var i = count
    while (i > 0) {
        append(c)
        i--
    }
    return this
}

operator fun <T : Any> StringBuilder.plusAssign(text: T) {
    this.append(text)
}

fun repeatChar(char: Char, count: Int): String {
    var result = ""
    for (i in 1 .. count) {
        result += char
    }

    return result
}

fun Int.max(vararg others: Int): Int {
    var max = this
    for (other in others) {
        if (max < other) max = other
    }
    return max
}

fun Int.min(vararg others: Int): Int {
    var min = this
    for (other in others) {
        if (min > other) min = other
    }
    return min
}

fun Double.max(vararg others: Double): Double {
    var max = this
    for (other in others) {
        if (max < other) max = other
    }
    return max
}

fun Double.min(vararg others: Double): Double {
    var min = this
    for (other in others) {
        if (min > other) min = other
    }
    return min
}

fun Float.max(vararg others: Float): Float {
    var max = this
    for (other in others) {
        if (max < other) max = other
    }
    return max
}

fun Float.min(vararg others: Float): Float {
    var min = this
    for (other in others) {
        if (min > other) min = other
    }
    return min
}

fun Int.minLimit(minBound: Int): Int = if (this < minBound) minBound else this

fun Int.maxLimit(maxBound: Int): Int = if (this > maxBound) maxBound else this

fun Int.rangeLimit(minBound: Int, maxBound: Int): Int = if (this < minBound) minBound else if (this > maxBound) maxBound else this

fun Long.minLimit(minBound: Long): Long = if (this < minBound) minBound else this

fun Long.maxLimit(maxBound: Long): Long = if (this > maxBound) maxBound else this

fun Long.rangeLimit(minBound: Long, maxBound: Long): Long = if (this < minBound) minBound else if (this > maxBound) maxBound else this

fun Float.minLimit(minBound: Float): Float = if (this < minBound) minBound else this

fun Float.maxLimit(maxBound: Float): Float = if (this > maxBound) maxBound else this

fun Float.rangeLimit(minBound: Float, maxBound: Float): Float = if (this < minBound) minBound else if (this > maxBound) maxBound else this

fun Double.minLimit(minBound: Double): Double = if (this < minBound) minBound else this

fun Double.maxLimit(maxBound: Double): Double = if (this > maxBound) maxBound else this

fun Double.rangeLimit(minBound: Double, maxBound: Double): Double =
    if (this < minBound) minBound else if (this > maxBound) maxBound else this

fun <T : Any> MutableList<T>.add(vararg items: T) {
    for (item in items) {
        this.add(item)
    }
}

inline fun com.intellij.openapi.diagnostic.Logger.debug(lazyMessage: () -> String) {
    if (this.isDebugEnabled) {
        val message = lazyMessage()
        this.debug(message)
        System.out.println(message)
    }
}

fun com.intellij.openapi.diagnostic.Logger.timeIt(lazyMessage: () -> String, runnable: () -> Unit) {
    if (this.isDebugEnabled) {
        TimeIt.logTime(this, lazyMessage(), runnable)
    } else {
        runnable()
    }
}

/**
 * clone a child of an element under a new name and optionally remove the cloned child
 *
 * @param oldName name of existing child
 * @param newName name of new child child
 * @param removeOld if true then old child is to be removed
 * @param replaceNew if true then existing child with newName is to be replaced
 */
fun Element.cloneChild(oldName: String, newName: String, removeOld: Boolean = true, replaceNew: Boolean = true): Element? {
    val element = this.getChild(oldName) ?: return null
    if (!replaceNew && this.getChild(newName) != null) {
        if (removeOld) this.removeChild(oldName)
        return element
    }

    val newElement = Element(newName)
    newElement.addContent(element.cloneContent())
    for (attr in element.attributes) {
        newElement.setAttribute(attr.name, attr.value)
    }
    if (removeOld) this.removeChild(oldName)
    this.removeChild(newName)
    this.addContent(newElement)
    return element
}

fun <T> List<T>.forEachReversed(action: (T) -> Unit) {
    var i = size
    while (i-- > 0) {
        action.invoke(get(i))
    }
}

fun Project.getProjectBaseDirectory(): VirtualFile? {
    val basePath = this.basePath
    if (basePath != null) {
        return LocalFileSystem.getInstance().findFileByPath(basePath)
    }
    return null
}
