package com.makapp.kaizen.data.local.room.friends

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.makapp.kaizen.data.local.room.challenges.ChallengeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendsDao {
    @Transaction
    @Query("SELECT * FROM FriendEntity")
    fun watchAll(): Flow<List<FriendWithChallengesEntity>>

    @Upsert
    suspend fun insertFriends(friends: List<FriendEntity>)

    @Upsert
    suspend fun insertChallenges(challenges: List<ChallengeEntity>)

    @Query("DELETE FROM FriendEntity WHERE id NOT IN (:keptIds)")
    suspend fun deleteStaleFriendEntities(keptIds: List<String>)

    @Query("DELETE FROM ChallengeEntity WHERE id NOT IN (:keptIds)")
    suspend fun deleteStaleChallengeEntities(keptIds: List<String>)

    @Transaction
    suspend fun refresh(friendsWithChallenges: List<FriendWithChallengesEntity>) {
        val friendIds = mutableListOf<String>()
        val challengesIds = mutableListOf<String>()

        friendsWithChallenges.forEach { friendWithChallenges ->
            friendIds.add(friendWithChallenges.friend.id)
            friendWithChallenges.challenges.forEach { challenge ->
                challengesIds.add(challenge.id)
            }
        }

        insertChallenges(friendsWithChallenges.flatMap { it.challenges })
        insertFriends(friendsWithChallenges.map { it.friend })

        deleteStaleFriendEntities(keptIds = friendIds)
        deleteStaleChallengeEntities(keptIds = challengesIds)
    }
}
