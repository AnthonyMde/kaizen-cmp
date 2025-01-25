package com.makapp.kaizen.data.local.room.challenges

import com.makapp.kaizen.data.remote.dto.ChallengeDTO

fun ChallengeDTO.toChallengeEntity(userId: String): ChallengeEntity {
    return ChallengeEntity(
        id = id,
        userId = userId,
        name = name,
        status = status,
        createdAt = createdAt,
        days = days,
        isDoneForToday = isDoneForToday,
        failureCount = failureCount,
        maxAuthorizedFailures = maxAuthorizedFailures
    )
}

fun ChallengeEntity.toChallengeDTO(): ChallengeDTO {
    return ChallengeDTO(
        id = id,
        name = name,
        status = status,
        createdAt = createdAt,
        days = days,
        isDoneForToday = isDoneForToday,
        failureCount = failureCount,
        maxAuthorizedFailures = maxAuthorizedFailures
    )
}
