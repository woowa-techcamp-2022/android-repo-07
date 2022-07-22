package org.woowatechcamp.githubapplication.notification

import org.woowatechcamp.githubapplication.domain.usecase.NotiUseCase
import org.woowatechcamp.githubapplication.domain.entity.NotiModel

class FakeNotiUseCase : NotiUseCase {
    override suspend fun invoke(index: Int, pagingUnit: Int): Result<List<NotiModel>> {
        return runCatching {
            listOf(NotiModel(
                id = "creative",
                name = "duck",
                fullName = "creativeduck",
                title = "Noti Test",
                timeDiff = "1일 전",
                imgUrl = "https://hello",
                num = "#0",
                commentNum = 0,
                url = "https://bye",
                timeDiffNum = 100L,
                repo = "/repo",
                threadId = "10101"
            ))
        }

    }
}