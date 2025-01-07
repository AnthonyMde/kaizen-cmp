package org.towny.kaizen.data.remote.dto

import kotlinx.serialization.Serializable
import org.towny.kaizen.domain.models.Challenge
import org.towny.kaizen.domain.models.User

@Serializable
data class UserDTO(
    val id: String,
    val name: String,
    val profilePictureIndex: Int
) {
    companion object {
        fun from(user: User) = UserDTO(
            id = user.id,
            name = user.name,
            profilePictureIndex = user.profilePictureIndex
        )
    }

    fun toUser(challenges: List<Challenge> = emptyList()) = User(
        id = id,
        name = name,
        profilePictureIndex = profilePictureIndex,
        challenges = challenges
    )
}