package org.woowatechcamp.githubapplication.presentation.home.notifications.model

data class NotiModel(
    val id : String,
    val name : String,
    val fullName : String,
    val title : String,
    val timeDiff : String,
    val imgUrl : String,
    val num : String,
    var commentNum : Int,
    val url : String,
    val timeDiffNum : Long,
    val repo : String,
    val threadId : String?
) {
    fun refreshComment(comments : Int) : NotiModel {
        return NotiModel(
            id, name, fullName, title, timeDiff, imgUrl,
            num, comments, url, timeDiffNum, repo, threadId
        )
    }
}