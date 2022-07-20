package org.woowatechcamp.githubapplication.presentation.home.issue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.woowatechcamp.githubapplication.data.issue.IssueRepository
import org.woowatechcamp.githubapplication.presentation.home.issue.model.IssueModel
import org.woowatechcamp.githubapplication.presentation.home.issue.paging.IssuePagingSource
import org.woowatechcamp.githubapplication.presentation.home.issue.paging.IssueUseCase
import org.woowatechcamp.githubapplication.util.UiState
import javax.inject.Inject

@HiltViewModel
class IssueViewModel @Inject constructor(
    private val issueRepository: IssueRepository,
    private val issueUseCase: IssueUseCase
) : ViewModel() {

    private val _issueState = MutableSharedFlow<UiState<List<IssueModel>>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val issueState: SharedFlow<UiState<List<IssueModel>>>
        get() = _issueState.asSharedFlow()

    // 이슈 가져오기
    fun getIssues(state: String) = viewModelScope.launch {
        _issueState.emit(
            issueRepository.getIssues(state)
        )
    }

    fun getIssuePaging(state: String) = Pager(
        config = PagingConfig(
            pageSize = 10,
            initialLoadSize = 5 * 2
        ),
        pagingSourceFactory = { IssuePagingSource(issueUseCase, state) }
    ).flow.cachedIn(viewModelScope)



}