package org.woowatechcamp.githubapplication.data.auth

import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    // 실제 로그인
    @POST("/login/oauth/access_token")
    suspend fun getToken(
        // local properties 사용방법 찾으면 이 코드 변경하기
        @Query("client_id") clientId : String,
        @Query("client_secret") clientSecret : String,
        @Query("code") code : String
    ) : Response<AuthResponse>
}