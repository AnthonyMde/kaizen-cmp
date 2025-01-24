package com.makapp.kaizen.data.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.makapp.kaizen.data.local.room.AppDatabase
import com.makapp.kaizen.data.local.room.ROOM_DB_FILE_NAME

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath(ROOM_DB_FILE_NAME)
    return Room.databaseBuilder(
        context = appContext,
        name = dbFile.absolutePath
    )
}
