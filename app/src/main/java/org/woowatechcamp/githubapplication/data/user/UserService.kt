package org.woowatechcamp.githubapplication.data.user

import org.woowatechcamp.githubapplication.data.user.entity.UserResponse
import retrofit2.http.GET

interface UserService {
    @GET("/user")
    suspend fun getUser() : UserResponse
}