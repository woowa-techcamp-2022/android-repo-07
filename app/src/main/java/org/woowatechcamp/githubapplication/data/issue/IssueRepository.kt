package org.woowatechcamp.githubapplication.data.issue

import org.woowatechcamp.githubapplication.presentation.home.issue.model.IssueModel
import org.woowatechcamp.githubapplication.presentation.home.issue.model.IssueState
import org.woowatechcamp.githubapplication.util.UiState
import org.woowatechcamp.githubapplication.util.ext.getDate
import org.woowatechcamp.githubapplication.util.ext.getIndexString
import org.woowatechcamp.githubapplication.util.ext.getTimeDiff
import javax.inject.Inject

class IssueRepository @Inject constructor(
    private val service: IssueService
) {
    suspend fun getIssues(state: String): UiState<List<IssueModel>> {
        try {
            return UiState.Success(
                service.getIssues(state).map { issue ->
                    IssueModel(
                        id = issue.id,
                        state = IssueState.getIssueState(issue.state),
                        name = issue.repository.name,
                        fullName = issue.repository.full_name,
                        number = issue.number.getIndexString(),
                        title = issue.title,
                        timeDiff = issue.updated_at.getDate().getTimeDiff()
                    )
                }
            )
        } catch (e: Exception) {
            return UiState.Error(e.message ?: "Issue 를 불러오는 데 실패했습니다.")
        }
    }
}