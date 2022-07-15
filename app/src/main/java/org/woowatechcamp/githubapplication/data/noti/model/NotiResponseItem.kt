package org.woowatechcamp.githubapplication.data.noti.model

import org.woowatechcamp.githubapplication.data.notifications.model.Repository
import org.woowatechcamp.githubapplication.data.notifications.model.Subject

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
)