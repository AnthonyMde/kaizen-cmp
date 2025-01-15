package org.towny.kaizen.data.remote.dto

import kotlinx.serialization.Serializable
import org.towny.kaizen.domain.models.Friend
import org.towny.kaizen.domain.models.FriendPreview

@Serializable
data class FriendDTO(
    val id: String,
    val name: String,
    val profilePictureIndex: Int,
    val challenges: List<ChallengeDTO> = emptyList()
) {
    fun toFriend(): Friend {
        return Friend(
            id = id,
            name = name,
            profilePictureIndex = profilePictureIndex,
            challenges = challenges.map { it.toChallenge() }
        )
    }

    fun toFriendPreview(): FriendPreview {
        return FriendPreview(
            id = id,
            name = name,
            profilePictureIndex = profilePictureIndex,
        )
    }
}
