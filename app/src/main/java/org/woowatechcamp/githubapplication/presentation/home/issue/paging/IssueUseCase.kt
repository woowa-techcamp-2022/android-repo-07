package org.woowatechcamp.githubapplication.presentation.home.issue.paging

import org.woowatechcamp.githubapplication.data.issue.IssueService
import org.woowatechcamp.githubapplication.data.issue.IssueUseCase
import org.woowatechcamp.githubapplication.presentation.home.issue.model.IssueModel
import org.woowatechcamp.githubapplication.presentation.home.issue.model.IssueState
import javax.inject.Inject

class IssueUseCaseImpl @Inject constructor(
    private val service : IssueService
) : IssueUseCase {
    override suspend operator fun invoke(
        state: String,
        index: Int,
        pagingUnit: Int
    ) : Result<List<IssueModel>> =
        runCatching {
            service.getIssuePaging(state,"created", "desc", pagingUnit, index)
                .filter { issue -> issue.pull_request == null }
                .map {
                    it.getIssueModel()
                }
        }
}