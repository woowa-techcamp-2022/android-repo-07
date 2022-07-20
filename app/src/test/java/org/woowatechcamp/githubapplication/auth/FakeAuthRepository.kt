package org.woowatechcamp.githubapplication.auth

import org.woowatechcamp.githubapplication.data.auth.AuthRepository
import org.woowatechcamp.githubapplication.data.issue.IssueRepository
import org.woowatechcamp.githubapplication.presentation.home.issue.model.IssueModel
import org.woowatechcamp.githubapplication.presentation.home.issue.model.IssueState
import org.woowatechcamp.githubapplication.presentation.user.model.UserModel
import org.woowatechcamp.githubapplication.util.UiState
import org.woowatechcamp.githubapplication.util.getOrError

class FakeAuthRepository : AuthRepository {

    private var returnError = false

    private var accessToken : String? = null

    override suspend fun getToken(
        clientId: String,
        clientSecrets: String,
        code: String
    ): UiState<String> {
        if (returnError) {
            return UiState.Error("Test Error")
        }
        return try {
            UiState.Success(
                accessToken.getOrError("Test Null")
            )
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Test Exception")
        }
    }

    fun addToken(token : String) {
        accessToken = token
    }

    fun setReturnError(value : Boolean) {
        returnError = value
    }



}