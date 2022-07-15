package org.woowatechcamp.githubapplication.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun getTimeDiff(dateString : String) : String {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val itemDate = format.parse(dateString)
            val diff = Date().time - itemDate.time
            val diffHour = diff / 1000 / 60 / 60
            if (diffHour >= 24) {
                "${(diffHour/24).toInt()}일 전"
            } else {
                "${diffHour.toInt()}시간 전"
            }
        } catch (e : Exception) {
            "오류"
        }
    }
}