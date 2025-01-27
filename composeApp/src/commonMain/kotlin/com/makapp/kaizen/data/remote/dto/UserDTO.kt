package com.makapp.kaizen.data.remote.dto

import kotlinx.serialization.Serializable
import com.makapp.kaizen.domain.models.Challenge
import com.makapp.kaizen.domain.models.User

@Serializable
data class UserDTO(
    val id: String,
    val email: String,
    val name: String,
    val displayName: String? = null,
    val profilePictureIndex: Int,
    val friendIds: List<String>,
) {
    companion object {
        fun from(
            user: User,
            friendIds: List<String> = emptyList()
        ) = UserDTO(
            id = user.id,
            email = user.email,
            name = user.name,
            displayName = user.displayName,
            profilePictureIndex = user.profilePictureIndex,
            friendIds = friendIds
        )
    }

    fun toUser(challenges: List<Challenge> = emptyList()) = User(
        id = id,
        email = email,
        name = name,
        displayName = displayName,
        profilePictureIndex = profilePictureIndex,
        challenges = challenges
    )
}