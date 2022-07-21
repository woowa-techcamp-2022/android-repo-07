package org.woowatechcamp.githubapplication.data.noti

import org.woowatechcamp.githubapplication.presentation.home.notifications.model.NotiModel
import org.woowatechcamp.githubapplication.util.UiState

interface NotiRepository {

    suspend fun getNoti(): UiState<List<NotiModel>>

    suspend fun getComment(noti: NotiModel): UiState<NotiModel>

    suspend fun markNoti(threadId: String): UiState<String>

}