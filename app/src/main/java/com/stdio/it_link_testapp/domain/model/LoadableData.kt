package com.stdio.it_link_testapp.domain.model

sealed interface LoadableData<out R> : ImageData<R> {

    data class Success<out T>(val data: T, ) : LoadableData<T>

    data class Error(
        val exception: String,
        val code: Int = -1,
        val url: String
    ) : LoadableData<Nothing>

    data object Loading : LoadableData<Nothing>
}