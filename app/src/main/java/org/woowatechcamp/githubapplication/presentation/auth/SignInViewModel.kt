package org.woowatechcamp.githubapplication.presentation.auth

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.woowatechcamp.githubapplication.BuildConfig
import org.woowatechcamp.githubapplication.GithubApplication
import org.woowatechcamp.githubapplication.data.auth.AuthRepository
import org.woowatechcamp.githubapplication.presentation.MainActivity
import org.woowatechcamp.githubapplication.util.AuthPreferences
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository : AuthRepository,
    private val preferences : AuthPreferences
) : ViewModel() {

    private val _code = MutableLiveData<String>()
    private val _errorMessage = MutableLiveData<String>()
    val code : LiveData<String> = _code
    val errorMessage : LiveData<String>  = _errorMessage

    fun setCode(code : String) {
        _code.postValue(code)
    }

    fun getToken(code: String) = viewModelScope.launch {
        try {
            val response = repository.getToken(
                BuildConfig.CLIENT_ID,
                BuildConfig.CLIENT_SECRETS,
                code)
            val body = response.body()
            if (response.isSuccessful && body!= null) {
                body.apply {
                    preferences.accessToken = accessToken
//                    Log.d("GITHUB_AUTH", "$accessToken")
                    val intent = Intent(GithubApplication.application, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    GithubApplication.application.startActivity(intent)
                }
            }
            else {
                _errorMessage.postValue("오류가 발생하였습니다.")
            }
        } catch (e : Exception) {
            _errorMessage.postValue(e.message)
        }
    }
}