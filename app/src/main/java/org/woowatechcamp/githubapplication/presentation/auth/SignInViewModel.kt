package org.woowatechcamp.githubapplication.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.woowatechcamp.githubapplication.BuildConfig
import org.woowatechcamp.githubapplication.data.auth.AuthRepository
import org.woowatechcamp.githubapplication.util.AuthPreferences
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val preferences: AuthPreferences
) : ViewModel() {

    private val _signInState = MutableStateFlow<SignInState>(SignInState.Empty)

    val signInState : StateFlow<SignInState>
        get() = _signInState.asStateFlow()

    fun getToken(code: String) = viewModelScope.launch {
        runCatching {
            _signInState.value = SignInState.Loading
            repository.getToken(
                BuildConfig.CLIENT_ID,
                BuildConfig.CLIENT_SECRETS,
                code)
        }.onSuccess { res ->
            preferences.accessToken = res.accessToken
            _signInState.value = SignInState.Success(res.accessToken)
        }.onFailure { e ->
            _signInState.value = SignInState.Error(e.message ?: "오류 발생")
        }
    }
}

sealed class SignInState {
    object Empty : SignInState()
    object Loading : SignInState()
    data class Success(val token : String) : SignInState()
    class Error(val message : String) : SignInState()
}