package org.woowatechcamp.githubapplication.data.issue.model

import com.google.gson.annotations.SerializedName
import org.woowatechcamp.githubapplication.presentation.home.issue.model.IssueModel
import org.woowatechcamp.githubapplication.presentation.home.issue.model.IssueState
import org.woowatechcamp.githubapplication.util.ext.getDate
import org.woowatechcamp.githubapplication.util.ext.getIndexString
import org.woowatechcamp.githubapplication.util.ext.getTimeDiff

data class IssueResponse(
    @SerializedName("active_lock_reason") val activeLockReason: Any,
    val assignee: Assignee,
    val assignees: List<Assignee>,
    @SerializedName("author_association") val authorAssociation: String,
    val body: String,
    @SerializedName("closed_at") val closedAt: Any,
    val comments: Int,
    @SerializedName("comments_url") val commentsUrl: String,
    @SerializedName("created_at") val createdAt: String,
    val draft: Boolean,
    @SerializedName("events_url") val eventsUrl: String,
    @SerializedName("html_url") val htmlUrl: String,
    val id: Int,
    val labels: List<Label>,
    @SerializedName("labels_url") val labelsUrl: String,
    val locked: Boolean,
    val milestone: Any,
    @SerializedName("node_id") val nodeId: String,
    val number: Int,
    @SerializedName("performed_via_github_app") val performedViaGithubApp: Any,
    @SerializedName("pull_request") val pullRequest: PullRequest?,
    val reactions: Reactions,
    val repository: Repository,
    @SerializedName("repository_url") val repositoryUrl: String,
    val state: String,
    @SerializedName("state_reason") val stateReason: Any,
    @SerializedName("timeline_url") val timelineUrl: String,
    val title: String,
    @SerializedName("updated_at") val updatedAt: String,
    val url: String,
    val user: User
) {
    fun getIssueModel(): IssueModel {
        return IssueModel(
            id = id,
            state = IssueState.getIssueState(state),
            name = repository.name,
            fullName = repository.fullName,
            number = number.getIndexString(),
            title = title,
            timeDiff = updatedAt.getDate().getTimeDiff()
        )
    }
}