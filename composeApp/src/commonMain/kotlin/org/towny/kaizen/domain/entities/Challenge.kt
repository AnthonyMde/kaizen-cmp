package org.towny.kaizen.domain.entities

data class Challenge(
    val id: String,
    val name: String,
    val isCompleted: Boolean,
    val failures: Int,
    val maxFailures: Int
) {
    fun isFailed(): Boolean = failures > maxFailures
}
