package org.towny.kaizen.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.towny.kaizen.data.repository.sources.RemoteFirestoreDataSource
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.ChallengesRepository
import org.towny.kaizen.domain.services.GetUserSessionUseCase

class ChallengesRepositoryImpl(
    private val remoteFirestoreDataSource: RemoteFirestoreDataSource,
    private val userSessionUseCase: GetUserSessionUseCase
) : ChallengesRepository {

    override suspend fun toggleChallenge(
        userId: String,
        challengeId: String,
        isChecked: Boolean
    ) {
        remoteFirestoreDataSource.toggleChallenge(userId, challengeId, isChecked)
    }

    override suspend fun create(name: String, numberOfErrors: Int): Flow<Resource<Unit>> {
        return flow<Resource<Unit>> {
            emit(Resource.Loading())

            val userId = userSessionUseCase.invoke() ?: run {
                emit(Resource.Error(DomainException.User.NoUserSessionFound))
                return@flow
            }
            val request = CreateChallengeRequest(
                userId = userId,
                name = name,
                maxFailures = numberOfErrors
            )

            remoteFirestoreDataSource.createChallenge(request)
            emit(Resource.Success())
        }.catch { e ->
            emit(Resource.Error(e))
        }
    }
}