package com.makapp.kaizen.data.repository

import com.makapp.kaizen.data.local.room.friendPreviews.FriendPreviewEntity
import com.makapp.kaizen.data.local.room.friendPreviews.FriendPreviewsDao
import com.makapp.kaizen.data.local.room.user.UserDao
import com.makapp.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import com.makapp.kaizen.data.toDomainException
import com.makapp.kaizen.domain.models.FriendPreview
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.FriendPreviewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class FriendPreviewsRepositoryImpl(
    private val firebaseFunctions: FirebaseFunctionsDataSource,
    private val friendPreviewsDao: FriendPreviewsDao,
    private val userDao: UserDao
) : FriendPreviewsRepository {
    override suspend fun getFriendPreview(username: String): Resource<FriendPreview> = try {
        val preview = firebaseFunctions.getFriendPreviewByName(username)
        Resource.Success(preview)
    } catch (e: Exception) {
        Resource.Error(e.toDomainException())
    }

    override fun watchFriendPreviews(): Flow<Resource<List<FriendPreview>>> =
        friendPreviewsDao.watchAll().map { previewEntities ->
            val previews = previewEntities.map { it.toFriendPreview() }
            Resource.Success(previews)
        }.catch {
            Resource.Error<Resource<List<FriendPreview>>>(it.toDomainException())
        }

    override suspend fun refreshFriendPreviews(): Resource<Unit> = try {
        val friendPreviews = firebaseFunctions.getFriends(includeChallenges = false)
            .map { it.toFriendPreview() }
        friendPreviewsDao.refresh(friendPreviews.map { it.toFriendPreviewEntity() })
        Resource.Success()
    } catch (e: Exception) {
        Resource.Error(e.toDomainException())
    }
}

private fun FriendPreviewEntity.toFriendPreview(): FriendPreview {
    return FriendPreview(
        id = id,
        name = name,
        displayName = displayName,
        profilePictureIndex = profilePictureIndex,
        isFavorite = isFavorite
    )
}

private fun FriendPreview.toFriendPreviewEntity(): FriendPreviewEntity {
    return FriendPreviewEntity(
        id = id,
        name = name,
        displayName = displayName,
        profilePictureIndex = profilePictureIndex,
        isFavorite = isFavorite
    )
}
