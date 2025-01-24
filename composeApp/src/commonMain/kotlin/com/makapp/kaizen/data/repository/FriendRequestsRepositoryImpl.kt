package com.makapp.kaizen.data.repository

import com.makapp.kaizen.data.local.room.FriendRequestsDao
import com.makapp.kaizen.data.local.room.entities.FriendRequestEntity
import com.makapp.kaizen.data.local.room.entities.FriendRequestProfileEntity
import com.makapp.kaizen.data.local.room.entities.FriendRequestWithProfilesEntity
import com.makapp.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import com.makapp.kaizen.data.toDomainException
import com.makapp.kaizen.domain.models.FriendRequest
import com.makapp.kaizen.domain.models.FriendRequestProfile
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
            friendRequestsDao.insert(requests.map { it.toRoomEntity() })
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
            if (status == FriendRequest.Status.CANCELED || status == FriendRequest.Status.DECLINED) {
                friendRequestsDao.delete(requestId)
            }
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
                profilePictureIndex = sender.profilePictureIndex
            ),
            receiver = FriendRequestProfileEntity(
                id = receiver.id,
                username = receiver.username,
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
                profilePictureIndex = sender.profilePictureIndex
            ),
            receiver = FriendRequestProfile(
                id = receiver.id,
                username = receiver.username,
                profilePictureIndex = receiver.profilePictureIndex
            ),
        )
    }
}
