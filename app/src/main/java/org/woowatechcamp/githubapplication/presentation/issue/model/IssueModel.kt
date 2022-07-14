package org.woowatechcamp.githubapplication.presentation.issue.model

data class IssueModel(
    val state : IssueState,
    val name : String,
    val fullName : String,
    val number : Int,
    val title : String,
    val timeDiff : String
)