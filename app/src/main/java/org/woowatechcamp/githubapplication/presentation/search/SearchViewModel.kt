package org.woowatechcamp.githubapplication.presentation.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.woowatechcamp.githubapplication.domain.entity.SearchInfo
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {
    private val _searchUiState =
        MutableStateFlow<SearchUiState>(SearchUiState.Empty)
    val searchUiState: StateFlow<SearchUiState>
        get() = _searchUiState.asStateFlow()

    private fun List<SearchInfo>.toUiState() = SearchUiState.Success(this)

    sealed class SearchUiState {
        object Empty : SearchUiState()
        object Loading : SearchUiState()
        data class Success(val data: List<SearchInfo>) : SearchUiState()
        class Error(val message: String?) : SearchUiState()
    }
}
