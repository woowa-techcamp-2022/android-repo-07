package org.woowatechcamp.githubapplication.presentation.home.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.woowatechcamp.githubapplication.data.noti.NotiRepository
import org.woowatechcamp.githubapplication.presentation.home.notifications.model.NotiModel
import org.woowatechcamp.githubapplication.util.UiState
import org.woowatechcamp.githubapplication.util.ext.cancelWhenActive
import javax.inject.Inject

@HiltViewModel
class NotiViewModel @Inject constructor(
    private val notiRepository: NotiRepository
) : ViewModel() {

    private var job : Job? = null

    private val _notiState = MutableSharedFlow<UiState<List<NotiModel>>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val _markState = MutableSharedFlow<UiState<String>>()
    private val _notiCommentState = MutableSharedFlow<UiState<NotiModel>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val notiState: SharedFlow<UiState<List<NotiModel>>>
        get() = _notiState.asSharedFlow()
    val markState: SharedFlow<UiState<String>>
        get() = _markState.asSharedFlow()
    val notiCommentState: SharedFlow<UiState<NotiModel>>
        get() = _notiCommentState.asSharedFlow()

    fun getNoti() {
        job.cancelWhenActive()
        job = viewModelScope.launch {
            _notiState.emit(UiState.Loading)
            with(notiRepository.getNoti()) {
                _notiState.emit(this)
                if (this is UiState.Success) {
                    this@with.value.forEach { noti ->
                        getComments(noti)
                    }
                }
            }
        }
    }

    suspend fun getComments(noti : NotiModel) {
        _notiCommentState.emit(
            notiRepository.getComment(noti)
        )
    }

    fun markNoti(threadId: String) = viewModelScope.launch {
        _markState.emit(
            notiRepository.markNoti(threadId)
        )
    }
}