package org.woowatechcamp.githubapplication.domain.repository

import org.woowatechcamp.githubapplication.domain.entity.NotiModel
import org.woowatechcamp.githubapplication.util.UiState

interface NotiRepository {

    suspend fun getNoti(): UiState<List<NotiModel>>

    suspend fun getComment(noti: NotiModel): UiState<NotiModel>

    suspend fun markNoti(threadId: String): UiState<String>

}