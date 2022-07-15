package org.woowatechcamp.githubapplication.presentation.issue

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.woowatechcamp.githubapplication.data.issue.IssueRepository
import org.woowatechcamp.githubapplication.data.issue.model.IssueResponseItem
import javax.inject.Inject

@HiltViewModel
class IssueViewModel @Inject constructor(
    private val issueRepository: IssueRepository
) : ViewModel() {

    private val _issueList = MutableLiveData<List<IssueResponseItem>>()
    private val _errorMessage = MutableLiveData<String>()

    val errorMessage : LiveData<String> = _errorMessage
    val issueList : LiveData<List<IssueResponseItem>> = _issueList

    // 이슈 가져오기
    fun getIssues(state : String) = viewModelScope.launch {
        runCatching {
            issueRepository.getIssues(state)
        }.onSuccess { res ->
            if (res.body() == null) { _errorMessage.postValue("Issue 를 불러오는 데 오류가 발생했습니다.") }
            else { _issueList.postValue(res.body()!!.toList()) }

        }.onFailure { e ->
            _errorMessage.value = e.message
        }
    }

}