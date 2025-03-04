package com.makapp.kaizen.utils

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateUtils {
    fun getTodaysDate(): String {
        val now = Clock.System.now()
        val zone = TimeZone.currentSystemDefault()
        val localDate = now.toLocalDateTime(zone).date

        val dayOfWeek =
            localDate.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
        val day = localDate.dayOfMonth.toString().padStart(2, '0')
        val month = localDate.month.name.lowercase()
        val year = localDate.year

        return "$dayOfWeek, $day $month $year"
    }

    fun Timestamp.toLocalDate(): LocalDate {
        return Instant.fromEpochSeconds(this.seconds, this.nanoseconds)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    fun getCurrentLocalDate(): LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date

    fun LocalDate.toShortDateFormat(): String {
        return this.dayOfMonth.toString().padStart(2, '0') + "/" +
                this.monthNumber.toString().padStart(2, padChar = '0') + "/" +
                this.year.toString()
    }
}
