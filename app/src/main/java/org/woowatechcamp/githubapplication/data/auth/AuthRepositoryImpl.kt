package org.woowatechcamp.githubapplication.data.auth

import org.woowatechcamp.githubapplication.util.AuthPreferences
import org.woowatechcamp.githubapplication.util.UiState
import org.woowatechcamp.githubapplication.util.getOrError
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val service: AuthService,
    private val preferences: AuthPreferences
) : AuthRepository {

    override suspend fun getToken(
        clientId: String,
        clientSecrets: String,
        code: String
    ): UiState<String> {
        try {
            with(
                service.getToken(clientId, clientSecrets, code)
                    .getOrError("로그인 응답을 받지 못했습니다.")
            ) {
                preferences.accessToken = accessToken
                return UiState.Success(accessToken)
            }
        } catch (e: Exception) {
            return UiState.Error(e.message ?: "로그인에 실패했습니다.")
        }
    }
}