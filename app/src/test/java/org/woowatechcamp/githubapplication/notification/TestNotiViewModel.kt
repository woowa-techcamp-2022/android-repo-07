package org.woowatechcamp.githubapplication.notification

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.woowatechcamp.githubapplication.presentation.home.notifications.NotiViewModel
import org.woowatechcamp.githubapplication.domain.entity.NotiModel
import org.woowatechcamp.githubapplication.rule.MainCoroutineRule
import org.woowatechcamp.githubapplication.util.UiState

@ExperimentalCoroutinesApi
class TestNotiViewModel {

    private lateinit var notiViewModel : NotiViewModel
    private lateinit var notiRepository: FakeNotiRepository
    private lateinit var notiUseCase : FakeNotiUseCase

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        notiRepository = FakeNotiRepository()
        notiUseCase = FakeNotiUseCase()

        notiViewModel = NotiViewModel(notiRepository, notiUseCase)
    }

    @Test
    fun test_noti_viewModel() = runTest {

        val threadId = "Hello"
        val notiList = listOf(
            NotiModel(
                id = "creative",
                name = "duck",
                fullName = "creativeduck",
                title = "Noti Test",
                timeDiff = "1일 전",
                imgUrl = "https://hello",
                num = "#0",
                commentNum = 0,
                url = "https://bye",
                timeDiffNum = 100L,
                repo = "/repo",
                threadId = "10101"
            ),
            NotiModel(
                id = "creative",
                name = "duck",
                fullName = "creativeduck",
                title = "Noti Test",
                timeDiff = "1일 전",
                imgUrl = "https://hello",
                num = "#0",
                commentNum = 1,
                url = "https://bye",
                timeDiffNum = 100L,
                repo = "/repo",
                threadId = "10101"
            )
        )

        val refreshNotiModel = NotiModel(
            id = "creative",
            name = "duck",
            fullName = "creativeduck",
            title = "Noti Test",
            timeDiff = "1일 전",
            imgUrl = "https://hello",
            num = "#0",
            commentNum = 3,
            url = "https://bye",
            timeDiffNum = 100L,
            repo = "/repo",
            threadId = "10101"
        )

        // notifications test
        notiViewModel.notiState.test {

            // loading test
            notiViewModel.getNoti()
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Error("Test Null"), awaitItem())

            // value test
            notiRepository.addNoties(notiList)
            notiViewModel.getNoti()
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success(notiList), awaitItem())

            // error test
            notiRepository.setReturnError(true)
            notiViewModel.getNoti()
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Error("Test Error"), awaitItem())
        }
        notiRepository.setReturnError(false)

        // comment test
        notiViewModel.notiCommentState.test {
            // null test
            assertEquals(UiState.Error("Test Null"), awaitItem())

            // value test
            notiRepository.addCommentNoti(refreshNotiModel)
            notiViewModel.getComments(refreshNotiModel)
            assertEquals(UiState.Success(refreshNotiModel), awaitItem())

            // error test
            notiRepository.setReturnError(true)
            notiViewModel.getComments(refreshNotiModel)
            assertEquals(UiState.Error("Test Error"), awaitItem())
        }
        notiRepository.setReturnError(false)

        // mark test
        notiViewModel.markState.test {
            // null test
            notiViewModel.markNoti(threadId)
            assertEquals(UiState.Error("Test Null"), awaitItem())

            // value 205 test
            notiRepository.setStatus(205)
            notiViewModel.markNoti(threadId)
            assertEquals(UiState.Success("Success"), awaitItem())

            // value 304 test
            notiRepository.setStatus(304)
            notiViewModel.markNoti(threadId)
            assertEquals(UiState.Error("Not Modified"), awaitItem())

            // value 403 test
            notiRepository.setStatus(403)
            notiViewModel.markNoti(threadId)
            assertEquals(UiState.Error("Forbidden"), awaitItem())

            // value else test
            notiRepository.setStatus(500)
            notiViewModel.markNoti(threadId)
            assertEquals(UiState.Error("Status Error"), awaitItem())

            // error test
            notiRepository.setReturnError(true)
            notiViewModel.markNoti(threadId)
            assertEquals(UiState.Error("Test Error"), awaitItem())
        }

    }
}