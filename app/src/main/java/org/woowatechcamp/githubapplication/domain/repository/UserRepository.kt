package org.woowatechcamp.githubapplication.domain.repository

import org.woowatechcamp.githubapplication.domain.entity.UserModel
import org.woowatechcamp.githubapplication.util.UiState

interface UserRepository {
    suspend fun getUser(): UiState<UserModel>
}