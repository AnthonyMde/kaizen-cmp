package com.makapp.kaizen.domain

import com.makapp.kaizen.domain.models.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

object FlowHelper {
    fun <T, R> Flow<Resource<T>>.mapSuccess(block: (T?) -> R): Flow<Resource<R>> {
        return this.map {
            when (it) {
                is Resource.Error -> {
                    Resource.Error(it.throwable)
                }
                is Resource.Loading -> {
                    Resource.Loading()
                }
                is Resource.Success -> {
                    Resource.Success(block(it.data))
                }
            }
        }
    }

    fun <T> Flow<T>.stateIn(
        scope: CoroutineScope,
        initialValue: T
    ): StateFlow<T> {
        return this.stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = initialValue
        )
    }
}