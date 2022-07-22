package org.woowatechcamp.githubapplication.data.noti

import org.woowatechcamp.githubapplication.presentation.home.notifications.model.NotiModel

interface NotiUseCase {
    suspend operator fun invoke(
        index: Int,
        pagingUnit: Int = 10
    ): Result<List<NotiModel>>
}