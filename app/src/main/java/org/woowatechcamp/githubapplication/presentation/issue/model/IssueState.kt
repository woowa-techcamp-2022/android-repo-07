package org.woowatechcamp.githubapplication.presentation.issue.model

import org.woowatechcamp.githubapplication.R


enum class IssueState(
    val state: String,
    val icon: Int
) {
    OPEN("open", R.drawable.ic_issue_open_24),
    CLOSED("closed", R.drawable.ic_issue_closed_24),
    UNKNOWN("unknown", R.drawable.ic_issue_open_24);

    companion object {
        fun getIssueState(state: String?): IssueState =
            values().find { it.state == state } ?: UNKNOWN
    }
}