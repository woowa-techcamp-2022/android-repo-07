package org.woowatechcamp.githubapplication.issue

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.woowatechcamp.githubapplication.presentation.home.issue.IssueViewModel
import org.woowatechcamp.githubapplication.domain.entity.IssueModel
import org.woowatechcamp.githubapplication.presentation.home.issue.IssueState
import org.woowatechcamp.githubapplication.rule.MainCoroutineRule
import org.woowatechcamp.githubapplication.util.UiState

@ExperimentalCoroutinesApi
class TestIssueViewModel {

    private lateinit var issueViewModel: IssueViewModel
    private lateinit var issueRepository : FakeIssueRepository
    private lateinit var issueUseCase: FakeIssueUseCase

    private var state = "open"

    private val issues = listOf(
        IssueModel(
            id = 0,
            state = IssueState.getIssueState("open"),
            name = "Issue",
            fullName = "Full Issue",
            number = "#1",
            title = "Issue Title",
            timeDiff = "2일 전"
        ),
        IssueModel(
            id = 0,
            state = IssueState.getIssueState("closed"),
            name = "Issue",
            fullName = "Full Issue",
            number = "#2",
            title = "Issue Title",
            timeDiff = "2일 전"
        ),
        IssueModel(
            id = 0,
            state = IssueState.getIssueState("open"),
            name = "Issue",
            fullName = "Full Issue",
            number = "#3",
            title = "Issue Title",
            timeDiff = "2일 전"
        ),
        IssueModel(
            id = 0,
            state = IssueState.getIssueState("closed"),
            name = "Issue",
            fullName = "Full Issue",
            number = "#4",
            title = "Issue Title",
            timeDiff = "2일 전"
        )
    )

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        issueRepository = FakeIssueRepository()
        issueUseCase = FakeIssueUseCase()
        issueViewModel = IssueViewModel(issueRepository, issueUseCase)
        issueUseCase.addIssues(issues)
    }

    @Test
    fun test_issue_null() = runTest {
        issueViewModel.issueState.test {
            issueViewModel.getIssues(state)
            assertEquals(UiState.Error("Test Null"), awaitItem())
        }
    }

    @Test
    fun test_issue_value_open() = runTest {
        issueViewModel.issueState.test {
            issueRepository.addIssues(issues)
            issueViewModel.getIssues(state)
            assertEquals(
                UiState.Success(issues.filter { issue -> issue.state.state == "open" }),
                awaitItem())
        }
    }

    @Test
    fun test_issue_value_closed() = runTest {
        issueViewModel.issueState.test {
            state = "closed"
            issueRepository.addIssues(issues)
            issueViewModel.getIssues(state)
            assertEquals(
                UiState.Success(issues.filter { issue -> issue.state.state == "closed" }),
                awaitItem())
        }
    }

    @Test
    fun test_issue_error() = runTest {
        issueViewModel.issueState.test {
            issueRepository.setReturnError(true)
            issueViewModel.getIssues(state)
            assertEquals(UiState.Error("Test Error"), awaitItem())
        }
    }
}