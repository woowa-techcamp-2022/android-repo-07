package org.woowatechcamp.githubapplication.data.user.comment

import com.google.gson.annotations.SerializedName

data class Reactions(
    @SerializedName("+1") val plus : Int,
    @SerializedName("-1") val minus : Int,
    val confused: Int,
    val eyes: Int,
    val heart: Int,
    val hooray: Int,
    val laugh: Int,
    val rocket: Int,
    val total_count: Int,
    val url: String
)