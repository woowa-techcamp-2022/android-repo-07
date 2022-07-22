package org.woowatechcamp.githubapplication.data.auth.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("access_token") val accessToken: String,
    val scope: String,
    @SerializedName("token_type") val tokenType: String
)
