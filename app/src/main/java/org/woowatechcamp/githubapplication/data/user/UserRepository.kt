package org.woowatechcamp.githubapplication.data.user

import org.woowatechcamp.githubapplication.presentation.user.model.UserModel
import org.woowatechcamp.githubapplication.util.UiState
import org.woowatechcamp.githubapplication.util.getOrError
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val service: UserService
) {

    suspend fun getUser(): UiState<UserModel> {
        try {
            val user = service.getUser().getOrError("사용자 정보에 대한 응답을 받지 못했습니다.")
            val starred = service.getStarred(user.login).getOrError("Star 정보에 대한 응답을 받지 못했습니다.")
            with(user) {
                return UiState.Success(
                    refreshStarred(starred.size)
                )
            }
        } catch (e: Exception) {
            return UiState.Error(e.message ?: "사용자 정보를 가져오는 데 실패했습니다.")
        }
    }
}