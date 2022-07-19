package org.woowatechcamp.githubapplication.data.noti

import org.woowatechcamp.githubapplication.data.notifications.model.NotiMarkResponse
import org.woowatechcamp.githubapplication.data.user.comment.CommentResponse
import org.woowatechcamp.githubapplication.presentation.home.notifications.model.NotiModel
import org.woowatechcamp.githubapplication.util.ext.*
import retrofit2.Response
import javax.inject.Inject

class NotiRepository @Inject constructor(
    private val service: NotiService
) {

    suspend fun getNoti(): Result<List<NotiModel>> {
        return runCatching {
            val notiList = service.getNoti()
            notiList.map { noti ->
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
        }
    }

    suspend fun getComment(noti: NotiModel): Result<List<CommentResponse>> {
        return runCatching {
            service.getComments(noti.repo, noti.name)
        }
    }

    suspend fun markNoti(threadId: String): Result<Response<NotiMarkResponse>> {
        return runCatching {
            service.markNoti(threadId)
        }
    }
}