package org.woowatechcamp.githubapplication.presentation.home.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.woowatechcamp.githubapplication.data.noti.NotiRepository
import org.woowatechcamp.githubapplication.presentation.home.notifications.model.NotiModel
import org.woowatechcamp.githubapplication.util.UiState
import javax.inject.Inject

@HiltViewModel
class NotiViewModel @Inject constructor(
    private val notiRepository: NotiRepository
) : ViewModel() {

    private val _notiState = MutableSharedFlow<UiState<List<NotiModel>>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val _markState = MutableSharedFlow<UiState<String>>()

    val notiState : SharedFlow<UiState<List<NotiModel>>>
        get() = _notiState.asSharedFlow()
    val markState : SharedFlow<UiState<String>>
        get() = _markState.asSharedFlow()

    // TODO 최적화하기
    fun getNoti() = viewModelScope.launch {
        _notiState.emit(UiState.Loading)
        notiRepository.getNoti()
            .onSuccess {
                _notiState.emit(UiState.Success(it))             }
            .onFailure { e->
                _notiState.emit(UiState.Error(e.message ?: "알림을 가져오는 데 실패했습니다.")) }
    }

    fun markNoti(threadId : String) = viewModelScope.launch {
        _markState.emit(UiState.Loading)
        notiRepository.markNoti(threadId)
            .onSuccess { res ->
                _markState.emit(
                    when (res.code()) {
                        205 -> { UiState.Success("Success") }
                        304 -> { UiState.Error("Not Modified") }
                        403 -> { UiState.Error("Forbidden") }
                        else -> { UiState.Error("알림 읽음을 처리하는 데 오류가 발생했습니다.") } }
                )
            }.onFailure { e ->
                _markState.emit(
                    UiState.Error(e.message ?: "알림 읽음을 처리하는 데 실패했습니다."))
            }
    }
}