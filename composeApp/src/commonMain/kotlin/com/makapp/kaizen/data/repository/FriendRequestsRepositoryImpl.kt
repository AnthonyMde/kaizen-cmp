package com.makapp.kaizen.data.repository

import com.makapp.kaizen.data.local.room.friendRequests.FriendRequestsDao
import com.makapp.kaizen.data.local.room.friendRequests.FriendRequestEntity
import com.makapp.kaizen.data.local.room.friendRequests.FriendRequestProfileEntity
import com.makapp.kaizen.data.local.room.friendRequests.FriendRequestWithProfilesEntity
import com.makapp.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import com.makapp.kaizen.data.toDomainException
import com.makapp.kaizen.domain.models.friend.FriendRequest
import com.makapp.kaizen.domain.models.friend.FriendRequestProfile
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.FriendRequestsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class FriendRequestsRepositoryImpl(
    private val firebaseFunctions: FirebaseFunctionsDataSource,
    private val friendRequestsDao: FriendRequestsDao
) : FriendRequestsRepository {
    override fun watchFriendRequests(): Flow<Resource<List<FriendRequest>>> {
        return friendRequestsDao.watchAll().map { requestEntities ->
            val requests = requestEntities.map { it.toDomainModel() }
            Resource.Success(requests)
        }.catch {
            Resource.Error<Resource<List<FriendRequest>>>(it)
        }
    }

    override suspend fun refreshFriendRequests(): Resource<Unit> {
        return try {
            val requests = firebaseFunctions.getFriendRequests()
            friendRequestsDao.refresh(requests.map { it.toRoomEntity() })
            Resource.Success()
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

    private fun FriendRequest.toRoomEntity(): FriendRequestWithProfilesEntity {
        return FriendRequestWithProfilesEntity(
            friendRequest = FriendRequestEntity(
                id = id,
                senderId = sender.id,
                receiverId = receiver.id,
                status = status
            ),
            sender = FriendRequestProfileEntity(
                id = sender.id,
                username = sender.username,
                displayName = sender.displayName,
                profilePictureIndex = sender.profilePictureIndex
            ),
            receiver = FriendRequestProfileEntity(
                id = receiver.id,
                username = receiver.username,
                displayName = receiver.displayName,
                profilePictureIndex = receiver.profilePictureIndex
            )
        )
    }

    private fun FriendRequestWithProfilesEntity.toDomainModel(): FriendRequest {
        return FriendRequest(
            id = friendRequest.id,
            status = friendRequest.status,
            sender = FriendRequestProfile(
                id = sender.id,
                username = sender.username,
                displayName = sender.displayName,
                profilePictureIndex = sender.profilePictureIndex
            ),
            receiver = FriendRequestProfile(
                id = receiver.id,
                username = receiver.username,
                displayName = receiver.displayName,
                profilePictureIndex = receiver.profilePictureIndex
            ),
        )
    }
}
