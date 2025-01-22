package com.makapp.kaizen.data.repository

import dev.gitlive.firebase.functions.FirebaseFunctionsException
import com.makapp.kaizen.data.toDomainException
import com.makapp.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import com.makapp.kaizen.domain.models.FriendRequest
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.FriendRequestsRepository

class FriendRequestsRepositoryImpl(
    private val firebaseFunctions: FirebaseFunctionsDataSource
) : FriendRequestsRepository {
    override suspend fun getFriendRequests(): Resource<List<FriendRequest>> {
        return try {
            val requests = firebaseFunctions.getFriendRequests()
            Resource.Success(requests)
        } catch (e: Exception) {
            Resource.Error(e.toDomainException())
        }
    }

    override suspend fun createFriendRequest(friendId: String): Resource<Unit> {
        return try {
            firebaseFunctions.createFriendRequest(friendId)
            Resource.Success()
        } catch (e: Exception) {
            Resource.Error(e.toDomainException())

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
            Resource.Error(e.toDomainException())
        }
    }
}
