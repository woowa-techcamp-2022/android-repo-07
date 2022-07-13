package org.woowatechcamp.githubapplication.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val issueRepository: IssueRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    private val _issueList = MutableLiveData<List<IssueResponseItem>>()
    private val _userProfile = MutableLiveData<Bitmap>()

    val errorMessage : LiveData<String> = _errorMessage
    val issueList : LiveData<List<IssueResponseItem>> = _issueList
    val userProfile : LiveData<Bitmap> = _userProfile

    // 사용자 정보 가져오기(프로필용)
    fun getUser() = viewModelScope.launch {
        try {
            val response = userRepository.getUser()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                val url = body.avatar_url
                withContext(Dispatchers.IO) {
                    val mInputStream = URL(url).openStream()
                    val mBitmap = BitmapFactory.decodeStream(mInputStream)
                    _userProfile.postValue(mBitmap)
                }
            } else {
                _errorMessage.postValue("사용자 정보를 가져오는 데 실패했습니다.")
            }
        } catch (e : Exception) {
            _errorMessage.postValue(e.message)
        }
    }

    // 이슈 가져오기
    fun getIssues(state : String) = viewModelScope.launch {
        try {
            val response = issueRepository.getIssues(state)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                _issueList.postValue(body.toList())
            } else {
                _errorMessage.postValue("오류가 발생했습니다.")
            }
        } catch (e : Exception) {
            _errorMessage.postValue(e.message)
        }
    }
}