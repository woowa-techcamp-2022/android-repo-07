package org.woowatechcamp.githubapplication.data.user

import org.woowatechcamp.githubapplication.presentation.user.model.UserModel
import org.woowatechcamp.githubapplication.util.UiState
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val service: UserService
) {
    suspend fun getUser(): UiState<UserModel> {
        try {
            val user = service.getUser()
            val starred = service.getStarred(user.login)
            with(user) {
                return UiState.Success(
                    UserModel(
                        name = name.orEmpty(),
                        nickname = login,
                        bio = bio.orEmpty(),
                        location = location.orEmpty(),
                        blog = blog.orEmpty(),
                        email = email.orEmpty(),
                        imgUrl = avatar_url.orEmpty(),
                        followers = followers ?: 0,
                        following = following ?: 0,
                        repoNum = (public_repos ?: 0) + (total_private_repos ?: 0),
                        starredNum = starred.size
                    )
                )
            }
        } catch (e: Exception) {
            return UiState.Error(e.message ?: "사용자 정보를 가져오는 데 실패했습니다.")
        }
    }
}