package org.woowatechcamp.githubapplication.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.woowatechcamp.githubapplication.data.issue.IssueRepository
import org.woowatechcamp.githubapplication.data.issue.model.IssueResponseItem
import org.woowatechcamp.githubapplication.data.user.UserRepository
import org.woowatechcamp.githubapplication.data.user.model.UserResponse
import retrofit2.http.Url
import java.io.InputStream
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    private val _userProfile = MutableLiveData<Bitmap>()
    private val _userInfo = MutableLiveData<UserResponse>()

    val errorMessage : LiveData<String> = _errorMessage
    val userProfile : LiveData<Bitmap> = _userProfile
    val userInfo : LiveData<UserResponse> = _userInfo
    // 사용자 정보 가져오기(프로필용)
    fun getUser() = viewModelScope.launch {
        runCatching {
            userRepository.getUser()
        }.onSuccess { res ->
            if (res == null) { _errorMessage.postValue("사용자 정보를 가져오는 데 실패했습니다.") }
            else {
                _userInfo.postValue(res)
                val url = res.avatar_url
                withContext(Dispatchers.IO) {
                    val mInputStream = URL(url).openStream()
                    val mBitmap = BitmapFactory.decodeStream(mInputStream)
                    _userProfile.postValue(mBitmap)
                }
            }
        }.onFailure { e ->
            _errorMessage.postValue(e.message)
        }
    }
}