package org.towny.kaizen.data.repository

import org.towny.kaizen.data.repository.sources.FirestoreDataSource
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.FriendRequest
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.AuthRepository
import org.towny.kaizen.domain.repository.FriendsRepository

class FriendsRepositoryImpl(
    private val firestore: FirestoreDataSource,
    private val authRepository: AuthRepository
) : FriendsRepository {
    override suspend fun createOrUpdateFriendRequest(request: FriendRequest): Resource<Unit> {
        val userId = authRepository.getUserSession()?.uid
            ?: return Resource.Error(DomainException.User.NoUserSessionFound)

        return try {
            firestore.createOrUpdateFriendRequest(userId = userId, request = request)
            Resource.Success()
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}
