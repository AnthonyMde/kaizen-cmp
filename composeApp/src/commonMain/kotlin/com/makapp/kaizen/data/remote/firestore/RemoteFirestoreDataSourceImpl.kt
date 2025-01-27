package com.makapp.kaizen.data.remote.firestore

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.DocumentReference
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import com.makapp.kaizen.data.remote.dto.ChallengeFirestoreDTO
import com.makapp.kaizen.data.remote.dto.UserDTO
import com.makapp.kaizen.data.repository.entities.CreateChallengeRequest
import com.makapp.kaizen.data.repository.sources.FirestoreDataSource
import com.makapp.kaizen.domain.models.Challenge

class RemoteFirestoreDataSourceImpl : FirestoreDataSource {
    private val firestore = Firebase.firestore

    companion object {
        private const val USER_COLLECTION = "users"
        private const val CHALLENGE_COLLECTION = "challenges"
    }

    override fun watchCurrentUser(userId: String): Flow<UserDTO?> = firestore
        .collection(USER_COLLECTION)
        .document(userId)
        .snapshots
        .map { querySnapshot -> querySnapshot.data<UserDTO?>() }

    override suspend fun createUser(userDTO: UserDTO) {
        firestore
            .collection(USER_COLLECTION)
            .document(userDTO.id)
            .set(
                mapOf(
                    FirestoreUserKeys.ID to userDTO.id,
                    FirestoreUserKeys.EMAIL to userDTO.email,
                    FirestoreUserKeys.USERNAME to userDTO.name.lowercase(),
                    FirestoreUserKeys.DISPLAY_NAME to userDTO.displayName?.ifBlank { null },
                    FirestoreUserKeys.PROFILE_PICTURE_INDEX to userDTO.profilePictureIndex
                )
            )
    }

    override fun watchAllChallenges(userId: String): Flow<List<ChallengeFirestoreDTO>> = flow {
        getUserDocumentRef(userId)
            .collection(CHALLENGE_COLLECTION)
            .snapshots
            .catch { e ->
                println("DEBUG: (firestore) Cannot watch challenges because $e")
                emit(emptyList())
            }
            .collect { querySnapshot ->
                val challenges = querySnapshot.documents.map { documentSnapshot ->
                    documentSnapshot.data<ChallengeFirestoreDTO>()
                }
                emit(challenges)
            }
    }

    override suspend fun toggleChallengeStatus(
        userId: String,
        challengeId: String,
        isChecked: Boolean
    ) {
        getUserDocumentRef(userId)
            .collection(CHALLENGE_COLLECTION)
            .document(challengeId)
            .update(mapOf(FirestoreChallengeKeys.IS_DONE_FOR_TODAY to isChecked))
    }

    // TODO: do not create directly on firestore.
    override suspend fun createChallenge(request: CreateChallengeRequest) {
        val docRef = getUserDocumentRef(request.userId)
            .collection(CHALLENGE_COLLECTION)
            .add(
                mapOf(
                    FirestoreChallengeKeys.NAME to request.name,
                    FirestoreChallengeKeys.CREATED_AT to FieldValue.serverTimestamp,
                    FirestoreChallengeKeys.STATUS to Challenge.Status.ON_GOING.name,
                    FirestoreChallengeKeys.DAYS to 1,
                    FirestoreChallengeKeys.IS_DONE_FOR_TODAY to false,
                    FirestoreChallengeKeys.FAILURE_COUNT to 0,
                    FirestoreChallengeKeys.MAX_AUTHORIZED_FAILURES to request.maxFailures
                )
            )
        docRef.update(mapOf(FirestoreChallengeKeys.ID to docRef.id))
    }

    private suspend fun getUserDocumentRef(userId: String): DocumentReference = firestore
        .collection(USER_COLLECTION)
        .where { FirestoreUserKeys.ID equalTo userId }
        .get()
        .documents
        .first()
        .reference
}
