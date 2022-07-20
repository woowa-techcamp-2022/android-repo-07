package org.woowatechcamp.githubapplication.data.user

import org.woowatechcamp.githubapplication.presentation.user.model.UserModel
import org.woowatechcamp.githubapplication.util.UiState
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val service: UserService
) {
    suspend fun getUser() : UiState<UserModel> {
        try {
            val user = service.getUser()
            val starred = service.getStarred(user.login)
            with(user) {
                return UiState.Success(
                    user.refreshStarred(starred.size)
                )
            }
        } catch (e: Exception) {
            return UiState.Error(e.message ?: "사용자 정보를 가져오는 데 실패했습니다.")
        }
    }
}