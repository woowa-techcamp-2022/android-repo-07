package org.woowatechcamp.githubapplication.data.noti.model

import com.google.gson.annotations.SerializedName

data class Reactions(
    @SerializedName("+1") val plus: Int,
    @SerializedName("-1") val minus: Int,
    val confused: Int,
    val eyes: Int,
    val heart: Int,
    val hooray: Int,
    val laugh: Int,
    val rocket: Int,
    @SerializedName("total_count") val totalCount: Int,
    val url: String
)