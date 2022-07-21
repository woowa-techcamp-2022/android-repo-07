package org.woowatechcamp.githubapplication.data.user

import org.woowatechcamp.githubapplication.presentation.user.model.UserModel
import org.woowatechcamp.githubapplication.util.UiState

interface UserRepository {
    suspend fun getUser(): UiState<UserModel>
}