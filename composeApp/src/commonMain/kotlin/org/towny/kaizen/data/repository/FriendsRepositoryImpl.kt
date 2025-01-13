package org.towny.kaizen.data.repository

import dev.gitlive.firebase.functions.FirebaseFunctionsException
import org.towny.kaizen.data.remote.firebase_functions.toDomainException
import org.towny.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import org.towny.kaizen.domain.models.FriendPreview
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.FriendsRepository

class FriendsRepositoryImpl(
    private val firebaseFunctions: FirebaseFunctionsDataSource
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
}
