package com.makapp.kaizen.data.repository

import com.makapp.kaizen.data.local.room.friendPreviews.FriendPreviewEntity
import com.makapp.kaizen.data.local.room.friendPreviews.FriendPreviewsDao
import com.makapp.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import com.makapp.kaizen.data.toDomainException
import com.makapp.kaizen.domain.models.Friend
import com.makapp.kaizen.domain.models.FriendPreview
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.FriendsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class FriendsRepositoryImpl(
    private val firebaseFunctions: FirebaseFunctionsDataSource,
    private val friendPreviewsDao: FriendPreviewsDao
) : FriendsRepository {
    override suspend fun getFriendPreview(username: String): Resource<FriendPreview> = try {
        val preview = firebaseFunctions.getFriendPreviewByName(username)
        Resource.Success(preview)
    } catch (e: Exception) {
        Resource.Error(e.toDomainException())
    }

    override fun watchFriendPreviews(): Flow<Resource<List<FriendPreview>>> {
        return friendPreviewsDao.watchAll().map { previews ->
            val models = previews.map { it.toFriendPreview() }
            Resource.Success(models)
        }.catch {
            Resource.Error<Resource<List<FriendPreview>>>(it.toDomainException())
        }
    }

    override suspend fun refreshFriendPreviews(): Resource<Unit> = try {
        val friendPreviews = firebaseFunctions.getFriends(includeChallenges = false)
            .map { it.toFriendPreview() }
        friendPreviewsDao.refresh(friendPreviews.map { it.toFriendPreviewEntity() })
        Resource.Success()
    } catch (e: Exception) {
        Resource.Error(e.toDomainException())
    }

    override suspend fun getFriends(): Resource<List<Friend>> = try {
        val friends = firebaseFunctions.getFriends(includeChallenges = true)
            .map { it.toFriend() }
        Resource.Success(friends)
    } catch (e: Exception) {
        Resource.Error(e.toDomainException())
    }
}

private fun FriendPreviewEntity.toFriendPreview(): FriendPreview {
    return FriendPreview(
        id = id,
        name = name,
        profilePictureIndex = profilePictureIndex
    )
}

private fun FriendPreview.toFriendPreviewEntity(): FriendPreviewEntity {
    return FriendPreviewEntity(
        id = id,
        name = name,
        profilePictureIndex = profilePictureIndex
    )
}
