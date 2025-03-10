package com.makapp.kaizen.data.local.room.challenges

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makapp.kaizen.data.remote.dto.ChallengeDTO
import com.makapp.kaizen.domain.models.Challenge.Status

@Entity
data class ChallengeEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val status: Status,
    val createdAt: ChallengeDTO.Timestamp,
    val updatedAt: ChallengeDTO.Timestamp,
    val days:  Int,
    val isDoneForToday: Boolean,
    val failureCount: Int,
    val maxAuthorizedFailures: Int,
    val isDeleted: Boolean,
    val commitment: String?,
    val expectations: String?
)
