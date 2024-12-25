package org.towny.kaizen.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.towny.kaizen.data.repository.sources.LocalPreferencesDataSource
import org.towny.kaizen.data.repository.sources.RemoteFirestoreDataSource
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.models.User
import org.towny.kaizen.domain.repository.UsersRepository

@OptIn(ExperimentalCoroutinesApi::class)
class UsersRepositoryImpl(
    private val remoteFirestoreDataSource: RemoteFirestoreDataSource,
    private val localPreferencesDataSource: LocalPreferencesDataSource
) : UsersRepository {

    override val watchAll: Flow<Resource<List<User>>> = remoteFirestoreDataSource.watchAllUsers()
        .flatMapLatest { userDTOs ->
            combine(
                userDTOs.map { userDTO ->
                    remoteFirestoreDataSource.watchAllChallenges(userDTO.id).map { challengeDTOs ->
                        val challenges = challengeDTOs.map { it.toChallenge() }
                        userDTO.toUser(challenges = challenges)
                    }
                })
            { users ->
                Resource.Success(users.toList())
            }
        }

    override fun getSavedUsername(): Flow<Resource<String?>> {
        return localPreferencesDataSource.getSavedUsername().map { name ->
            Resource.Success<String?>(name)
        }.catch<Resource<String?>> { throwable ->
            emit(Resource.Error(throwable))
        }
    }
}