package org.woowatechcamp.githubapplication.data.notifications.model

import org.woowatechcamp.githubapplication.presentation.home.notifications.model.NotiModel
import org.woowatechcamp.githubapplication.util.ext.getDate
import org.woowatechcamp.githubapplication.util.ext.getDeliNumber
import org.woowatechcamp.githubapplication.util.ext.getIndexString
import org.woowatechcamp.githubapplication.util.ext.getTimeDiff

data class NotiResponseItem(
    val id: String,
    val last_read_at: String,
    val reason: String,
    val repository: Repository,
    val subject: Subject,
    val subscription_url: String,
    val unread: Boolean,
    val updated_at: String,
    val url: String
) {
    fun getModel() = NotiModel(
        name = repository.name,
        fullName = repository.full_name,
        title = subject.title,
        timeDiff = updated_at.getDate().getTimeDiff(),
        imgUrl = repository.owner.avatar_url,
        num = subject.url.getDeliNumber("issues/").getIndexString()
    )
}