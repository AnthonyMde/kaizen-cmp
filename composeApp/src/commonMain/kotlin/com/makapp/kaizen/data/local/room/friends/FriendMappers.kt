package com.makapp.kaizen.data.local.room.friends

import com.makapp.kaizen.data.local.room.challenges.toChallengeDTO
import com.makapp.kaizen.data.local.room.challenges.toChallengeEntity
import com.makapp.kaizen.data.remote.dto.ChallengeDTO
import com.makapp.kaizen.data.remote.dto.FriendDTO

fun FriendWithChallengesEntity.toFriendDTO(): FriendDTO {
    return FriendDTO(
        id = friend.id,
        name = friend.name,
        profilePictureIndex = friend.profilePictureIndex,
        challenges = challenges.map { it.toChallengeDTO() }
    )
}

fun FriendDTO.toFriendWithChallengesEntity(challenges: List<ChallengeDTO>): FriendWithChallengesEntity {
    return FriendWithChallengesEntity(
        friend = FriendEntity(
            id = id,
            name = name,
            profilePictureIndex = profilePictureIndex
        ),
        challenges = challenges.map { it.toChallengeEntity(userId = id) }
    )
}
