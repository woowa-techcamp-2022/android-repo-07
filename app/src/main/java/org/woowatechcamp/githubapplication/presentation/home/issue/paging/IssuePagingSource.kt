package org.woowatechcamp.githubapplication.presentation.home.issue.paging

import org.woowatechcamp.githubapplication.data.issue.IssueUseCase
import org.woowatechcamp.githubapplication.presentation.home.issue.model.IssueModel
import org.woowatechcamp.githubapplication.util.OffsetPagingSource
import org.woowatechcamp.githubapplication.util.START_POSITION_INDEX

class IssuePagingSource(
    private val issueUseCase: IssueUseCase,
    private val state : String
) : OffsetPagingSource<IssueModel>() {

    override suspend fun load(
        params: LoadParams<Int>): LoadResult<Int, IssueModel> {
        val currentPosition = params.key ?: START_POSITION_INDEX

        val result = issueUseCase(state, currentPosition, 10)
            .getOrElse {
                return LoadResult.Error(it)
            }

        val nextPosition = if (result.isNotEmpty()) currentPosition + 1 else null
        val previousPosition =
            if (currentPosition != START_POSITION_INDEX) null else currentPosition - 1

        return runCatching {
            LoadResult.Page(
                data = result,
                prevKey = previousPosition,
                nextKey = nextPosition
            )
        }.getOrElse {
            LoadResult.Error(it)
        }

    }
}