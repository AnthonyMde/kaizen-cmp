package org.towny.kaizen.domain.models

sealed class Resource<T>(
    val data: T? = null,
) {
    class Loading<T> : Resource<T>()
    class Success<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(val throwable: Throwable?) : Resource<T>()
}