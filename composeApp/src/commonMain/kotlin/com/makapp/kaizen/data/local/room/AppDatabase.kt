package com.makapp.kaizen.data.local.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.makapp.kaizen.data.local.room.entities.FriendRequestEntity
import com.makapp.kaizen.data.local.room.entities.FriendRequestProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

const val ROOM_DB_FILE_NAME = "kaizen_room.db"

@Database(
    entities = [
        FriendRequestEntity::class,
        FriendRequestProfileEntity::class
    ],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getFriendRequestsDao(): FriendRequestsDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
