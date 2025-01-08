package org.towny.kaizen.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.towny.kaizen.data.remote.dto.UserDTO
import org.towny.kaizen.data.repository.sources.RemoteFirestoreDataSource
import org.towny.kaizen.domain.models.Resource
import org.towny.kaizen.domain.models.User
import org.towny.kaizen.domain.repository.AuthRepository
import org.towny.kaizen.domain.repository.UsersRepository

@OptIn(ExperimentalCoroutinesApi::class)
class UsersRepositoryImpl(
    private val remoteFirestoreDataSource: RemoteFirestoreDataSource,
    private val authRepository: AuthRepository
) : UsersRepository {

    private var currentUser: User? = null

    override fun watchMe(): Flow<Resource<User?>> {
        return remoteFirestoreDataSource
            .watchCurrentUser(authRepository.getUserSession()!!.uid)
            .flatMapLatest { userDTO ->
                remoteFirestoreDataSource.watchAllChallenges(authRepository.getUserSession()!!.uid)
                    .map { challengeDTOs ->
                        val challenges = challengeDTOs.map { it.toChallenge() }
                        userDTO?.toUser(challenges = challenges)
                    }
            }.catch { e ->
                Resource.Error<Resource<User?>>(e)
            }.map { user ->
                currentUser = user
                Resource.Success(user)
            }
    }

    override fun watchFriends(): Flow<Resource<List<User>>> {
        return remoteFirestoreDataSource
            .watchOtherUsers(authRepository.getUserSession()!!.uid)
            .flatMapLatest { userDTOs ->
                combine(
                    userDTOs.map { userDTO ->
                        remoteFirestoreDataSource.watchAllChallenges(userDTO.id)
                            .map { challengeDTOs ->
                                val challenges = challengeDTOs.map { it.toChallenge() }
                                userDTO.toUser(challenges = challenges)
                            }
                    })
                { userArray ->
                    Resource.Success(userArray.toList())
                }
            }.catch { e ->
                Resource.Error<Resource<List<User>>>(e)
            }
    }

    override suspend fun getCurrentUser(): User? {
        if (currentUser != null) {
            return currentUser
        }
        return watchMe().firstOrNull()?.data
    }

    override suspend fun createUser(user: User): Resource<Unit> {
        return remoteFirestoreDataSource.createUser(UserDTO.from(user))
    }
}
