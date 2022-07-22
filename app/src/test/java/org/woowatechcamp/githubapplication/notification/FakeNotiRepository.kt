package org.woowatechcamp.githubapplication.notification

import org.woowatechcamp.githubapplication.domain.repository.NotiRepository
import org.woowatechcamp.githubapplication.domain.entity.NotiModel
import org.woowatechcamp.githubapplication.util.UiState
import org.woowatechcamp.githubapplication.util.getOrError

class FakeNotiRepository : NotiRepository {

    private var returnError = false
    private var statusCode : Int? = null

    private var notiList : List<NotiModel>? = null
    private var commentNoti : NotiModel? = null

    override suspend fun getNoti(): UiState<List<NotiModel>> {
        if (returnError) {
            return UiState.Error("Test Error")
        }
        return try {
            UiState.Success(
                notiList.getOrError("Test Null")
            )
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Test Exception")
        }
    }

    override suspend fun getComment(noti: NotiModel): UiState<NotiModel> {
        if (returnError) {
            return UiState.Error("Test Error")
        }
        return try {
            UiState.Success(
                commentNoti.getOrError("Test Null")
            )
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Test Exception")
        }
    }

    override suspend fun markNoti(threadId: String): UiState<String> {
        if (returnError) {
            return UiState.Error("Test Error")
        }
        return try {
            when (statusCode.getOrError("Test Null")) {
                205 -> {
                    UiState.Success("Success")
                }
                304 -> {
                    UiState.Error("Not Modified")
                }
                403 -> {
                    UiState.Error("Forbidden")
                }
                else -> {
                    UiState.Error("Status Error")
                }
            }
        } catch (e: Exception) {
            UiState.Error(e.message ?: "Test Exception")
        }
    }

    fun setStatus(code : Int) {
        statusCode = code
    }

    fun addCommentNoti(noti : NotiModel) {
        commentNoti = noti
    }

    fun addNoties(noties : List<NotiModel>) {
        notiList = noties
    }

    fun setReturnError(value : Boolean) {
        returnError = value
    }



}