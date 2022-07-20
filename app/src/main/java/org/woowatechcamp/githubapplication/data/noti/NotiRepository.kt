package org.woowatechcamp.githubapplication.data.noti

import org.woowatechcamp.githubapplication.data.notifications.model.NotiMarkResponse
import org.woowatechcamp.githubapplication.data.user.comment.CommentResponse
import org.woowatechcamp.githubapplication.presentation.home.notifications.model.NotiModel
import org.woowatechcamp.githubapplication.util.UiState
import org.woowatechcamp.githubapplication.util.ext.*
import javax.inject.Inject

class NotiRepository @Inject constructor(
    private val service: NotiService
) {

    suspend fun getNoti(): UiState<List<NotiModel>> {
        return try {
            UiState.Success(
                service.getNoti().map { noti ->
                    with(noti) {
                        NotiModel(
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
                }.sortedBy { noti -> noti.timeDiffNum }
            )
        } catch (e: Exception) {
            UiState.Error(e.message ?: "알림을 가져오는 데 실패했습니다.")
        }
    }

    suspend fun getComment(noti: NotiModel): UiState<NotiModel> {
        return try {
            UiState.Success(
                noti.refreshComment(
                    service.getComments(noti.repo, noti.name).size
                )
            )
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Comment 개수를 가져오는 데 실패했습니다.")
        }
    }

    suspend fun markNoti(threadId: String): UiState<String> {
        try {
            with(service.markNoti(threadId)) {
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