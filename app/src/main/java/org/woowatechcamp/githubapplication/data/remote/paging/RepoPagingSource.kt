package org.woowatechcamp.githubapplication.data.remote.paging

import android.util.Log
import org.woowatechcamp.githubapplication.domain.entity.SearchInfo
import org.woowatechcamp.githubapplication.domain.usecase.RepoSearchUseCase
import org.woowatechcamp.githubapplication.util.OffsetPagingSource
import org.woowatechcamp.githubapplication.util.START_POSITION_INDEX

class RepoPagingSource(
    private val repoSearchUseCase: RepoSearchUseCase,
    private val query: String
) : OffsetPagingSource<SearchInfo>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, SearchInfo> {

        val currentPosition = params.key ?: START_POSITION_INDEX

        val result = repoSearchUseCase(query, currentPosition, 10)
            .getOrElse {
                return LoadResult.Error(it)
            }

        val nextPosition = if (result.isNotEmpty()) currentPosition + 1 else null
        val previousPosition =
            if (currentPosition != START_POSITION_INDEX) currentPosition - 1 else null

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
