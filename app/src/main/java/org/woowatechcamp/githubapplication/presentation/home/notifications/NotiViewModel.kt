package org.woowatechcamp.githubapplication.presentation.home.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.woowatechcamp.githubapplication.data.noti.NotiRepository
import org.woowatechcamp.githubapplication.presentation.home.notifications.model.NotiModel
import org.woowatechcamp.githubapplication.util.ext.getDate
import org.woowatechcamp.githubapplication.util.ext.getDeliNumber
import org.woowatechcamp.githubapplication.util.ext.getIndexString
import org.woowatechcamp.githubapplication.util.ext.getTimeDiff
import javax.inject.Inject

@HiltViewModel
class NotiViewModel @Inject constructor(
    private val notiRepository: NotiRepository
) : ViewModel() {

    private val _notiList = MutableLiveData<List<NotiModel>>()
    private val _markMessage = MutableLiveData<String>()

    val notiList : LiveData<List<NotiModel>> = _notiList
    val markMessage : LiveData<String> = _markMessage

    // noti 가져오기
    fun getNoti() = viewModelScope.launch {
        kotlin.runCatching {
            notiRepository.getNoti()
        }.onSuccess { res ->
            if (res.body() == null) { _markMessage.postValue("알림을 가져오는 데 오류가 발생했습니다.") }
            else {
                val list = res.body()!!.toList()
                val result = ArrayList<NotiModel>()
                for(i in list.indices) {
                    val item = list[i]
                    // TODO 이 부분 최적화하기
                    runCatching {
                        notiRepository.getComments(
                            item.repository.owner.login,
                            item.repository.name)
                    }.onSuccess { comment ->

                        result.add(
                            NotiModel(
                                id = item.id,
                                name = item.repository.name,
                                fullName = item.repository.full_name,
                                title = item.subject.title,
                                timeDiff = item.updated_at.getDate().getTimeDiff(),
                                imgUrl = item.repository.owner.avatar_url,
                                num = item.subject.url.getDeliNumber("issues/").getIndexString(),
                                commentNum = comment.size,
                                url = item.url
                            )
                        )
                    }.onFailure {

                    }
                }
                _notiList.postValue(result.toList())
            }
        }.onFailure { e ->
            _markMessage.postValue(e.message)
        }
    }
    // noti 읽음 표시하기 - thread 를 통해 개별적으로 처리
    fun markNoti(threadId : Long) = viewModelScope.launch {
        kotlin.runCatching {
            notiRepository.markNoti(threadId)
        }.onSuccess { res ->
            if (res.body() == null) { _markMessage.postValue("알림 읽음을 처리하는 데 오류가 발생했습니다.") }
            else {
                _markMessage.value = when (res.body()!!.status) {
                    205 -> { "Success" }
                    304 -> { "Not Modified" }
                    403 -> { "Forbidden" }
                    else -> { "알림 읽음을 처리하는 데 오류가 발생했습니다." }
                }
            }
        }.onFailure { e ->
            _markMessage.postValue(e.message)
        }
    }

}