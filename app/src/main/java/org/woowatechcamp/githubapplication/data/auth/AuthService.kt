package org.woowatechcamp.githubapplication.data.auth

import org.woowatechcamp.githubapplication.data.auth.model.AuthResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @POST("/login/oauth/access_token")
    suspend fun getToken(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("code") code: String
    ): AuthResponse?
}