package org.woowatechcamp.githubapplication.user

import org.woowatechcamp.githubapplication.domain.repository.UserRepository
import org.woowatechcamp.githubapplication.domain.entity.UserModel
import org.woowatechcamp.githubapplication.util.UiState
import org.woowatechcamp.githubapplication.util.getOrError

class FakeMainRepository : UserRepository {

    private var returnError = false

    private var userModel : UserModel? = null

    override suspend fun getUser(): UiState<UserModel> {
        if (returnError) {
            return UiState.Error("Test Error")
        }
        return try {
            UiState.Success(
                userModel.getOrError("Test Null"))
        } catch (e : Exception) {
            UiState.Error(e.message ?: "Test Exception")
        }
    }

    fun addUser(value : UserModel) {
        userModel = value
    }

    fun setReturnError(value : Boolean) {
        returnError = value
    }
}