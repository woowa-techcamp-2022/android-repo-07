package org.woowatechcamp.githubapplication.data.user

import org.woowatechcamp.githubapplication.data.user.entity.UserResponse
import org.woowatechcamp.githubapplication.data.user.starred.StarredResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("/user")
    suspend fun getUser() : UserResponse
    @GET("/users/{name}/starred")
    suspend fun getStarred(
        @Path("name") name : String
    ) : List<StarredResponse>
}