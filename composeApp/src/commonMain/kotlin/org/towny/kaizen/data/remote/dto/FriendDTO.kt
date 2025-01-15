package org.towny.kaizen.data.remote.dto

import kotlinx.serialization.Serializable
import org.towny.kaizen.domain.models.Friend

@Serializable
data class FriendDTO(
    val id: String,
    val name: String,
    val profilePictureIndex: Int,
    val challenges: List<ChallengeDTO>
) {
    fun toFriend(): Friend {
        return Friend(
            id = id,
            name = name,
            profilePictureIndex = profilePictureIndex,
            challenges = challenges.map { it.toChallenge() }
        )
    }
}
