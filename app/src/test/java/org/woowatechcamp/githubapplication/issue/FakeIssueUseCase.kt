package org.woowatechcamp.githubapplication.issue

import org.woowatechcamp.githubapplication.domain.usecase.IssueUseCase
import org.woowatechcamp.githubapplication.domain.entity.IssueModel
import org.woowatechcamp.githubapplication.util.getOrError

class FakeIssueUseCase : IssueUseCase {

    private var issueList : List<IssueModel>? = null

    override suspend fun invoke(
        state: String,
        index: Int,
        pagingUnit: Int
    ): Result<List<IssueModel>> {
        return runCatching {
            issueList.getOrError("Test Null")
        }
    }

    fun addIssues(issues : List<IssueModel>) {
        issueList = issues
    }

}
