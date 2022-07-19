package org.woowatechcamp.githubapplication.presentation.home

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
import org.woowatechcamp.githubapplication.data.user.UserRepository
import org.woowatechcamp.githubapplication.presentation.user.model.UserModel
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    private val _userProfile = MutableLiveData<Bitmap>()
    private val _userInfo = MutableLiveData<UserModel>()

    val errorMessage: LiveData<String> = _errorMessage
    val userProfile: LiveData<Bitmap> = _userProfile
    val userInfo: LiveData<UserModel> = _userInfo

    // 사용자 정보 가져오기(프로필용)
    fun getUser() = viewModelScope.launch {
        runCatching {
            userRepository.getUser()
        }.onSuccess { res ->
            if (res == null) {
                _errorMessage.postValue("사용자 정보를 가져오는 데 실패했습니다.")
            } else {
                // 여기서 프로필 사진 얻어서 캐시하기 -> 프로필 화면에 바로 전달할 수 있도록
                // 여기서 또 호출해야 하는가....
                val url = res.avatar_url
                withContext(Dispatchers.IO) {
                    val mInputStream = URL(url).openStream()
                    val mBitmap = BitmapFactory.decodeStream(mInputStream)
                    _userProfile.postValue(mBitmap)
                    runCatching {
                        userRepository.getStarred(res.login.orEmpty())
                    }.onSuccess { star ->
                        val starred = star.size
                        res.apply {
                            _userInfo.postValue(
                                UserModel(
                                    name = name.orEmpty(),
                                    nickname = login.orEmpty(),
                                    bio = bio.orEmpty(),
                                    location = location.orEmpty(),
                                    blog = blog.orEmpty(),
                                    email = email.orEmpty(),
                                    imgUrl = avatar_url.orEmpty(),
                                    followers = followers ?: 0,
                                    following = following ?: 0,
                                    repoNum = (public_repos ?: 0) + (total_private_repos ?: 0),
                                    starredNum = starred
                                )
                            )
                        }
                    }.onFailure {
                        _errorMessage.postValue("오류가 발생했습니다.")
                    }
                }
            }
        }.onFailure { e ->
            _errorMessage.postValue(e.message)
        }
    }
}