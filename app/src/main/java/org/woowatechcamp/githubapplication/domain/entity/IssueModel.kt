package org.woowatechcamp.githubapplication.domain.entity

import org.woowatechcamp.githubapplication.presentation.home.issue.IssueState

data class IssueModel(
    val id: Int,
    val state: IssueState,
    val name: String,
    val fullName: String,
    val number: String,
    val title: String,
    val timeDiff: String
)