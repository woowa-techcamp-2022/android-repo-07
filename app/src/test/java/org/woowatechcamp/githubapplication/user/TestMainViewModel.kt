package org.woowatechcamp.githubapplication.user

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.woowatechcamp.githubapplication.presentation.home.MainViewModel
import org.woowatechcamp.githubapplication.domain.entity.UserModel
import org.woowatechcamp.githubapplication.rule.MainCoroutineRule
import org.woowatechcamp.githubapplication.util.UiState

@ExperimentalCoroutinesApi
class TestMainViewModel {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var userRepository : FakeMainRepository

    private val userData = UserModel(
        name = "Helllo",
        nickname = "hello",
        bio = "bio",
        location = "location",
        blog = "blog",
        email = "email",
        imgUrl = "imgUrl",
        followInfo = "follow",
        repoNum = 12,
        starredNum = 3
    )

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        userRepository = FakeMainRepository()
        mainViewModel = MainViewModel(userRepository)
    }

    @Test
    fun test_user_empty_value() = runTest {
        assertEquals(UiState.Empty, mainViewModel.mainState.value)
    }

    @Test
    fun test_profile_empty_value() = runTest {
        mainViewModel.profile.test {
            mainViewModel.getProfile()
            assertEquals(
                UiState.Empty,
                awaitItem()
            )
        }
    }

    @Test
    fun test_user_null_value() = runTest {
        mainViewModel.getUser()
        assertEquals(UiState.Error("Test Null"), mainViewModel.mainState.value)
    }

    @Test
    fun test_profile_null_value() = runTest {
        mainViewModel.getUser()
        mainViewModel.profile.test {
            mainViewModel.getProfile()
            assertEquals(
                UiState.Error("Test Null"),
                awaitItem()
            )
        }
    }

    @Test
    fun test_user_value_test() = runTest {
        userRepository.addUser(userData)
        mainViewModel.getUser()
        assertEquals(UiState.Success(userData), mainViewModel.mainState.value)
    }

    @Test
    fun test_profile_value_test() = runTest {
        userRepository.addUser(userData)
        mainViewModel.getUser()
        mainViewModel.profile.test {
            mainViewModel.getProfile()
            assertEquals(
                UiState.Success(userData),
                awaitItem()
            )
        }
    }

    @Test
    fun test_user_error_test() = runTest {
        userRepository.setReturnError(true)
        mainViewModel.getUser()
        assertEquals(UiState.Error("Test Error"), mainViewModel.mainState.value)
    }

    @Test
    fun test_profile_error_test() = runTest {
        userRepository.setReturnError(true)
        mainViewModel.getUser()
        mainViewModel.profile.test {
            mainViewModel.getProfile()
            assertEquals(
                UiState.Error("Test Error"),
                awaitItem()
            )
        }
    }
}