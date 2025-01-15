package org.towny.kaizen.data.repository

import dev.gitlive.firebase.functions.FirebaseFunctionsException
import org.towny.kaizen.data.remote.firebase_functions.toDomainException
import org.towny.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import org.towny.kaizen.domain.models.Friend
import org.towny.kaizen.domain.models.FriendPreview
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.FriendsRepository

class FriendsRepositoryImpl(
    private val firebaseFunctions: FirebaseFunctionsDataSource
): FriendsRepository {
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

    override suspend fun getFriends(): Resource<List<Friend>> {
        return try {
            val friends = firebaseFunctions.getFriends().map { it.toFriend() }
            Resource.Success(friends)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}
