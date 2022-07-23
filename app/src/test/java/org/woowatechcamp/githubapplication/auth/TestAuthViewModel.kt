package org.woowatechcamp.githubapplication.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.woowatechcamp.githubapplication.presentation.auth.SignInViewModel
import org.woowatechcamp.githubapplication.rule.MainCoroutineRule
import org.woowatechcamp.githubapplication.util.UiState

@ExperimentalCoroutinesApi
class TestAuthViewModel {

    private lateinit var authViewModel : SignInViewModel
    private lateinit var authRepository : FakeAuthRepository

    private val code = "code"
    private val token = "token"

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
    fun test_auth_empty() = runTest {
        assertEquals(UiState.Empty, authViewModel.signInState.value)
    }

    @Test
    fun test_auth_null() = runTest {
        authViewModel.getToken(code)
        assertEquals(UiState.Error("Test Null"), authViewModel.signInState.value)
    }

    @Test
    fun test_auth_value() = runTest {
        authRepository.addToken(token)
        authViewModel.getToken(code)
        assertEquals(UiState.Success(token), authViewModel.signInState.value)
    }

    @Test
    fun test_auth_error() = runTest {
        authRepository.setReturnError(true)
        authViewModel.getToken(code)
        assertEquals(UiState.Error("Test Error"), authViewModel.signInState.value)
    }
}