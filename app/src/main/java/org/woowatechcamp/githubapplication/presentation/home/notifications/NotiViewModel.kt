package org.woowatechcamp.githubapplication.presentation.home.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.woowatechcamp.githubapplication.data.noti.NotiRepository
import org.woowatechcamp.githubapplication.data.noti.NotiUseCase
import org.woowatechcamp.githubapplication.presentation.home.notifications.model.NotiModel
import org.woowatechcamp.githubapplication.presentation.home.notifications.paging.NotiPagingAdapter
import org.woowatechcamp.githubapplication.presentation.home.notifications.paging.NotiPagingSource
import org.woowatechcamp.githubapplication.util.UiState
import org.woowatechcamp.githubapplication.util.ext.cancelWhenActive
import javax.inject.Inject

@HiltViewModel
class NotiViewModel @Inject constructor(
    private val notiRepository: NotiRepository,
    private val notiUseCase: NotiUseCase
) : ViewModel() {

    private var job: Job? = null

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

    private val _removeItemFlow = MutableStateFlow(mutableListOf<NotiModel>())
    private val removeItemFlow: Flow<MutableList<NotiModel>> get() = _removeItemFlow


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

    fun getNotiPaging(
        adapter: NotiPagingAdapter
    ) = viewModelScope.launch {
        Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 5 * 2
            ),
            pagingSourceFactory = { NotiPagingSource(notiUseCase) }
        ).flow
            .cachedIn(viewModelScope)
            .combine(removeItemFlow) { pagingData, removed ->
                pagingData.filter {
                    it !in removed
                }
            }
            .collectLatest {
                adapter.submitData(it)
            }
    }

    fun removeItem(item: NotiModel) {
        val list = mutableListOf(item)
        list.addAll(_removeItemFlow.value)
        _removeItemFlow.value = list
    }

    suspend fun getComments(noti: NotiModel) = viewModelScope.launch {
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