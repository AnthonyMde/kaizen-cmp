package com.makapp.kaizen.data.repository

import com.makapp.kaizen.data.local.room.friends.FriendsDao
import com.makapp.kaizen.data.local.room.friends.toFriendDTO
import com.makapp.kaizen.data.local.room.friends.toFriendWithChallengesEntity
import com.makapp.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import com.makapp.kaizen.data.toDomainException
import com.makapp.kaizen.domain.models.Friend
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.repository.FriendsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class FriendsRepositoryImpl(
    private val firebaseFunctions: FirebaseFunctionsDataSource,
    private val friendsDao: FriendsDao
) : FriendsRepository {
    override fun watchFriends(): Flow<Resource<List<Friend>>> {
        return friendsDao.watchAll()
            .map { friendsWithChallenges ->
                val friendsDTO = friendsWithChallenges
                    .map { it.toFriendDTO() }
                val friends = friendsDTO.map { it.toFriend() }
                Resource.Success(friends)
            }.catch {
                Resource.Error<List<Friend>>(it.toDomainException())
            }
    }

    override suspend fun refreshFriends(): Resource<Unit> = try {
        val friendDTOs = firebaseFunctions.getFriends(includeChallenges = true)
        val friendsWithChallengesEntities =
            friendDTOs.map { it.toFriendWithChallengesEntity(it.challenges) }

        friendsDao.refresh(friendsWithChallengesEntities)

        Resource.Success()
    } catch (e: Exception) {
        Resource.Error(e.toDomainException())
    }

    override suspend fun toggleFriendAsFavorite(friendId: String): Resource<Unit> {
        return try {
            firebaseFunctions.toggleFriendAsFavorite(friendId)
            Resource.Success()
        } catch (e: Exception) {
            Resource.Error(e.toDomainException())
        }
    }
}
