package org.towny.kaizen.data.repository

import org.towny.kaizen.data.remote.FirestoreDataSource
import org.towny.kaizen.data.repository.sources.RemoteDataSource
import org.towny.kaizen.domain.repository.ChallengesRepository

class ChallengesRepositoryImpl(
    private val remoteDataSource: RemoteDataSource = FirestoreDataSource()
): ChallengesRepository {

    override suspend fun toggleChallenge(
        userId: String,
        challengeId: String,
        isChecked: Boolean
    ) {
        remoteDataSource.toggleChallenge(userId, challengeId, isChecked)
    }
}