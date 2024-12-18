package org.towny.kaizen.data.remote

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.towny.kaizen.data.remote.dto.ChallengeDTO
import org.towny.kaizen.data.remote.dto.UserDTO
import org.towny.kaizen.data.repository.UsersRemoteDataSource

class UsersFirestoreDataSource : UsersRemoteDataSource {
    private val firestore = Firebase.firestore

    companion object {
        private const val USER_COLLECTION = "users"
        private const val CHALLENGE_COLLECTION = "challenges"
    }

    override fun watchAll(): Flow<List<UserDTO>> = flow {
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

    override fun watchChallenges(userId: String): Flow<List<ChallengeDTO>> = flow {
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

}