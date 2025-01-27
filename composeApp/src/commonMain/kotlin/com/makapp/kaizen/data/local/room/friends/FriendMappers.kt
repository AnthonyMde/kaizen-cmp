package com.makapp.kaizen.data.local.room.friends

import com.makapp.kaizen.data.local.room.challenges.toChallengeDTO
import com.makapp.kaizen.data.local.room.challenges.toChallengeEntity
import com.makapp.kaizen.data.remote.dto.ChallengeDTO
import com.makapp.kaizen.data.remote.dto.FriendDTO

fun FriendWithChallengesEntity.toFriendDTO(): FriendDTO {
    return FriendDTO(
        id = friend.id,
        name = friend.name,
        displayName = friend.displayName,
        profilePictureIndex = friend.profilePictureIndex,
        challenges = challenges.map { it.toChallengeDTO() },
        isFavorite = friend.isFavorite
    )
}

fun FriendDTO.toFriendWithChallengesEntity(challenges: List<ChallengeDTO>): FriendWithChallengesEntity {
    return FriendWithChallengesEntity(
        friend = FriendEntity(
            id = id,
            name = name,
            displayName = displayName,
            profilePictureIndex = profilePictureIndex,
            isFavorite = isFavorite
        ),
        challenges = challenges.map { it.toChallengeEntity(userId = id) }
    )
}
