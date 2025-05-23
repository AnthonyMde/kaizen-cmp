package com.makapp.kaizen.data.local.room.challenges

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makapp.kaizen.data.remote.dto.ChallengeDTO.Timestamp
import com.makapp.kaizen.domain.models.challenge.Challenge

@Entity
data class ChallengeEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val status: Challenge.Status,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
    val days:  Int,
    val isDoneForToday: Boolean,
    val failureCount: Int,
    val maxAuthorizedFailures: Int,
    val isDeleted: Boolean,
    val commitment: String?,
    val expectations: String?,
    val lastFailureDate: Timestamp?,
    val didUseForgotFeatureToday: Boolean = false,
)
