package org.towny.kaizen.data.repository

import dev.gitlive.firebase.functions.FirebaseFunctionsException
import org.towny.kaizen.data.remote.firebase_functions.toDomainException
import org.towny.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.FriendPreview
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.repository.FriendsRepository
import org.towny.kaizen.domain.repository.UsersRepository

class FriendsRepositoryImpl(
    private val firebaseFunctions: FirebaseFunctionsDataSource,
    private val usersRepository: UsersRepository
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
        val userId = usersRepository.getCurrentUser()?.id ?: return Resource.Error(
            DomainException.User.NoUserAccountFound
        )

        return try {
            firebaseFunctions.createFriendRequest(userId)
            Resource.Success()
        } catch (e: Exception) {
            if (e is FirebaseFunctionsException) {
                Resource.Error(e.toDomainException())
            } else Resource.Error(e)
        }
    }
}
