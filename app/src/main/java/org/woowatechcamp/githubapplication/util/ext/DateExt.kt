package org.woowatechcamp.githubapplication.util.ext

import java.util.*

fun Date.getTimeDiff() : String {
    return try {
        val diff = Date().time - this.time
        val diffMin = diff / 1000 / 60
        val diffHour = diffMin / 60
        val diffDay = diffHour / 24
        val diffMonth = diffDay / 30

        if (diffHour < 1) {
            "${diffMin}분 전"
        } else if (diffHour < 24) {
            "${diffHour}시간 전"
        } else if (diffDay < 30) {
            "${diffDay}일 전"
        } else if (diffMonth < 12) {
            "${diffMonth}달 전"
        } else {
            "${diffMonth / 12}년 전"
        }
    } catch (e : Exception) {
        "오류"
    }
}