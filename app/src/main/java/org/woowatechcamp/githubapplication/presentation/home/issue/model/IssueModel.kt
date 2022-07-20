package org.woowatechcamp.githubapplication.presentation.home.issue.model

data class IssueModel(
    val id: Int,
    val state: IssueState,
    val name: String,
    val fullName: String,
    val number: String,
    val title: String,
    val timeDiff: String
)