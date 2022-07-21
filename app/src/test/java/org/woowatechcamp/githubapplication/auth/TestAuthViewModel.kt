package org.woowatechcamp.githubapplication.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.woowatechcamp.githubapplication.data.auth.AuthRepository
import org.woowatechcamp.githubapplication.issue.FakeIssueRepository
import org.woowatechcamp.githubapplication.presentation.auth.SignInViewModel
import org.woowatechcamp.githubapplication.presentation.home.issue.IssueViewModel
import org.woowatechcamp.githubapplication.presentation.home.issue.model.IssueModel
import org.woowatechcamp.githubapplication.presentation.home.issue.model.IssueState
import org.woowatechcamp.githubapplication.rule.MainCoroutineRule
import org.woowatechcamp.githubapplication.util.UiState

@ExperimentalCoroutinesApi
class TestAuthViewModel {

    private lateinit var authViewModel : SignInViewModel
    private lateinit var authRepository : FakeAuthRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        authRepository = FakeAuthRepository()
        authViewModel = SignInViewModel(authRepository)
    }

    @Test
    fun test_auth_viewModel() = runTest {
        val code = "code"
        val token = "token"

        // empty test
        assertEquals(UiState.Empty, authViewModel.signInState.value)

        // null test
        authViewModel.getToken(code)
        assertEquals(UiState.Error("Test Null"), authViewModel.signInState.value)

        // value test
        authRepository.addToken(token)
        authViewModel.getToken(code)
        assertEquals(UiState.Success(token), authViewModel.signInState.value)

        // error test
        authRepository.setReturnError(true)
        authViewModel.getToken(code)
        assertEquals(UiState.Error("Test Error"), authViewModel.signInState.value)
    }
}