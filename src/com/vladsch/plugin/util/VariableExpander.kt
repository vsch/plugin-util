package com.vladsch.plugin.util

import java.util.regex.Pattern

class VariableExpander {
    companion object {
        val VARIABLE_REF: Pattern = Pattern.compile("\\$\\{([a-zA-Z_$][a-zA-Z_0-9$]+)}")
    }

    private val valueMap = HashMap<String, List<String>>()
    private val asMacroMap = HashSet<String>()

    fun resolve(value: CharSequence?): String {
        return Template.resolveRefs(value,VARIABLE_REF) { groups ->
            val name = groups[1]
            valueMap[name]?.joinToString() ?: if (asMacroMap.contains(name)) "\${$name}" else ""
        }
    }

    fun asMacro(name: String) {
        asMacroMap.add(name)
    }

    fun hasVariableRef(text: CharSequence?): Boolean {
        return VARIABLE_REF.matcher(text ?: "").find()
    }

    operator fun set(name: String, value: String?) {
        if (value == null) {
            valueMap.remove(name)
        } else {
            valueMap[name] = listOf(value)
        }
    }

    operator fun set(name: String, value: List<String>) {
        valueMap[name] = value
    }

    operator fun set(name: String, value: Array<String>) {
        valueMap[name] = value.toList()
    }

    operator fun get(name: String): List<String> = valueMap[name] ?: listOf()
    override fun toString(): String {
        return "VariableExpander(valueMap=${valueMap.entries.sortedBy { it.key }.joinToString { (key,value) -> "$key -> [ " + value.joinToString(", ") + " ]\n" }}" +
                "asMacroMap=[ ${asMacroMap.joinToString(", ") } ])"
    }
}

