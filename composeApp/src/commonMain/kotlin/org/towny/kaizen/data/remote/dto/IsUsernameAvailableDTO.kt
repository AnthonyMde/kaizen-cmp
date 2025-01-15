package org.towny.kaizen.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class IsUsernameAvailableDTO(
    val isAvailable: Boolean
)
