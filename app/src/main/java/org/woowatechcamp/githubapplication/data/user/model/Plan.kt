package org.woowatechcamp.githubapplication.data.user.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Plan(
    val collaborators: Int,
    val name: String,
    val private_repos: Int,
    val space: Int
) : Parcelable