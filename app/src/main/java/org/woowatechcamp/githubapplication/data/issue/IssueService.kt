package org.woowatechcamp.githubapplication.data.issue

import org.woowatechcamp.githubapplication.data.issue.model.IssueResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface IssueService {
    @GET("/user/issues")
    suspend fun getIssues(
        @Query("state") state: String
    ): List<IssueResponse>?

    @GET("/user/issues")
    suspend fun getIssuePaging(
        @Query("state") state: String,
        @Query("sort") sort: String,
        @Query("order") order: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ) : List<IssueResponse>
}