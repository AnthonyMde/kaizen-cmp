package com.makapp.kaizen.data.repository

import com.makapp.kaizen.data.local.room.challenges.ChallengesDao
import com.makapp.kaizen.data.local.room.challenges.toChallengeDTO
import com.makapp.kaizen.data.repository.entities.CreateChallengeRequest
import com.makapp.kaizen.data.repository.entities.ToggleStatusRequest
import com.makapp.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import com.makapp.kaizen.data.repository.sources.FirestoreDataSource
import com.makapp.kaizen.data.toDomainException
import com.makapp.kaizen.domain.models.Challenge
import com.makapp.kaizen.domain.models.CreateChallengeForm
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.ChallengesRepository
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ChallengesRepositoryImpl(
    private val firestore: FirestoreDataSource,
    private val firebaseFunctions: FirebaseFunctionsDataSource,
    private val challengesDao: ChallengesDao
) : ChallengesRepository {

    override suspend fun toggleStatus(
        userId: String,
        challengeId: String,
        isChecked: Boolean
    ): Resource<Unit> = try {
        val request = ToggleStatusRequest(
            userId = userId,
            challengeId = challengeId,
            isChecked = isChecked,
            updatedAt = Timestamp.now()
        )
        firestore.toggleChallengeStatus(request)
        Resource.Success()
    } catch (e: Exception) {
        println("DEBUG: (firestore) Cannot toggle challenge's state because $e")
        Resource.Error(e.toDomainException())
    }

    override suspend fun create(
        form: CreateChallengeForm
    ): Flow<Resource<Unit>> = flow<Resource<Unit>> {
        emit(Resource.Loading())

        val request = CreateChallengeRequest(
            name = form.name,
            maxFailures = form.numberOfErrors.toInt(),
            commitment = form.commitment,
            expectations = form.expectations
        )

        firebaseFunctions.createChallenge(request)
        emit(Resource.Success())
    }.catch { e ->
        println("DEBUG: (firestore) Cannot create challenge because $e")
        emit(Resource.Error(e.toDomainException()))
    }

    override suspend fun getChallengeById(id: String): Resource<Challenge> = try {
        val challenge = challengesDao.getById(id).toChallengeDTO().toChallenge()
        Resource.Success(challenge)
    } catch (e: Exception) {
        Resource.Error(e.toDomainException())
    }
}