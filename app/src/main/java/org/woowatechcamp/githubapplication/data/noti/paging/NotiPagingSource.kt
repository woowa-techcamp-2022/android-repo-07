package org.woowatechcamp.githubapplication.data.noti.paging

import org.woowatechcamp.githubapplication.domain.usecase.NotiUseCase
import org.woowatechcamp.githubapplication.domain.entity.NotiModel
import org.woowatechcamp.githubapplication.util.OffsetPagingSource
import org.woowatechcamp.githubapplication.util.START_POSITION_INDEX

class NotiPagingSource(
    private val notiUseCase: NotiUseCase
) : OffsetPagingSource<NotiModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotiModel> {
        val currentPosition = params.key ?: START_POSITION_INDEX

        val result = notiUseCase(currentPosition, 10)
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