package com.makapp.kaizen.domain.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Challenge(
    val id: String,
    val name: String,
    val status: Status,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
    val days:  Int,
    val isDoneForToday: Boolean,
    val failureCount: Int,
    val maxAuthorizedFailures: Int
) {
    companion object {
        const val MAX_POSSIBLE_FAILURES = 12
    }
    fun isFailed(): Boolean = status == Status.FAILED
    fun isPaused(): Boolean = status == Status.PAUSED

    @Serializable
    enum class Status {
        ON_GOING,
        PAUSED,
        DONE,
        FAILED
    }
}
