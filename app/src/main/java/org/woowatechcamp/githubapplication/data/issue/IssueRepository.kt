package org.woowatechcamp.githubapplication.data.issue

import org.woowatechcamp.githubapplication.presentation.home.issue.model.IssueModel
import org.woowatechcamp.githubapplication.util.UiState
import javax.inject.Inject

class IssueRepository @Inject constructor(
    private val service: IssueService
) {
    suspend fun getIssues(state: String): UiState<List<IssueModel>> {
        return try {
            UiState.Success(
                service.getIssues(state)
                    .filter { item -> item.pull_request == null }
                    .map { issue ->
                        issue.getIssueModel()
                    }
            )
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Issue 를 불러오는 데 실패했습니다.")
        }
    }
}