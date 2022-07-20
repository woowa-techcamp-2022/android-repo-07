package org.woowatechcamp.githubapplication.data.noti.model

import org.woowatechcamp.githubapplication.data.notifications.model.Repository
import org.woowatechcamp.githubapplication.data.notifications.model.Subject
import org.woowatechcamp.githubapplication.presentation.home.notifications.model.NotiModel
import org.woowatechcamp.githubapplication.util.ext.*

data class NotiResponse(
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
    fun getNotiModel(): NotiModel {
        return NotiModel(
            id = id,
            name = repository.name,
            fullName = repository.full_name,
            title = subject.title,
            timeDiff = updated_at.getDate().getTimeDiff(),
            imgUrl = repository.owner.avatar_url,
            num = subject.url.getDeliNumber("issues/").getIndexString(),
            url = url,
            timeDiffNum = updated_at.getDate().getTimeDiffNum(),
            repo = repository.owner.login,
            threadId = url.getDeli("threads/")
        )
    }
}