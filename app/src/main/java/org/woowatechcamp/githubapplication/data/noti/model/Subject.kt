package org.woowatechcamp.githubapplication.data.noti.model

import com.google.gson.annotations.SerializedName

data class Subject(
    @SerializedName("latest_comment_url") val latestCommentUrl: String,
    val title: String,
    val type: String,
    val url: String
)