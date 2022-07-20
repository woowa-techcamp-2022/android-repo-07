package org.woowatechcamp.githubapplication.data.issue

import org.woowatechcamp.githubapplication.presentation.home.issue.model.IssueModel
import org.woowatechcamp.githubapplication.util.UiState
import org.woowatechcamp.githubapplication.util.getOrError
import javax.inject.Inject

class IssueRepository @Inject constructor(
    private val service: IssueService
) {
    suspend fun getIssues(state: String): UiState<List<IssueModel>> {
        return try {
            UiState.Success(
                service.getIssues(state).getOrError("Issue 정보에 대한 응답을 받지 못헀습니다.")
                    .filter { item -> item.pullRequest == null }
                    .map { issue ->
                        issue.getIssueModel()
                    }
            )
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Issue 를 불러오는 데 실패했습니다.")
        }
    }
}