package com.makapp.kaizen.data.local.room.friends

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.makapp.kaizen.data.local.room.challenges.ChallengeEntity

@Entity
data class FriendEntity(
    @PrimaryKey val id: String,
    val name: String,
    val profilePictureIndex: Int
)

data class FriendWithChallengesEntity(
    @Embedded val friend: FriendEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val challenges: List<ChallengeEntity>
)
