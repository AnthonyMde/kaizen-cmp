package com.makapp.kaizen.data.repository

import com.makapp.kaizen.data.local.room.user.UserDao
import com.makapp.kaizen.data.local.room.user.toUserDTO
import com.makapp.kaizen.data.local.room.user.toUserEntity
import com.makapp.kaizen.data.remote.dto.CreateUserRequest
import com.makapp.kaizen.data.remote.dto.UserDTO
import com.makapp.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import com.makapp.kaizen.data.repository.sources.FirestoreDataSource
import com.makapp.kaizen.data.toDomainException
import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.models.User
import com.makapp.kaizen.domain.repository.AuthRepository
import com.makapp.kaizen.domain.repository.UsersRepository
import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import dev.gitlive.firebase.firestore.FirestoreExceptionCode
import dev.gitlive.firebase.firestore.code
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class UsersRepositoryImpl(
    private val firestore: FirestoreDataSource,
    private val firebaseFunctions: FirebaseFunctionsDataSource,
    private val authRepository: AuthRepository,
    private val userDao: UserDao,
    private val scope: CoroutineScope
) : UsersRepository {
    /**
     * This is the only part of the code which is using firebase realtime flow.
     * Other parts of the code are using synchronous http calls + room caching.
     */
    override fun watchCurrentUser(): Flow<Resource<User?>> = flow {
        emit(Resource.Loading())

        val userId = authRepository.getUserSession()?.uid ?: run {
            emit(Resource.Error(DomainException.User.NoUserSessionFound))
            return@flow
        }

        firestore
            .watchCurrentUser(userId)
            .flatMapLatest { userDTO ->
                firestore.watchAllChallenges(userId)
                    .map { challengeDTOs ->
                        scope.launch { saveUserToRoom(userDTO) }

                        val challenges = challengeDTOs.map { it.toChallenge() }
                        userDTO?.toUser(challenges = challenges)
                    }
            }.map { user ->
                @Suppress("UNCHECKED_CAST")
                Resource.Success(user) as Resource<User?> // Do we have a better way?
            }.catch { e ->
                println("DEBUG: (repository) Cannot watch me because $e")
                if (e is NoSuchElementException) {
                    emit(Resource.Error(DomainException.User.NoUserAccountFound))
                } else if (e is FirebaseFirestoreException && e.code == FirestoreExceptionCode.PERMISSION_DENIED) {
                    emit(Resource.Error(DomainException.User.NoUserAccountFound))
                } else {
                    emit(Resource.Error(e.toDomainException()))
                }
            }.collect(this)
    }

    private suspend fun saveUserToRoom(
        userDTO: UserDTO?,
    ) {
        if (userDTO == null) return

        userDao.refreshUser(userDTO.toUserEntity())
    }

    override suspend fun isUsernameAvailable(username: String): Resource<Boolean> {
        return try {
            val isAvailable = firebaseFunctions.isUsernameAvailable(username).isAvailable
            Resource.Success(isAvailable)
        } catch (e: Exception) {
            Resource.Error(e.toDomainException())
        }
    }

    override suspend fun getUser(): User? = userDao.getUser()?.toUserDTO()?.toUser()

    override suspend fun createUser(request: CreateUserRequest): Resource<Unit> = try {
        firebaseFunctions.createUserAccount(request)
        Resource.Success()
    } catch (e: Exception) {
        Resource.Error(e.toDomainException())
    }

    override suspend fun deleteUserAccount(): Resource<Unit> = try {
        firebaseFunctions.deleteUserAccount()
        Resource.Success()
    } catch (e: Exception) {
        Resource.Error(e.toDomainException())
    }
}
