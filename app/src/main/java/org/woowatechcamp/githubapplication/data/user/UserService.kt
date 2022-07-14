package org.woowatechcamp.githubapplication.data.user

import org.woowatechcamp.githubapplication.data.user.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET

interface UserService {
    @GET("/user")
    suspend fun getUser() : Response<UserResponse>
}