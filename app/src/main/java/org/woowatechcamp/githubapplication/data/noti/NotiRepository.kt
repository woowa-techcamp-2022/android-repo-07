package org.woowatechcamp.githubapplication.data.noti

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotiRepository @Inject constructor(
    private val service : NotiService
) {
    suspend fun getNoti() = service.getNoti()

    suspend fun markNoti(threadId : Long) = service.markNoti(threadId)

    suspend fun getComments(repo : String, name : String) = service.getComments(repo, name)
}