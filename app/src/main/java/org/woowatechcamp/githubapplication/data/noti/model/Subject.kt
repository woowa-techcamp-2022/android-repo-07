package org.woowatechcamp.githubapplication.data.notifications.model

data class Subject(
    val latest_comment_url: String,
    val title: String,
    val type: String,
    val url: String
)