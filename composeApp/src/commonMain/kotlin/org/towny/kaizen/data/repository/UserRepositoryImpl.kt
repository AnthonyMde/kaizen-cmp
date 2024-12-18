package org.towny.kaizen.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.models.User
import org.towny.kaizen.domain.repository.UsersRepository

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryImpl(
    private val remoteDataSource: UsersRemoteDataSource
) : UsersRepository {

    override val watchAll: Flow<Resource<List<User>>> = remoteDataSource.watchAll()
        .flatMapLatest { userDTOs ->
            combine(
                userDTOs.map { userDTO ->
                    remoteDataSource.watchChallenges(userDTO.id).map { challengeDTOs ->
                        val challenges = challengeDTOs.map { it.toChallenge() }
                        userDTO.toUser(challenges = challenges)
                    }
                })
            { users ->
                Resource.Success(users.toList())
            }
        }
}