package com.makapp.kaizen.data.local.room.user

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert

@Dao
interface UserDao {
    @Upsert
    suspend fun insert(user: UserEntity)

    @Query("DELETE FROM UserEntity WHERE id != :id")
    suspend fun deleteStaleUserEntity(id: String)

    @Query("SELECT * FROM UserEntity LIMIT 1")
    suspend fun getUser(): UserEntity?

    @Transaction
    suspend fun refreshUser(user: UserEntity) {
        insert(user)
        deleteStaleUserEntity(user.id)
    }
}
