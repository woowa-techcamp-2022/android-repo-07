package org.woowatechcamp.githubapplication.domain.usecase

import org.woowatechcamp.githubapplication.domain.entity.NotiModel

interface NotiUseCase {
    suspend operator fun invoke(
        index: Int,
        pagingUnit: Int = 10
    ): Result<List<NotiModel>>
}