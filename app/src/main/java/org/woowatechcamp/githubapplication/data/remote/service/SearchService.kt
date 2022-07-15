package org.woowatechcamp.githubapplication.data.remote.service

import org.woowatechcamp.githubapplication.data.remote.dto.ResponseRepoSearchDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    // GET Search Repo
    @GET("search/repositories")
    suspend fun getRepoSearch(
        @Query("q") q: String,
        @Query("sort") sort: String,
        @Query("order") order: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): ResponseRepoSearchDTO
}
