package com.makapp.kaizen.data.local.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.makapp.kaizen.data.local.room.app.AppDao
import com.makapp.kaizen.data.local.room.challenges.ChallengeEntity
import com.makapp.kaizen.data.local.room.challenges.ChallengesDao
import com.makapp.kaizen.data.local.room.converters.StringListConverters
import com.makapp.kaizen.data.local.room.converters.TimestampConverters
import com.makapp.kaizen.data.local.room.friendPreviews.FriendPreviewEntity
import com.makapp.kaizen.data.local.room.friendRequests.FriendRequestEntity
import com.makapp.kaizen.data.local.room.friendRequests.FriendRequestProfileEntity
import com.makapp.kaizen.data.local.room.friendPreviews.FriendPreviewsDao
import com.makapp.kaizen.data.local.room.friendRequests.FriendRequestsDao
import com.makapp.kaizen.data.local.room.friends.FriendEntity
import com.makapp.kaizen.data.local.room.friends.FriendsDao
import com.makapp.kaizen.data.local.room.user.UserDao
import com.makapp.kaizen.data.local.room.user.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

const val ROOM_DB_FILE_NAME = "kaizen_room.db"

@Database(
    entities = [
        UserEntity::class,
        FriendEntity::class,
        FriendRequestEntity::class,
        FriendRequestProfileEntity::class,
        FriendPreviewEntity::class,
        ChallengeEntity::class
    ],
    version = 2
)
@TypeConverters(TimestampConverters::class, StringListConverters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getFriendRequestsDao(): FriendRequestsDao
    abstract fun getFriendPreviewsDao(): FriendPreviewsDao
    abstract fun getFriendsDao(): FriendsDao
    abstract fun getUserDao(): UserDao
    abstract fun getAppDao(): AppDao
    abstract fun getChallengesDao(): ChallengesDao
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
