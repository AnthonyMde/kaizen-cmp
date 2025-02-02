package com.makapp.kaizen.utils

object StringUtils {
    fun uppercaseEachWord(string: String): String = string
        .split(' ')
        .joinToString(separator = " ") { word ->
            word.replaceFirstChar { it.uppercase() }
        }
}
