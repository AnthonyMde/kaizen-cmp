package com.makapp.kaizen.data.repository.sources

import com.makapp.kaizen.data.remote.dto.ChallengeFirestoreDTO
import com.makapp.kaizen.data.remote.dto.UserDTO
import com.makapp.kaizen.data.repository.entities.ToggleStatusRequest
import kotlinx.coroutines.flow.Flow

interface FirestoreDataSource {
    fun watchCurrentUser(userId: String): Flow<UserDTO?>

    fun watchAllChallenges(userId: String): Flow<List<ChallengeFirestoreDTO>>
    suspend fun toggleChallengeStatus(request: ToggleStatusRequest)
}
