package org.woowatechcamp.githubapplication.data.noti.model

import com.google.gson.annotations.SerializedName
import org.woowatechcamp.githubapplication.domain.entity.NotiModel
import org.woowatechcamp.githubapplication.util.ext.*

data class NotiResponse(
    val id: String,
    @SerializedName("last_read_at") val lastReadAt: String,
    val reason: String,
    val repository: Repository,
    val subject: Subject,
    @SerializedName("subscription_url") val subscriptionUrl: String,
    val unread: Boolean,
    @SerializedName("updated_at") val updatedAt: String,
    val url: String
) {
    fun getNotiModel(): NotiModel {
        return NotiModel(
            id = id,
            name = repository.name,
            fullName = repository.fullName,
            title = subject.title,
            timeDiff = updatedAt.getDate().getTimeDiff(),
            imgUrl = repository.owner.avatarUrl,
            num = subject.url.getDeliNumber("issues/").getIndexString(),
            url = url,
            timeDiffNum = updatedAt.getDate().getTimeDiffNum(),
            repo = repository.owner.login,
            threadId = url.getDeli("threads/")
        )
    }
}