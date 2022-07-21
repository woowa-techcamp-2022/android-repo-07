package org.woowatechcamp.githubapplication.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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
    private val _profile = MutableSharedFlow<UiState<UserModel>>()

    val mainState: StateFlow<UiState<UserModel>>
        get() = _mainState.asStateFlow()
    val profile: SharedFlow<UiState<UserModel>>
        get() = _profile.asSharedFlow()

    fun getUser() = viewModelScope.launch {
        _mainState.value = userRepository.getUser()
    }

    fun getProfile() = viewModelScope.launch {
        _profile.emit(
            _mainState.value
        )
    }
}