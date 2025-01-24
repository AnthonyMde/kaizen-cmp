package com.makapp.kaizen.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.makapp.kaizen.data.local.room.entities.FriendRequestEntity
import com.makapp.kaizen.data.local.room.entities.FriendRequestProfileEntity
import com.makapp.kaizen.data.local.room.entities.FriendRequestWithProfilesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendRequestsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(requestEntity: FriendRequestEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profileEntity: FriendRequestProfileEntity)

    @Transaction
    suspend fun insert(requests: List<FriendRequestWithProfilesEntity>) {
        requests.forEach { request ->
            insert(request.sender)
            insert(request.receiver)
            insert(request.friendRequest)
        }
    }

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
