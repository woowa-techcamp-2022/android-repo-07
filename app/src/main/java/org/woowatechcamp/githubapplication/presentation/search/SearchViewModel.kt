package org.woowatechcamp.githubapplication.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.woowatechcamp.githubapplication.data.remote.paging.RepoPagingSource
import org.woowatechcamp.githubapplication.domain.entity.SearchInfo
import org.woowatechcamp.githubapplication.domain.usecase.RepoSearchUseCase
import org.woowatechcamp.githubapplication.util.debounce
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repoSearchUseCase: RepoSearchUseCase
) : ViewModel() {
    private val _searchUiState =
        MutableStateFlow<SearchUiState>(SearchUiState.Empty)
    val searchUiState: StateFlow<SearchUiState>
        get() = _searchUiState.asStateFlow()

    var keyword = ""

    // 페이징 수정 요망
    fun getRepoSearch(keyword: String) = Pager(
        config = PagingConfig(
            pageSize = 10,
            initialLoadSize = 5 * 2
        ),
        pagingSourceFactory = { RepoPagingSource(repoSearchUseCase, keyword) }
    ).flow.cachedIn(viewModelScope)

    private fun List<SearchInfo>.toUiState() = SearchUiState.Success(this)

    val textChangeAction = debounce<String>(150L, viewModelScope,
        block = {
            keyword = it
            if(keyword != "")
                getRepoSearch(keyword)
        }
    )

    fun setLoading() {
        _searchUiState.value = SearchUiState.Loading
    }

    fun setEmpty() {
        _searchUiState.value = SearchUiState.Empty
    }

    sealed class SearchUiState {
        object Empty : SearchUiState()
        object Loading : SearchUiState()
        data class Success(val data: List<SearchInfo>) : SearchUiState()
        class Error(val message: String?) : SearchUiState()
    }
}
