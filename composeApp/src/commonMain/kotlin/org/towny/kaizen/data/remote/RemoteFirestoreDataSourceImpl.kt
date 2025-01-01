package org.towny.kaizen.data.remote

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.towny.kaizen.data.remote.dto.ChallengeDTO
import org.towny.kaizen.data.remote.dto.UserDTO
import org.towny.kaizen.data.repository.CreateChallengeRequest
import org.towny.kaizen.data.repository.sources.RemoteFirestoreDataSource
import org.towny.kaizen.data.repository.sources.entity.FirestoreChallengeKeys

class RemoteFirestoreDataSourceImpl : RemoteFirestoreDataSource {
    private val firestore = Firebase.firestore

    companion object {
        private const val USER_COLLECTION = "users"
        private const val CHALLENGE_COLLECTION = "challenges"
    }

    override fun watchAllUsers(): Flow<List<UserDTO>> = flow {
        firestore
            .collection(USER_COLLECTION)
            .snapshots
            .collect { querySnapshot ->
                val users = querySnapshot.documents.map { documentSnapshot ->
                    documentSnapshot.data<UserDTO>()
                }
                emit(users)
            }
    }

    override fun watchAllChallenges(userId: String): Flow<List<ChallengeDTO>> = flow {
        firestore
            .collection(USER_COLLECTION)
            .document(userId)
            .collection(CHALLENGE_COLLECTION)
            .snapshots
            .collect { querySnapshot ->
                val challenges = querySnapshot.documents.map { documentSnapshot ->
                    documentSnapshot.data<ChallengeDTO>()
                }
                emit(challenges)
            }
    }

    override suspend fun toggleChallenge(
        userId: String,
        challengeId: String,
        isChecked: Boolean
    ) {
        firestore
            .collection(USER_COLLECTION)
            .document(userId)
            .collection(CHALLENGE_COLLECTION)
            .document(challengeId)
            .update(mapOf(FirestoreChallengeKeys.IS_COMPLETED to isChecked))
    }

    override suspend fun getUserBy(name: String): UserDTO? {
        return try {
            firestore
                .collection(USER_COLLECTION)
                .get()
                .documents
                .map { documentSnapshot ->
                    documentSnapshot.data<UserDTO>()
                }
                .firstOrNull { user ->
                    user.name == name
                }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun createChallenge(request: CreateChallengeRequest) {
        val docRef = firestore
            .collection(USER_COLLECTION)
            .document(request.userId)
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
    }
}
