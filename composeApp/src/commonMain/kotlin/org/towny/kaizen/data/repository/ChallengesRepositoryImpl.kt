package org.towny.kaizen.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.towny.kaizen.data.repository.entities.CreateChallengeRequest
import org.towny.kaizen.data.repository.sources.FirestoreDataSource
import org.towny.kaizen.data.toDomainException
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.ChallengesRepository

class ChallengesRepositoryImpl(
    private val firestore: FirestoreDataSource
) : ChallengesRepository {

    override suspend fun toggleStatus(
        userId: String,
        challengeId: String,
        isChecked: Boolean
    ): Resource<Unit> = try {
        firestore.toggleChallengeStatus(userId, challengeId, isChecked)
        Resource.Success()
    } catch (e: Exception) {
        println("DEBUG: (firestore) Cannot toggle challenge's state because $e")
        Resource.Error(e.toDomainException())
    }

    override suspend fun create(
        userId: String,
        name: String,
        numberOfErrors: Int
    ): Flow<Resource<Unit>> = flow<Resource<Unit>> {
        emit(Resource.Loading())

        val request = CreateChallengeRequest(
            userId = userId,
            name = name,
            maxFailures = numberOfErrors
        )

        firestore.createChallenge(request)
        emit(Resource.Success())
    }.catch { e ->
        println("DEBUG: (firestore) Cannot create challenge because $e")
        emit(Resource.Error(e.toDomainException()))
    }
}