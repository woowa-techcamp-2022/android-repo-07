package org.woowatechcamp.githubapplication.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.woowatechcamp.githubapplication.data.local.AuthPreferences
import java.io.IOException

class TokenInterceptor(
    private val preferences: AuthPreferences
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val accessToken: String? = preferences.accessToken
        builder.header("Accept", "application/json")
        if (accessToken != null) {
            builder.addHeader("Authorization", "token $accessToken")
        }
        return chain.proceed(builder.build())
    }
}