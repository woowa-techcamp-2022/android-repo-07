package org.woowatechcamp.githubapplication.presentation.home.notifications.model

data class NotiModel(
    val id : String,
    val name : String,
    val fullName : String,
    val title : String,
    val timeDiff : String,
    val imgUrl : String,
    val num : String,
    val commentNum : Int,
    val url : String
)