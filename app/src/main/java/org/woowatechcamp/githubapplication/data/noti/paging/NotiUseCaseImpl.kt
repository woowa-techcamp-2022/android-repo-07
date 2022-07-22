package org.woowatechcamp.githubapplication.data.noti.paging

import org.woowatechcamp.githubapplication.data.noti.NotiService
import org.woowatechcamp.githubapplication.domain.usecase.NotiUseCase
import org.woowatechcamp.githubapplication.domain.entity.NotiModel
import javax.inject.Inject

class NotiUseCaseImpl @Inject constructor(
    private val notiService: NotiService
) : NotiUseCase {
    override suspend operator fun invoke(
        index: Int,
        pagingUnit: Int
    ): Result<List<NotiModel>> =
        runCatching {
            notiService.getNotiPaging(pagingUnit, index)
                .map { it.getNotiModel() }
        }
}