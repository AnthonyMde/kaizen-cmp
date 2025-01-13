package org.towny.kaizen.data.repository

import dev.gitlive.firebase.functions.FirebaseFunctionsException
import org.towny.kaizen.data.remote.firebase_functions.toDomainException
import org.towny.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import org.towny.kaizen.data.repository.sources.FirestoreDataSource
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.FriendPreview
import org.towny.kaizen.domain.models.FriendRequest
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.AuthRepository
import org.towny.kaizen.domain.repository.FriendsRepository

class FriendsRepositoryImpl(
    private val firestore: FirestoreDataSource,
    private val firebaseFunctions: FirebaseFunctionsDataSource,
    private val authRepository: AuthRepository
) : FriendsRepository {
    override suspend fun getFriendPreview(username: String): Resource<FriendPreview> {
        return try {
            val preview = firebaseFunctions.getFriendPreviewByName(username)
            Resource.Success(preview)
        } catch (e: Exception) {
            if (e is FirebaseFunctionsException) {
                Resource.Error(e.toDomainException())
            } else Resource.Error(e)
        }
    }

    override suspend fun createOrUpdateFriendRequest(request: FriendRequest): Resource<Unit> {
        val userId = authRepository.getUserSession()?.uid
            ?: return Resource.Error(DomainException.User.NoUserSessionFound)

        return try {
            firestore.createOrUpdateFriendRequest(userId = userId, request = request)
            Resource.Success()
        } catch (e: Exception) {
            if (e is FirebaseFunctionsException) {
                Resource.Error(e.toDomainException())
            } else Resource.Error(e)
        }
    }
}
