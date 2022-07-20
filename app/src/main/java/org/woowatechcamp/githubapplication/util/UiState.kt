package org.woowatechcamp.githubapplication.util

sealed class UiState<out T : Any> {
    object Empty : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<out T : Any>(val value: T) : UiState<T>()
    data class Error(val msg: String) : UiState<Nothing>()
}

inline fun <reified T : Any> UiState<T>.onError(action: (String) -> Unit) {
    if (this is UiState.Error) {
        action(this.msg)
    }
}

inline fun <reified T : Any> UiState<T>.onSuccess(action: (T) -> Unit) {
    if (this is UiState.Success) {
        action(this.value)
    }
}

inline fun <reified T : Any> UiState<T>.onEmpty(action: (UiState<T>) -> Unit) {
    if (this is UiState.Empty) {
        action(this)
    }
}

inline fun <reified T : Any> UiState<T>.onLoading(action: (UiState<T>) -> Unit) {
    if (this is UiState.Loading) {
        action(this)
    }
}

fun <T> T?.getOrError(message : String) : T {
    if (this == null) throw Exception(message)
    return this
}