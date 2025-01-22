package com.makapp.kaizen.data.repository

import dev.gitlive.firebase.firestore.FirebaseFirestoreException
import dev.gitlive.firebase.firestore.FirestoreExceptionCode
import dev.gitlive.firebase.firestore.code
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import com.makapp.kaizen.data.remote.dto.UserDTO
import com.makapp.kaizen.data.repository.sources.FirebaseFunctionsDataSource
import com.makapp.kaizen.data.repository.sources.FirestoreDataSource
import com.makapp.kaizen.data.toDomainException
import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.models.User
import com.makapp.kaizen.domain.repository.AuthRepository
import com.makapp.kaizen.domain.repository.UsersRepository

@OptIn(ExperimentalCoroutinesApi::class)
class UsersRepositoryImpl(
    private val firestore: FirestoreDataSource,
    private val firebaseFunctions: FirebaseFunctionsDataSource,
    private val authRepository: AuthRepository
) : UsersRepository {

    private var currentUser: User? = null

    override fun watchCurrentUser(): Flow<Resource<User?>> {
        val userId = authRepository.getUserSession()?.uid ?: return flowOf(
            Resource.Error(
                DomainException.User.NoUserSessionFound
            )
        )

        return firestore
            .watchCurrentUser(userId)
            .flatMapLatest { userDTO ->
                firestore.watchAllChallenges(userId)
                    .map { challengeDTOs ->
                        val challenges = challengeDTOs.map { it.toChallenge() }
                        userDTO?.toUser(challenges = challenges)
                    }
            }.map { user ->
                currentUser = user
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
            }
    }

    override suspend fun getCurrentUser(): User? {
        if (currentUser != null) {
            return currentUser
        }
        return watchCurrentUser().firstOrNull()?.data
    }

    override suspend fun createUser(user: User): Resource<Unit> = try {
        firestore.createUser(UserDTO.from(user))
        Resource.Success()
    } catch (e: Exception) {
        Resource.Error(e.toDomainException())
    }

    override suspend fun isUsernameAvailable(username: String): Resource<Boolean> {
        return try {
            val isAvailable = firebaseFunctions.isUsernameAvailable(username).isAvailable
            Resource.Success(isAvailable)
        } catch (e: Exception) {
            Resource.Error(e.toDomainException())
        }
    }

    override suspend fun deleteUserAccount(): Resource<Unit> = try {
        firebaseFunctions.deleteUserAccount()
        Resource.Success()
    } catch (e: Exception) {
        Resource.Error(e.toDomainException())
    }
}
