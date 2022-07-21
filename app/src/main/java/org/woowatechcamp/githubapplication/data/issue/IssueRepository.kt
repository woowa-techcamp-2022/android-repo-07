package org.woowatechcamp.githubapplication.data.issue

import org.woowatechcamp.githubapplication.presentation.home.issue.model.IssueModel
import org.woowatechcamp.githubapplication.util.UiState

interface IssueRepository {
    suspend fun getIssues(state: String): UiState<List<IssueModel>>
}