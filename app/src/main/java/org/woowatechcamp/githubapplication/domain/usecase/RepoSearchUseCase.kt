package org.woowatechcamp.githubapplication.domain.usecase

import org.woowatechcamp.githubapplication.data.remote.service.SearchService
import org.woowatechcamp.githubapplication.domain.entity.SearchInfo
import org.woowatechcamp.githubapplication.util.JsonToColorUtil
import javax.inject.Inject

class RepoSearchUseCase @Inject constructor(
    private val searchService: SearchService,
    private val jsonToColorUtil: JsonToColorUtil
) {
    suspend operator fun invoke(
        q: String,
        index: Int,
        pagingUnit: Int = 10
    ): Result<List<SearchInfo>> =
        runCatching {
            searchService.getRepoSearch(q, "stars", "desc", pagingUnit, index).items.map {
                it.toEntity(
                    jsonToColorUtil.parseJson(it.language)
                )
            }
        }
}
