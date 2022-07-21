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
import org.woowatechcamp.githubapplication.presentation.user.model.UserModel
import org.woowatechcamp.githubapplication.rule.MainCoroutineRule
import org.woowatechcamp.githubapplication.util.UiState

@ExperimentalCoroutinesApi
class TestMainViewModel {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var userRepository : FakeMainRepository

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
    fun test_user_viewModel() = runTest {
        val userData = UserModel(
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
        // user empty test
        assertEquals(UiState.Empty, mainViewModel.mainState.value)

        // profile empty test
        mainViewModel.profile.test {
            mainViewModel.getProfile()
            assertEquals(
                UiState.Empty,
                awaitItem()
            )
        }

        // user null value test
        mainViewModel.getUser()
        assertEquals(UiState.Error("Test Null"), mainViewModel.mainState.value)

        // profile null value test
        mainViewModel.profile.test {
            mainViewModel.getProfile()
            assertEquals(
                UiState.Error("Test Null"),
                awaitItem()
            )
        }

        // user value test
        userRepository.addUser(userData)
        mainViewModel.getUser()
        assertEquals(UiState.Success(userData), mainViewModel.mainState.value)

        // profile value test
        mainViewModel.profile.test {
            mainViewModel.getProfile()
            assertEquals(
                UiState.Success(userData),
                awaitItem()
            )
        }

        // user error test
        userRepository.setReturnError(true)
        mainViewModel.getUser()
        assertEquals(UiState.Error("Test Error"), mainViewModel.mainState.value)

        // profile erro test
        mainViewModel.profile.test {
            mainViewModel.getProfile()
            assertEquals(
                UiState.Error("Test Error"),
                awaitItem()
            )
        }
    }
}