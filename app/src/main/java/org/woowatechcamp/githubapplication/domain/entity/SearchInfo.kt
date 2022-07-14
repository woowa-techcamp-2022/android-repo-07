package org.woowatechcamp.githubapplication.domain.entity

data class SearchInfo(
    // TODO : 서버에서 오는 id 값으로 교체 예정
    val id: String,
    val profileImg: String,
    val title: String,
    val owner: String,
    val description: String,
    val starCount: String,
    val language: String
)
