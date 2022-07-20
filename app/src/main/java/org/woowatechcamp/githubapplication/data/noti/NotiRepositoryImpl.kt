package org.woowatechcamp.githubapplication.data.noti

import org.woowatechcamp.githubapplication.presentation.home.notifications.model.NotiModel
import org.woowatechcamp.githubapplication.util.UiState
import org.woowatechcamp.githubapplication.util.getOrError
import javax.inject.Inject

class NotiRepositoryImpl @Inject constructor(
    private val service: NotiService
) : NotiRepository {

    override suspend fun getNoti(): UiState<List<NotiModel>> {
        return try {
            UiState.Success(
                service.getNoti().getOrError("알림 정보에 대한 응답을 받지 못했습니다.")
                    .map { noti ->
                        noti.getNotiModel()
                    }.sortedBy { noti -> noti.timeDiffNum }
            )
        } catch (e: Exception) {
            UiState.Error(e.message ?: "알림을 가져오는 데 실패했습니다.")
        }
    }

    override suspend fun getComment(noti: NotiModel): UiState<NotiModel> {
        return try {
            UiState.Success(
                noti.refreshComment(
                    service.getComments(noti.repo, noti.name)
                        .getOrError("Comment 정보에 대한 응답을 받지 못했습니다.")
                        .size
                )
            )
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Comment 개수를 가져오는 데 실패했습니다.")
        }
    }

    override suspend fun markNoti(threadId: String): UiState<String> {
        try {
            with(service.markNoti(threadId).getOrError("읽음 처리에 대한 응답을 받지 못했습니다.")) {
                return when (code()) {
                    205 -> {
                        UiState.Success("Success")
                    }
                    304 -> {
                        UiState.Error("Not Modified")
                    }
                    403 -> {
                        UiState.Error("Forbidden")
                    }
                    else -> {
                        UiState.Error("알림 읽음을 처리하는 데 오류가 발생했습니다.")
                    }
                }
            }
        } catch (e: Exception) {
            return UiState.Error(e.message ?: "알림 읽음을 처리하는 데 실패했습니다.")
        }
    }
}