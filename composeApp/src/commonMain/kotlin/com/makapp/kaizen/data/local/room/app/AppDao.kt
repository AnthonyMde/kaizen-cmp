package com.makapp.kaizen.data.local.room.app

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface AppDao {
    @Query("DELETE FROM UserEntity")
    suspend fun deleteUsers()

    @Query("DELETE FROM FriendEntity")
    suspend fun deleteFriends()

    @Query("DELETE FROM FriendRequestEntity")
    suspend fun deleteFriendRequests()

    @Query("DELETE FROM FriendRequestProfileEntity")
    suspend fun deleteFriendRequestProfiles()

    @Query("DELETE FROM FriendPreviewEntity")
    suspend fun deleteFriendPreviews()

    @Query("DELETE FROM ChallengeEntity")
    suspend fun deleteChallenges()

    @Transaction
    suspend fun deleteNetworkCache() {
        deleteUsers()
        deleteFriends()
        deleteFriendRequests()
        deleteFriendRequestProfiles()
        deleteFriendPreviews()
        deleteChallenges()
    }
}
