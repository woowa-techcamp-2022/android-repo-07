package org.woowatechcamp.githubapplication.data.user.comment

data class CommentResposeItem(
    val author_association: String,
    val body: String,
    val commit_id: String,
    val created_at: String,
    val html_url: String,
    val id: Int,
    val line: Int,
    val node_id: String,
    val path: String,
    val position: Int,
    val reactions: Reactions,
    val updated_at: String,
    val url: String,
    val user: User
)