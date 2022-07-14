package org.woowatechcamp.githubapplication.data.issue

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IssueRepository @Inject constructor(
    private val service : IssueService
) {
    suspend fun getIssues(state : String) = service.getIssues(state)
}