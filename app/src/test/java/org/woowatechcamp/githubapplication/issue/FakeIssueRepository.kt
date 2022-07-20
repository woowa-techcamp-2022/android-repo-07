package org.woowatechcamp.githubapplication.issue

import org.woowatechcamp.githubapplication.data.issue.IssueRepository
import org.woowatechcamp.githubapplication.presentation.home.issue.model.IssueModel
import org.woowatechcamp.githubapplication.presentation.home.issue.model.IssueState
import org.woowatechcamp.githubapplication.presentation.user.model.UserModel
import org.woowatechcamp.githubapplication.util.UiState
import org.woowatechcamp.githubapplication.util.getOrError

class FakeIssueRepository : IssueRepository {

    private var returnError = false

    private var issueList : List<IssueModel>? = null

    override suspend fun getIssues(state: String): UiState<List<IssueModel>> {
        if (returnError) {
            return UiState.Error("Test Error")
        }
        return try {
            UiState.Success(
                issueList.getOrError("Test Null")
                    .filter { issue -> issue.state.state == state }
            )
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Test Exception")
        }
    }

    fun addIssues(issues : List<IssueModel>) {
        issueList = issues
    }

    fun setReturnError(value : Boolean) {
        returnError = value
    }



}