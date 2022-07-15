package org.woowatechcamp.githubapplication.domain.entity

data class SearchInfo(
    val id: Int,
    val profileImg: String,
    val title: String,
    val owner: String,
    val description: String,
    val starCount: String,
    val language: String
)
