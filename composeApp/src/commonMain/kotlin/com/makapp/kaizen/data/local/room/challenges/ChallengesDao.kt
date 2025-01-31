package com.makapp.kaizen.data.local.room.challenges

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ChallengesDao {
    @Query("SELECT * FROM ChallengeEntity WHERE id = (:id)")
    suspend fun getById(id: String): ChallengeEntity

    @Upsert
    suspend fun insertChallenges(challenges: List<ChallengeEntity>)
}
