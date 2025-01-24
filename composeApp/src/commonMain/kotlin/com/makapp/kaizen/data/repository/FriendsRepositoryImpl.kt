package com.makapp.kaizen.data.repository

import com.makapp.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import com.makapp.kaizen.data.toDomainException
import com.makapp.kaizen.domain.models.Friend
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.FriendsRepository

class FriendsRepositoryImpl(
    private val firebaseFunctions: FirebaseFunctionsDataSource,
) : FriendsRepository {
    override suspend fun getFriends(): Resource<List<Friend>> = try {
        val friends = firebaseFunctions.getFriends(includeChallenges = true)
            .map { it.toFriend() }
        Resource.Success(friends)
    } catch (e: Exception) {
        Resource.Error(e.toDomainException())
    }
}
