package org.woowatechcamp.githubapplication.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _code = MutableLiveData<String>()
    private val _errorMessage = MutableLiveData<String>()
    private val _accessSuccess = MutableSharedFlow<Boolean>()

    val code : LiveData<String> = _code
    val errorMessage : LiveData<String>  = _errorMessage
    val accessSuccess : SharedFlow<Boolean> = _accessSuccess.asSharedFlow()

    fun setCode(code: String) {
        _code.postValue(code)
    }

    fun getToken(code: String) = viewModelScope.launch {
        kotlin.runCatching {
            repository.getToken(
                BuildConfig.CLIENT_ID,
                BuildConfig.CLIENT_SECRETS,
                code)
        }.onSuccess { res ->
            if (res.body() == null) { _errorMessage.postValue("로그인에 실패했습니다.") }
            else {
                preferences.accessToken = res.body()!!.accessToken
                _accessSuccess.emit(true)
        }
        }.onFailure { e ->
            _errorMessage.postValue(e.message)
        }
    }
}