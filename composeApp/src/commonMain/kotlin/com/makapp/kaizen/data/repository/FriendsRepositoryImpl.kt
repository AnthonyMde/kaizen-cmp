package com.makapp.kaizen.data.repository

import com.makapp.kaizen.data.local.room.challenges.ChallengeEntity
import com.makapp.kaizen.data.local.room.friends.FriendEntity
import com.makapp.kaizen.data.local.room.friends.FriendWithChallengesEntity
import com.makapp.kaizen.data.local.room.friends.FriendsDao
import com.makapp.kaizen.data.remote.dto.ChallengeDTO
import com.makapp.kaizen.data.remote.dto.FriendDTO
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
        return friendsDao.watchAll().map { friendsWithChallenges ->
            val friendsDTO = friendsWithChallenges.map { it.toFriendDTO() }
            val friends = friendsDTO.map { it.toFriend() }
            Resource.Success(friends)
        }.catch { 
            Resource.Error<List<Friend>>(it.toDomainException())
        }
    }

    override suspend fun refreshFriends(): Resource<Unit> = try {
        val friendDTOs = firebaseFunctions.getFriends(includeChallenges = true)
        val friendsWithChallengesEntities = friendDTOs.map { it.toFriendWithChallengesEntity(it.challenges) }
        friendsDao.refresh(friendsWithChallengesEntities)
        Resource.Success()
    } catch (e: Exception) {
        Resource.Error(e.toDomainException())
    }
    
    private fun FriendWithChallengesEntity.toFriendDTO(): FriendDTO {
        return FriendDTO(
            id = friend.id,
            name = friend.name,
            profilePictureIndex = friend.profilePictureIndex,
            challenges = challenges.map { it.toChallengeDTO() }
        )
    }
    
    private fun ChallengeEntity.toChallengeDTO(): ChallengeDTO {
        return ChallengeDTO(
            id = id,
            name = name,
            status = status,
            createdAt = createdAt,
            days = days,
            isDoneForToday = isDoneForToday,
            failureCount = failureCount,
            maxAuthorizedFailures = maxAuthorizedFailures
        )
    }
    
    private fun FriendDTO.toFriendWithChallengesEntity(challenges: List<ChallengeDTO>): FriendWithChallengesEntity {
        return FriendWithChallengesEntity(
            friend = FriendEntity(
                id = id,
                name = name,
                profilePictureIndex = profilePictureIndex
            ),
            challenges = challenges.map { it.toChallengeEntity(userId = id) }
        )
    }
    
    private fun ChallengeDTO.toChallengeEntity(userId: String): ChallengeEntity {
        return ChallengeEntity(
            id = id,
            userId = userId,
            name = name,
            status = status,
            createdAt = createdAt,
            days = days,
            isDoneForToday = isDoneForToday,
            failureCount = failureCount,
            maxAuthorizedFailures = maxAuthorizedFailures
        )
    }
}
