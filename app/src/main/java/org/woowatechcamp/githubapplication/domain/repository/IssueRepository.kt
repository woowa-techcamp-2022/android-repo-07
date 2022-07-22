package org.woowatechcamp.githubapplication.domain.repository

import org.woowatechcamp.githubapplication.domain.entity.IssueModel
import org.woowatechcamp.githubapplication.util.UiState

interface IssueRepository {
    suspend fun getIssues(state: String): UiState<List<IssueModel>>
}