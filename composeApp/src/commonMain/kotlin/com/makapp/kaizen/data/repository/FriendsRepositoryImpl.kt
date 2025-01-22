package com.makapp.kaizen.data.repository

import com.makapp.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import com.makapp.kaizen.data.toDomainException
import com.makapp.kaizen.domain.models.Friend
import com.makapp.kaizen.domain.models.FriendPreview
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.FriendsRepository

class FriendsRepositoryImpl(
    private val firebaseFunctions: FirebaseFunctionsDataSource
) : FriendsRepository {
    override suspend fun getFriendPreview(username: String): Resource<FriendPreview> = try {
        val preview = firebaseFunctions.getFriendPreviewByName(username)
        Resource.Success(preview)
    } catch (e: Exception) {
        Resource.Error(e.toDomainException())
    }

    override suspend fun getFriendPreviews(): Resource<List<FriendPreview>> = try {
        val friendPreviews = firebaseFunctions.getFriends(includeChallenges = false)
            .map { it.toFriendPreview() }
        Resource.Success(friendPreviews)
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
