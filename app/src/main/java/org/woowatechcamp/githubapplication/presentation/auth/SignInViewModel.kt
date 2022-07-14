package org.woowatechcamp.githubapplication.presentation.auth

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.woowatechcamp.githubapplication.BuildConfig
import org.woowatechcamp.githubapplication.GithubApplication
import org.woowatechcamp.githubapplication.data.auth.AuthRepository
import org.woowatechcamp.githubapplication.presentation.MainActivity
import org.woowatechcamp.githubapplication.util.AuthPreferences
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val preferences: AuthPreferences
) : ViewModel() {

    private val _code = MutableLiveData<String>()
    private val _errorMessage = MutableLiveData<String>()
    private val _loginEvent = MutableSharedFlow<Boolean>()

    val code: LiveData<String> = _code
    val errorMessage: LiveData<String> = _errorMessage
    val loginEvent = _loginEvent.asSharedFlow()

    fun setCode(code: String) {
        _code.postValue(code)
    }

    fun getToken(code: String) = viewModelScope.launch {
        kotlin.runCatching {
            repository.getToken(
                BuildConfig.CLIENT_ID,
                BuildConfig.CLIENT_SECRETS,
                code
            )
        }.onSuccess { res ->
            val body = res.body()
            if (body == null) {
                _errorMessage.postValue("로그인을 하는 데 실패했습니다.")
            } else {
                preferences.accessToken = body.accessToken
//                    Log.d("GITHUB_AUTH", "$accessToken")
                _loginEvent.emit(true)
            }
        }.onFailure { e ->
            _errorMessage.postValue(e.message)
        }
    }
}