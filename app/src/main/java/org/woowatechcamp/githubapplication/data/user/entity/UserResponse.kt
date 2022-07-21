package org.woowatechcamp.githubapplication.data.user.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import org.woowatechcamp.githubapplication.presentation.user.model.UserModel

@Parcelize
data class UserResponse(
    @SerializedName("avatar_url") val avatarUrl: String?,
    val bio: String?,
    val blog: String?,
    val collaborators: Int?,
    val company: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("disk_usage") val diskUsage: Int?,
    val email: String?,
    @SerializedName("events_url") val eventsUrl: String?,
    val followers: Int?,
    @SerializedName("followers_url") val followersUrl: String,
    val following: Int?,
    @SerializedName("following_url") val followingUrl: String,
    @SerializedName("gists_url") val gistsUrl: String,
    @SerializedName("gravatar_id") val gravatarId: String,
    val hireable: Boolean?,
    @SerializedName("html_url") val htmlUrl: String,
    val id: Int?,
    val location: String?,
    val login: String,
    val name: String?,
    @SerializedName("node_id") val nodeId: String?,
    @SerializedName("organizations_url") val organizationsUrl: String,
    @SerializedName("owned_private_repos") val ownedPrivateRepos: Int?,
    val plan: Plan?,
    @SerializedName("private_gists") val privateGists: Int?,
    @SerializedName("public_gists") val publicGists: Int?,
    @SerializedName("public_repos") val publicRepos: Int?,
    @SerializedName("received_events_url") val receivedEventsUrl: String,
    @SerializedName("repos_url") val reposUrl: String,
    @SerializedName("site_admin") val siteAdmin: Boolean,
    @SerializedName("starred_url") val starredUrl: String,
    @SerializedName("subscriptions_url") val subscriptionsUrl: String,
    @SerializedName("total_private_repos") val totalPrivateRepos: Int?,
    @SerializedName("twitter_username") val twitterUsername: String?,
    @SerializedName("two_factor_authentication") val twoFactorAuthentication: Boolean?,
    val type: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    val url: String?
) : Parcelable {
    fun refreshStarred(starredNum: Int): UserModel {
        return UserModel(
            name = name.orEmpty(),
            nickname = login,
            bio = bio.orEmpty(),
            location = location.orEmpty(),
            blog = blog.orEmpty(),
            email = email.orEmpty(),
            imgUrl = avatarUrl.orEmpty(),
            followInfo = "$followers Followers ・ $following Following",
            repoNum = (publicRepos ?: 0) + (totalPrivateRepos ?: 0),
            starredNum = starredNum
        )
    }
}