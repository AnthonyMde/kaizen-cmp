package com.makapp.kaizen.data.remote.firestore

import com.makapp.kaizen.data.remote.dto.ChallengeFirestoreDTO
import com.makapp.kaizen.data.remote.dto.UserDTO
import com.makapp.kaizen.data.repository.sources.FirestoreDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.DocumentReference
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

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

    private suspend fun getUserDocumentRef(userId: String): DocumentReference = firestore
        .collection(USER_COLLECTION)
        .where { FirestoreUserKeys.ID equalTo userId }
        .get()
        .documents
        .first()
        .reference
}
