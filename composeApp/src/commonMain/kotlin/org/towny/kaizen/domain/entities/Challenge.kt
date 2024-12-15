package org.towny.kaizen.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Challenge(
    val id: String,
    val name: String,
    val isCompleted: Boolean,
    val failures: Int,
    val maxFailures: Int
) {
    fun isFailed(): Boolean = failures > maxFailures
}
