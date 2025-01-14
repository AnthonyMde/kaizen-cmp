package org.towny.kaizen.data.remote.dto

import kotlinx.serialization.Serializable
import org.towny.kaizen.domain.models.Challenge
import org.towny.kaizen.domain.models.User

@Serializable
data class UserDTO(
    val id: String,
    val email: String,
    val name: String,
    val profilePictureIndex: Int,
    val friendIds: List<String>
) {
    companion object {
        fun from(user: User, friendIds: List<String> = emptyList()) = UserDTO(
            id = user.id,
            email = user.email,
            name = user.name,
            profilePictureIndex = user.profilePictureIndex,
            friendIds = friendIds
        )
    }

    fun toUser(challenges: List<Challenge> = emptyList()) = User(
        id = id,
        email = email,
        name = name,
        profilePictureIndex = profilePictureIndex,
        challenges = challenges
    )
}