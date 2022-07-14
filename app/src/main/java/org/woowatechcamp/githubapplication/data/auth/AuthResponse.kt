package org.woowatechcamp.githubapplication.data.auth

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("access_token") val accessToken : String,
    @SerializedName("scope") val scope : String,
    @SerializedName("token_type") val tokenType : String
)
