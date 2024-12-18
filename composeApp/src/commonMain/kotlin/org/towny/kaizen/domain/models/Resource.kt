package org.towny.kaizen.domain.models

sealed class Resource<T>(
    val data: T? = null,
) {
    class Loading<T> : Resource<T>()
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(data: T? = null, val message: String?) : Resource<T>(data)
}