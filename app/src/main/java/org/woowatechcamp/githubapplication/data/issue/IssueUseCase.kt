package org.woowatechcamp.githubapplication.data.issue

import org.woowatechcamp.githubapplication.data.issue.model.IssueResponse
import org.woowatechcamp.githubapplication.presentation.home.issue.model.IssueModel

interface IssueUseCase {
    suspend operator fun invoke(
        state: String, index: Int, pagingUnit: Int = 10
    ) : Result<List<IssueModel>>
}