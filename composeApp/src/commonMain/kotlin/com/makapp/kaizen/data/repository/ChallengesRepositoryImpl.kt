package com.makapp.kaizen.data.repository

import com.makapp.kaizen.data.local.room.challenges.ChallengesDao
import com.makapp.kaizen.data.local.room.challenges.toChallengeDTO
import com.makapp.kaizen.data.remote.firebase_functions.ChallengeFieldName
import com.makapp.kaizen.data.repository.entities.CreateChallengeRequest
import com.makapp.kaizen.data.repository.entities.ToggleStatusRequest
import com.makapp.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import com.makapp.kaizen.data.repository.sources.FirestoreDataSource
import com.makapp.kaizen.data.toDomainException
import com.makapp.kaizen.domain.models.Challenge
import com.makapp.kaizen.domain.models.CreateChallengeForm
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.models.UpdateChallengeFields
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
            maxFailures = form.maxLives,
            commitment = form.commitment,
            expectations = form.expectations
        )

        firebaseFunctions.createChallenge(request)
        emit(Resource.Success())
    }.catch { e ->
        println("DEBUG: (firestore) Cannot create challenge because $e")
        emit(Resource.Error(e.toDomainException()))
    }

    override suspend fun update(
        id: String,
        fields: UpdateChallengeFields
    ): Flow<Resource<Unit>> = flow<Resource<Unit>> {
        emit(Resource.Loading())

        val fieldsToUpdate = mutableMapOf<String, Any>()
        if (fields.name != null) fieldsToUpdate[ChallengeFieldName.NAME] = fields.name
        if (fields.status?.name != null) fieldsToUpdate[ChallengeFieldName.STATUS] = fields.status.name
        if (fields.maxAuthorizedFailures != null) fieldsToUpdate[ChallengeFieldName.MAX_AUTHORIZED_FAILURES] =
            fields.maxAuthorizedFailures
        if (fields.expectations != null) fieldsToUpdate[ChallengeFieldName.EXPECTATIONS] = fields.expectations
        if (fields.commitment != null) fieldsToUpdate[ChallengeFieldName.COMMITMENT] = fields.commitment

        firebaseFunctions.updateChallenge(id, fieldsToUpdate)
        emit(Resource.Success())
    }.catch { e ->
        println("DEBUG: (firestore) Cannot update challenge because $e")
        emit(Resource.Error(e.toDomainException()))
    }

    override suspend fun getChallengeById(id: String): Resource<Challenge> = try {
        val challenge = challengesDao.getById(id).toChallengeDTO().toChallenge()
        Resource.Success(challenge)
    } catch (e: Exception) {
        Resource.Error(e.toDomainException())
    }
}