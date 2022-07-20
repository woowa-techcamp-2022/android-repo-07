package org.woowatechcamp.githubapplication.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.woowatechcamp.githubapplication.data.user.UserRepository
import org.woowatechcamp.githubapplication.presentation.user.model.UserModel
import org.woowatechcamp.githubapplication.util.UiState
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _mainState = MutableStateFlow<UiState<UserModel>>(UiState.Empty)

    val mainState: StateFlow<UiState<UserModel>>
        get() = _mainState

    fun getUser() = viewModelScope.launch {
        _mainState.value = userRepository.getUser()
    }
}