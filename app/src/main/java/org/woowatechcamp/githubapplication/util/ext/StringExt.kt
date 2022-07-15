package org.woowatechcamp.githubapplication.util.ext

import java.text.SimpleDateFormat
import java.util.*

fun String.getDate() : Date {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        format.parse(this)
    } catch (e : Exception) {
        return Date()
    }
}

fun String.getDeliNumber(deli : String) : Long {
    val urls = this.split(deli)
    return if (urls.size > 1) {
        urls[1].toLong()
    } else {
        0
    }
}