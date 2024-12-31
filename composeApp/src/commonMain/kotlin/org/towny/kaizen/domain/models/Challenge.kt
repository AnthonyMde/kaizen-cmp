package org.towny.kaizen.domain.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.towny.kaizen.utils.DateUtils

@Serializable
data class Challenge(
    val id: String,
    val name: String,
    val createdAt: LocalDate,
    val isCompleted: Boolean,
    val failures: Int,
    val maxFailures: Int
) {
    fun isFailed(): Boolean = failures > maxFailures
    val progressionInDays = DateUtils.getNumberOfDaysSince(createdAt)
}
