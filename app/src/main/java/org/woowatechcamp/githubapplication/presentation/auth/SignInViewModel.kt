package org.woowatechcamp.githubapplication.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.woowatechcamp.githubapplication.BuildConfig
import org.woowatechcamp.githubapplication.data.auth.AuthRepository
import org.woowatechcamp.githubapplication.util.AuthPreferences
import org.woowatechcamp.githubapplication.util.UiState
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val preferences: AuthPreferences
) : ViewModel() {

    private val _signInState = MutableStateFlow<UiState<String>>(UiState.Empty)

    val signInState : StateFlow<UiState<String>>
        get() = _signInState.asStateFlow()

    fun getToken(code: String) = viewModelScope.launch {
        _signInState.value = UiState.Loading
        repository.getToken(
            BuildConfig.CLIENT_ID,
            BuildConfig.CLIENT_SECRETS,
            code)
        .onSuccess { res ->
            preferences.accessToken = res.accessToken
            _signInState.value = UiState.Success(res.accessToken)
        }.onFailure { e ->
            _signInState.value = UiState.Error(e.message ?: "로그인에 실패했습니다.")
        }
    }
}