package com.makapp.kaizen.data.local.room.converters

import androidx.room.TypeConverter

class StringListConverters {

    @TypeConverter
    fun fromStringListToString(list: List<String>): String {
        return list.joinToString(separator = ",")
    }

    @TypeConverter
    fun fromStringToStringList(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split(",")
    }
}
