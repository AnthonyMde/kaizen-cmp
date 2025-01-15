package org.towny.kaizen.data.repository

import org.towny.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import org.towny.kaizen.data.toDomainException
import org.towny.kaizen.domain.models.Friend
import org.towny.kaizen.domain.models.FriendPreview
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.FriendsRepository

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
