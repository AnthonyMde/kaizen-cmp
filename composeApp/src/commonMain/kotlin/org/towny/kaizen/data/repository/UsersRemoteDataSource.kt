package org.towny.kaizen.data.repository

import kotlinx.coroutines.flow.Flow
import org.towny.kaizen.data.remote.dto.ChallengeDTO
import org.towny.kaizen.data.remote.dto.UserDTO

interface UsersRemoteDataSource {
    fun watchAll(): Flow<List<UserDTO>>
    fun watchChallenges(userId: String): Flow<List<ChallengeDTO>>
}