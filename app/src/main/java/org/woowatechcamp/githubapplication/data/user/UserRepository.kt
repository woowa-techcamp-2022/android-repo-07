package org.woowatechcamp.githubapplication.data.user

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val service: UserService
) {
    suspend fun getUser() = service.getUser()

    suspend fun getStarred(name: String) = service.getStarred(name)
}