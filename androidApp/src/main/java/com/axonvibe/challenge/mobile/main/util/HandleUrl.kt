package com.axonvibe.challenge.mobile.main.util

fun String.getCodeFromUrl(code : String) : String {
    val begin: Int = this.indexOf(code)
    if (begin == -1) return ""
    var end: Int = this.indexOf("&", begin)
    if (end == -1) end = this.length
    return this.substring(begin, end)
}