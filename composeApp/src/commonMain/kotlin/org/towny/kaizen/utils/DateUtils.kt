package org.towny.kaizen.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateUtils {
    fun getTodaysDate(): String {
        val now = Clock.System.now()
        val zone = TimeZone.currentSystemDefault()
        val localDate = now.toLocalDateTime(zone).date

        val dayOfWeek = localDate.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
        val day = localDate.dayOfMonth.toString().padStart(2, '0')
        val month = localDate.month.name.lowercase()
        val year = localDate.year

        return "$dayOfWeek, $day $month $year"
    }
}