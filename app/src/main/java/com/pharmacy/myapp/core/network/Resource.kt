package com.pharmacy.myapp.core.network

import com.pharmacy.myapp.data.GeneralException

sealed class Resource<out T> {
    data class Error(val exception: GeneralException) : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Progress(val isLoading: Boolean = true) : Resource<Nothing>()
}