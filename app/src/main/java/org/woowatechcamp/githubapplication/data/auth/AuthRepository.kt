package org.woowatechcamp.githubapplication.data.auth

import org.woowatechcamp.githubapplication.util.AuthPreferences
import org.woowatechcamp.githubapplication.util.UiState
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val service: AuthService,
    private val preferences: AuthPreferences
) {

    suspend fun getToken(
        clientId: String,
        clientSecrets: String,
        code: String
    ): UiState<String> {
        try {
            with(service.getToken(clientId, clientSecrets, code)) {
                preferences.accessToken = accessToken
                return UiState.Success(accessToken)
            }
        } catch (e: Exception) {
            return UiState.Error(e.message ?: "로그인에 실패했습니다.")
        }
    }
}