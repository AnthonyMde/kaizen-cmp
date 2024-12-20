package org.towny.kaizen.data.repository

import org.towny.kaizen.data.repository.sources.RemoteFirestoreDataSource
import org.towny.kaizen.domain.repository.ChallengesRepository

class ChallengesRepositoryImpl(
    private val remoteFirestoreDataSource: RemoteFirestoreDataSource
): ChallengesRepository {

    override suspend fun toggleChallenge(
        userId: String,
        challengeId: String,
        isChecked: Boolean
    ) {
        remoteFirestoreDataSource.toggleChallenge(userId, challengeId, isChecked)
    }
}