package org.towny.kaizen.data.remote.dto

import kotlinx.serialization.Serializable
import org.towny.kaizen.domain.models.Challenge
import org.towny.kaizen.domain.models.User

@Serializable
data class UserDTO(
    val id: String,
    val name: String,
    val isWasted: Boolean
) {
    companion object {
        fun from(user: User) = UserDTO(
            id = user.id,
            name = user.name,
            isWasted = user.isWasted
        )
    }

    fun toUser(challenges: List<Challenge> = emptyList()) = User(
        id = id,
        name = name,
        isWasted = isWasted,
        challenges = challenges
    )
}