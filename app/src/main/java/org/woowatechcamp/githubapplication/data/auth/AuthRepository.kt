package org.woowatechcamp.githubapplication.data.auth

import org.woowatechcamp.githubapplication.util.UiState

interface AuthRepository {
    suspend fun getToken(
        clientId: String,
        clientSecrets: String,
        code: String
    ): UiState<String>
}