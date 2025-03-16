package com.makapp.kaizen.data.local.room.challenges

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengesDao {
    @Query("SELECT * FROM ChallengeEntity WHERE id = (:id)")
    suspend fun getById(id: String): ChallengeEntity

    @Query("SELECT * FROM ChallengeEntity WHERE id = (:id)")
    fun watchById(id: String): Flow<ChallengeEntity>

    @Upsert
    suspend fun insertChallenges(challenges: List<ChallengeEntity>)
}
