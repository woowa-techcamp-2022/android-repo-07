package org.woowatechcamp.githubapplication.util

sealed class UiState<out T : Any> {
    object Empty : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<out T : Any>(val value: T) : UiState<T>()
    data class Error(val msg: String) : UiState<Nothing>()
}