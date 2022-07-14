package org.woowatechcamp.githubapplication.data.auth

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val service : AuthService)  {
    suspend fun getToken(
        clientId : String,
        clientSecrets : String,
        code: String
    ) = service.getToken(clientId, clientSecrets, code)
}