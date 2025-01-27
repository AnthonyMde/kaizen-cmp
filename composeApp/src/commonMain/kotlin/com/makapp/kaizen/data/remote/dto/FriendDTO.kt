package com.makapp.kaizen.data.remote.dto

import com.makapp.kaizen.domain.models.Friend
import com.makapp.kaizen.domain.models.FriendPreview
import kotlinx.serialization.Serializable

@Serializable
data class FriendDTO(
    val id: String,
    val name: String,
    val displayName: String? = null,
    val profilePictureIndex: Int,
    val challenges: List<ChallengeDTO> = emptyList(),
    val isFavorite: Boolean
) {
    fun toFriend(): Friend {
        return Friend(
            id = id,
            name = name,
            displayName = displayName,
            profilePictureIndex = profilePictureIndex,
            challenges = challenges.map { it.toChallenge() },
            isFavorite = isFavorite
        )
    }

    fun toFriendPreview(): FriendPreview {
        return FriendPreview(
            id = id,
            name = name,
            displayName = displayName,
            profilePictureIndex = profilePictureIndex,
            isFavorite = isFavorite
        )
    }
}
