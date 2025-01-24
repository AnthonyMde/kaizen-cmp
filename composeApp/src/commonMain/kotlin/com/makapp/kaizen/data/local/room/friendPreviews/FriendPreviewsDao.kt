package com.makapp.kaizen.data.local.room.friendPreviews

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendPreviewsDao {
    @Query("SELECT * FROM FriendPreviewEntity")
    fun watchAll(): Flow<List<FriendPreviewEntity>>

    @Upsert
    suspend fun insert(previews: List<FriendPreviewEntity>)

    @Query("DELETE FROM FriendPreviewEntity WHERE id NOT IN (:keptIds)")
    suspend fun deleteStalePreviews(keptIds: List<String>)

    @Transaction
    suspend fun refresh(previews: List<FriendPreviewEntity>) {
        val previewIds = previews.map { it.id }
        insert(previews)
        deleteStalePreviews(keptIds = previewIds)
    }
}