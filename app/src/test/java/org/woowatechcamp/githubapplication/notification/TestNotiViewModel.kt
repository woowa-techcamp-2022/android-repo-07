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

    private val threadId = "Hello"
    private val notiList = listOf(
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

    private val refreshNotiModel = NotiModel(
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
    fun test_noti_loading() = runTest {
        notiViewModel.notiState.test {
            notiViewModel.getNoti()
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Error("Test Null"), awaitItem())
        }
    }

    @Test
    fun test_noti_value() = runTest {
        notiViewModel.notiState.test {
            notiRepository.addNoties(notiList)
            notiViewModel.getNoti()
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success(notiList), awaitItem())
        }
    }

    @Test
    fun test_noti_erro() = runTest {
        notiViewModel.notiState.test {            // error test
            notiRepository.setReturnError(true)
            notiViewModel.getNoti()
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Error("Test Error"), awaitItem())
        }
    }

    @Test
    fun test_comment_null() = runTest {
        notiViewModel.notiCommentState.test {
            notiViewModel.getComments(refreshNotiModel)
            assertEquals(UiState.Error("Test Null"), awaitItem())
        }
    }

    @Test
    fun test_comment_value() = runTest {
        notiViewModel.notiCommentState.test {
            notiRepository.addCommentNoti(refreshNotiModel)
            notiViewModel.getComments(refreshNotiModel)
            assertEquals(UiState.Success(refreshNotiModel), awaitItem())
        }
    }

    @Test
    fun test_comment_error() = runTest {
        notiViewModel.notiCommentState.test {
            notiRepository.setReturnError(true)
            notiViewModel.getComments(refreshNotiModel)
            assertEquals(UiState.Error("Test Error"), awaitItem())
        }
    }

    @Test
    fun test_mark_null() = runTest {
        notiViewModel.markState.test {
            notiViewModel.markNoti(threadId)
            assertEquals(UiState.Error("Test Null"), awaitItem())
        }
    }

    @Test
    fun test_mark_value_205() = runTest {
        notiViewModel.markState.test {
            notiRepository.setStatus(205)
            notiViewModel.markNoti(threadId)
            assertEquals(UiState.Success("Success"), awaitItem())
        }
    }

    @Test
    fun test_mark_value_304() = runTest {
        notiViewModel.markState.test {
            notiRepository.setStatus(304)
            notiViewModel.markNoti(threadId)
            assertEquals(UiState.Error("Not Modified"), awaitItem())
        }
    }

    @Test
    fun test_mark_value_403() = runTest {
        notiViewModel.markState.test {
            notiRepository.setStatus(403)
            notiViewModel.markNoti(threadId)
            assertEquals(UiState.Error("Forbidden"), awaitItem())
        }
    }

    @Test
    fun test_mark_value_other_status() = runTest {
        notiViewModel.markState.test {
            notiRepository.setStatus(500)
            notiViewModel.markNoti(threadId)
            assertEquals(UiState.Error("Status Error"), awaitItem())
        }
    }

    @Test
    fun test_mark_error() = runTest {
        notiViewModel.markState.test {
            notiRepository.setReturnError(true)
            notiViewModel.markNoti(threadId)
            assertEquals(UiState.Error("Test Error"), awaitItem())
        }
    }
}