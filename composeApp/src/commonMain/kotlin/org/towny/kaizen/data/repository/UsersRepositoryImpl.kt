package org.towny.kaizen.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.towny.kaizen.data.local.LocalPreferencesDataSourceImpl
import org.towny.kaizen.data.remote.FirestoreDataSource
import org.towny.kaizen.data.repository.sources.LocalPreferencesDataSource
import org.towny.kaizen.data.repository.sources.RemoteDataSource
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.models.User
import org.towny.kaizen.domain.repository.UsersRepository

@OptIn(ExperimentalCoroutinesApi::class)
class UsersRepositoryImpl(
    private val remoteDataSource: RemoteDataSource = FirestoreDataSource(),
    private val localPreferencesDataSource: LocalPreferencesDataSource = LocalPreferencesDataSourceImpl()
) : UsersRepository {

    override val watchAll: Flow<Resource<List<User>>> = remoteDataSource.watchAllUsers()
        .flatMapLatest { userDTOs ->
            combine(
                userDTOs.map { userDTO ->
                    remoteDataSource.watchAllChallenges(userDTO.id).map { challengeDTOs ->
                        val challenges = challengeDTOs.map { it.toChallenge() }
                        userDTO.toUser(challenges = challenges)
                    }
                })
            { users ->
                Resource.Success(users.toList())
            }
        }

    override suspend fun getSavedUser(): Resource<User> {
        val user = localPreferencesDataSource.getSavedUser()
        return if (user != null) {
            Resource.Success(user)
        } else {
            Resource.Error(DomainException.NoSavedUserFound)
        }
    }
}