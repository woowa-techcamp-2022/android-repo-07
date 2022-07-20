package org.woowatechcamp.githubapplication.presentation.home.issue.paging

import org.woowatechcamp.githubapplication.data.issue.IssueService
import org.woowatechcamp.githubapplication.presentation.home.issue.model.IssueModel
import javax.inject.Inject

class IssueUseCase @Inject constructor(
    private val service : IssueService
) {
    suspend operator fun invoke(
        state: String,
        index: Int,
        pagingUnit: Int = 10
    ) : Result<List<IssueModel>> =
        runCatching {
            service.getIssuePaging(state,"created", "desc", pagingUnit, index)
                .map {
                    it.getIssueModel()
                }
        }
}