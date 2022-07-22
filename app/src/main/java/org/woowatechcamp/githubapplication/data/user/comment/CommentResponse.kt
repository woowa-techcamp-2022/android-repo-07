package org.woowatechcamp.githubapplication.data.user.comment

import com.google.gson.annotations.SerializedName

data class CommentResponse(
    @SerializedName("author_association") val authorAssociation: String,
    val body: String,
    @SerializedName("commit_id") val commitId: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("html_url") val htmlUrl: String,
    val id: Int,
    val line: Int,
    @SerializedName("node_id") val nodeId: String,
    val path: String,
    val position: Int,
    val reactions: Reactions,
    @SerializedName("updated_at") val updatedAt: String,
    val url: String,
    val user: User
)