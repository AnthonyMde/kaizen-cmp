package org.towny.kaizen.data.repository

import dev.gitlive.firebase.functions.FirebaseFunctionsException
import org.towny.kaizen.data.remote.firebase_functions.toDomainException
import org.towny.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import org.towny.kaizen.domain.models.FriendRequest
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.FriendRequestsRepository

class FriendRequestsRepositoryImpl(
    private val firebaseFunctions: FirebaseFunctionsDataSource
) : FriendRequestsRepository {
    override suspend fun getFriendRequests(): Resource<List<FriendRequest>> {
        return try {
            val requests = firebaseFunctions.getFriendRequests()
            Resource.Success(requests)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun createFriendRequest(friendId: String): Resource<Unit> {
        return try {
            firebaseFunctions.createFriendRequest(friendId)
            Resource.Success()
        } catch (e: Exception) {
            if (e is FirebaseFunctionsException) {
                Resource.Error(e.toDomainException())
            } else {
                Resource.Error(e)
            }
        }
    }

    override suspend fun updateFriendRequest(
        requestId: String,
        status: FriendRequest.Status
    ): Resource<Unit> {
        return try {
            firebaseFunctions.updateFriendRequest(requestId, status)
            Resource.Success()
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}
