package com.makapp.kaizen.data.local.room.friendRequests

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendRequestsDao {
    @Upsert
    suspend fun insert(requestEntity: FriendRequestEntity)

    @Upsert
    suspend fun insert(profileEntity: FriendRequestProfileEntity)

    @Transaction
    suspend fun insert(requests: List<FriendRequestWithProfilesEntity>) {
        requests.forEach { request ->
            insert(request.sender)
            insert(request.receiver)
            insert(request.friendRequest)
        }
    }

    @Query("DELETE FROM FriendRequestEntity WHERE id NOT IN (:keptIds)")
    suspend fun deleteStaleRequests(keptIds: List<String>)

    @Query("DELETE FROM FriendRequestProfileEntity WHERE id NOT IN (:keptIds)")
    suspend fun deleteStateProfiles(keptIds: List<String>)

    @Transaction
    suspend fun refresh(requests: List<FriendRequestWithProfilesEntity>) {
        val requestIds = requests.map { it.friendRequest.id }
        val profileIds = requests.flatMap { listOf(it.sender.id, it.receiver.id) }

        insert(requests)
        deleteStaleRequests(keptIds = requestIds)
        deleteStateProfiles(keptIds = profileIds)
    }

    @Transaction
    @Query("SELECT * FROM FriendRequestEntity WHERE id = :id")
    suspend fun findById(id: String): FriendRequestWithProfilesEntity

    @Delete
    suspend fun delete(requestEntity: FriendRequestEntity)

    @Delete
    suspend fun delete(profileEntity: FriendRequestProfileEntity)

    @Transaction
    suspend fun delete(requestId: String) {
        val request = findById(requestId)
        delete(request.friendRequest)
        delete(request.sender)
        delete(request.receiver)
    }

    @Transaction
    @Query("SELECT * FROM FriendRequestEntity")
    fun watchAll(): Flow<List<FriendRequestWithProfilesEntity>>

    @Transaction
    @Query("SELECT * FROM FriendRequestEntity")
    suspend fun getAll(): List<FriendRequestWithProfilesEntity>
}
