package org.woowatechcamp.githubapplication.data.auth

import org.woowatechcamp.githubapplication.util.AuthPreferences
import org.woowatechcamp.githubapplication.util.UiState
import javax.inject.Inject
import javax.inject.Singleton

class AuthRepository @Inject constructor(private val service: AuthService) {
    suspend fun getToken(
        clientId: String,
        clientSecrets: String,
        code: String
    ): UiState<String> {
        try {
            with(service.getToken(clientId, clientSecrets, code)) {
                return UiState.Success(accessToken)
            }
        } catch (e: Exception) {
            return UiState.Error(e.message ?: "로그인에 실패했습니다.")
        }
    }
}