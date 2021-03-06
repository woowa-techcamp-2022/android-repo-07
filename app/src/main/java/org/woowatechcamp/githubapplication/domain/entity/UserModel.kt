package org.woowatechcamp.githubapplication.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val name: String,
    val nickname: String,
    val bio: String,
    val location: String,
    val blog: String,
    val email: String,
    val imgUrl: String,
    val followInfo: String,
    val repoNum: Int,
    val starredNum: Int
) : Parcelable