package org.woowatechcamp.githubapplication.data.user.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Plan(
    val collaborators: Int,
    val name: String,
    @SerializedName("private_repos") val privateRepos: Int,
    val space: Int
) : Parcelable