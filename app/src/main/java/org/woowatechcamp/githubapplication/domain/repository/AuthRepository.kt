package org.woowatechcamp.githubapplication.domain.repository

import org.woowatechcamp.githubapplication.util.UiState

interface AuthRepository {
    suspend fun getToken(
        clientId: String,
        clientSecrets: String,
        code: String
    ): UiState<String>
}