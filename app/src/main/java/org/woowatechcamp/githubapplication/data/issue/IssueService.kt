package org.woowatechcamp.githubapplication.data.issue

import org.woowatechcamp.githubapplication.data.issue.model.IssueResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IssueService {
    @GET("/user/issues")
    suspend fun getIssues(
        @Query("state") state : String
    ) : Response<List<IssueResponse>>
}