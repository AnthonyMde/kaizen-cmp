package com.makapp.kaizen.data.local.room.converters

import androidx.room.TypeConverter
import com.makapp.kaizen.data.remote.dto.ChallengeDTO

class TimestampConverters {
    @TypeConverter
    fun fromTimestampToLong(timestamp: ChallengeDTO.Timestamp): Long {
        return (timestamp.seconds * 1000) + (timestamp.nanoseconds / 1_000_000) // returns millis.
    }

    @TypeConverter
    fun fromLongToTimestamp(epochMillis: Long): ChallengeDTO.Timestamp {
        val seconds = epochMillis / 1000
        val nanoseconds = ((epochMillis % 1000) * 1_000_000).toInt()

        return ChallengeDTO.Timestamp(seconds, nanoseconds)
    }
}
