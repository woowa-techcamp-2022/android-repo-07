package org.woowatechcamp.githubapplication.domain.usecase

import org.woowatechcamp.githubapplication.domain.entity.IssueModel

interface IssueUseCase {
    suspend operator fun invoke(
        state: String, index: Int, pagingUnit: Int = 10
    ) : Result<List<IssueModel>>
}