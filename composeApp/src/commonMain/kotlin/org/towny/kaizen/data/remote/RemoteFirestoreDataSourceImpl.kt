package org.towny.kaizen.data.remote

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.DocumentReference
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.towny.kaizen.data.remote.dto.ChallengeDTO
import org.towny.kaizen.data.remote.dto.UserDTO
import org.towny.kaizen.data.repository.CreateChallengeRequest
import org.towny.kaizen.data.repository.sources.RemoteFirestoreDataSource
import org.towny.kaizen.domain.models.Resource

class RemoteFirestoreDataSourceImpl : RemoteFirestoreDataSource {
    private val firestore = Firebase.firestore
    private var currentUserDocumentReference: DocumentReference? = null

    companion object {
        private const val USER_COLLECTION = "users"
        private const val CHALLENGE_COLLECTION = "challenges"
    }

    override fun watchAllUsers(): Flow<List<UserDTO>> = flow {
        try {
            firestore.collection(USER_COLLECTION).snapshots.collect { querySnapshot ->
                val users = querySnapshot.documents.map { documentSnapshot ->
                    documentSnapshot.data<UserDTO>()
                }
                emit(users)
            }
        } catch (e: Exception) {
            println("Cannot watch users because $e")
            emit(emptyList())
        }
    }

    override suspend fun createUser(userDTO: UserDTO): Resource<Unit> = try {
        firestore.collection(USER_COLLECTION).add(
            mapOf(
                FirestoreUserKeys.ID to userDTO.id,
                FirestoreUserKeys.NAME to userDTO.name,
                FirestoreUserKeys.PROFILE_PICTURE_INDEX to userDTO.profilePictureIndex
            )
        )

        Resource.Success()
    } catch (e: Exception) {
        println("Cannot create user because $e")
        throw e
    }

    override fun watchAllChallenges(userId: String): Flow<List<ChallengeDTO>> = flow {
        try {
            getCurrentUserDocReference(userId)
                .collection(CHALLENGE_COLLECTION)
                .snapshots
                .collect { querySnapshot ->
                    val challenges = querySnapshot.documents.map { documentSnapshot ->
                        documentSnapshot.data<ChallengeDTO>()
                    }
                    emit(challenges)
                }
        } catch (e: Exception) {
            println("Cannot watch challenges because $e")
            emit(emptyList())
        }
    }

    override suspend fun toggleChallenge(
        userId: String,
        challengeId: String,
        isChecked: Boolean
    ) {
        try {
            getCurrentUserDocReference(userId)
                .collection(CHALLENGE_COLLECTION)
                .document(challengeId)
                .update(mapOf(FirestoreChallengeKeys.IS_COMPLETED to isChecked))
        } catch (e: Exception) {
            println("Cannot toggle challenge's state because $e")
            throw e
        }
    }

    override suspend fun getUserBy(id: String): UserDTO? {
        return try {
            firestore.collection(USER_COLLECTION).get().documents.map { documentSnapshot ->
                documentSnapshot.data<UserDTO>()
            }.firstOrNull { user ->
                user.id == id
            }
        } catch (e: Exception) {
            println("Cannot get user by name because $e")
            throw e
        }
    }

    override suspend fun createChallenge(request: CreateChallengeRequest) {
        try {
            val docRef = getCurrentUserDocReference(request.userId)
                .collection(CHALLENGE_COLLECTION)
                .add(
                    mapOf(
                        FirestoreChallengeKeys.NAME to request.name,
                        FirestoreChallengeKeys.CREATED_AT to FieldValue.serverTimestamp,
                        FirestoreChallengeKeys.IS_COMPLETED to false,
                        FirestoreChallengeKeys.FAILURES to 0,
                        FirestoreChallengeKeys.MAX_FAILURES to request.maxFailures
                    )
                )
            docRef.update(mapOf(FirestoreChallengeKeys.ID to docRef.id))
        } catch (e: Exception) {
            println("Cannot create challenge because $e")
            throw e
        }
    }

    private suspend fun getCurrentUserDocReference(
        userId: String,
        refresh: Boolean = false
    ): DocumentReference {
        val ref = currentUserDocumentReference
        return if (ref != null && !refresh) {
            ref
        } else {
            val updatedRef = firestore.collection(USER_COLLECTION).where { "id" equalTo userId }
                .get().documents.first().reference
            currentUserDocumentReference = updatedRef
            updatedRef
        }
    }
}
