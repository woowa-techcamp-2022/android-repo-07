package org.woowatechcamp.githubapplication.data.noti

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.woowatechcamp.githubapplication.data.notifications.model.NotiMarkResponse
import org.woowatechcamp.githubapplication.presentation.home.notifications.model.NotiModel
import org.woowatechcamp.githubapplication.util.ext.*
import retrofit2.Response
import javax.inject.Inject

class NotiRepository @Inject constructor(
    private val service : NotiService
) {

    suspend fun getNoti() : Result<List<NotiModel>> {
        return runCatching {
            val resultNotiList = ArrayList<NotiModel>()
            withContext(Dispatchers.IO) {
                val notiList = service.getNoti()
                notiList.forEach { noti ->
                    launch {
                        val issue = service.getComments(
                            noti.repository.owner.login,
                            noti.repository.name)
                        resultNotiList.add(
                            NotiModel(
                                id = noti.id,
                                name = noti.repository.name,
                                fullName = noti.repository.full_name,
                                title = noti.subject.title,
                                timeDiff = noti.updated_at.getDate().getTimeDiff(),
                                imgUrl = noti.repository.owner.avatar_url,
                                num = noti.subject.url.getDeliNumber("issues/").getIndexString(),
                                commentNum = issue.size,
                                url = noti.url,
                                timeDiffNum = noti.updated_at.getDate().getTimeDiffNum()
                            )
                        )
                    }
                }
            }
            withContext(Dispatchers.Default) {
                resultNotiList.sortBy { it.timeDiffNum }
            }
            resultNotiList
        }
    }

    suspend fun markNoti(threadId : String) : Result<Response<NotiMarkResponse>>{
        return runCatching {
            service.markNoti(threadId)
        }
    }
}