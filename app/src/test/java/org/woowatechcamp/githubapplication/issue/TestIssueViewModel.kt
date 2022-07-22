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
    }

    @Test
    fun test_issue_viewModel() = runTest {

        var state = "open"

        val issues = listOf(
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

        issueUseCase.addIssues(issues)

        issueViewModel.issueState.test {
            // null test
            issueViewModel.getIssues(state)
            assertEquals(UiState.Error("Test Null"), awaitItem())

            // open value test
            issueRepository.addIssues(issues)
            issueViewModel.getIssues(state)
            assertEquals(
                UiState.Success(issues.filter { issue -> issue.state.state == "open" }),
                awaitItem())

            // closed value test
            state = "closed"
            issueRepository.addIssues(issues)
            issueViewModel.getIssues(state)
            assertEquals(
                UiState.Success(issues.filter { issue -> issue.state.state == "closed" }),
                awaitItem())

            // error test
            issueRepository.setReturnError(true)
            issueViewModel.getIssues(state)
            assertEquals(UiState.Error("Test Error"), awaitItem())
        }
    }
}