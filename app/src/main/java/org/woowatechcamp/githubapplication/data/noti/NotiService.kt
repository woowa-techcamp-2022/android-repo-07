package org.woowatechcamp.githubapplication.data.noti

import org.woowatechcamp.githubapplication.data.noti.model.NotiResponse
import org.woowatechcamp.githubapplication.data.notifications.model.NotiMarkResponse
import org.woowatechcamp.githubapplication.data.noti.model.CommentResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface NotiService {
    // Notifications 가져오기
    @GET("/notifications")
    suspend fun getNoti(): List<NotiResponse>?

    // 특정 Notifications 제거
    @PATCH("/notifications/threads/{thread_id}")
    suspend fun markNoti(
        @Path("thread_id") threadId: String
    ): Response<NotiMarkResponse>?

    // Comment 개수 가져오기
    @GET("/repos/{repo}/{name}/comments")
    suspend fun getComments(
        @Path("repo") repo: String,
        @Path("name") name: String
    ): List<CommentResponse>?

    // Notifications paging
    @GET("/notifications")
    suspend fun getNotiPaging(
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): List<NotiResponse>
}