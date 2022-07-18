package org.woowatechcamp.githubapplication.util.ext

import java.text.SimpleDateFormat
import java.util.*

fun String.getDate() : Date {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        format.parse(this)
    } catch (e : Exception) {
        return Date()
    }
}

fun String.getDeliNumber(deli : String) : Int {
    val urls = this.split(deli)
    return if (urls.size > 1) urls[1].toInt() else 0
}

fun String.getDeli(deli : String) : String? {
    val urls = this.split(deli)
    return if (urls.size > 1) urls[1] else null
}