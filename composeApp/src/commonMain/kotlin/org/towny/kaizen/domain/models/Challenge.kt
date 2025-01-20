package org.towny.kaizen.domain.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Challenge(
    val id: String,
    val name: String,
    val status: Status,
    val createdAt: LocalDate,
    val days:  Int,
    val isDoneForToday: Boolean,
    val failureCount: Int,
    val maxAuthorizedFailures: Int
) {
    fun isFailed(): Boolean = status == Status.FAILED

    @Serializable
    enum class Status {
        ON_GOING, PAUSED, DONE, FAILED
    }
}
