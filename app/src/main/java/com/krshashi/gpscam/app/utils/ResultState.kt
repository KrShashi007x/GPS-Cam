package com.krshashi.gpscam.app.utils

sealed class ResultState<out T> {
    data class Loading(val isLoading: Boolean = false) : ResultState<Nothing>()
    data class Success<T>(val data: T) : ResultState<T>()
    data class Failure(val message: String) : ResultState<Nothing>()
}