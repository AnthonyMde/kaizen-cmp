package org.towny.kaizen.data.repository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.towny.kaizen.domain.entities.User
import org.towny.kaizen.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {
    private val firestore = Firebase.firestore

    companion object {
        private const val USER_COLLECTION = "users"
    }

    override fun getUsers(): Flow<List<User>> = flow {
        firestore.collection(USER_COLLECTION).snapshots.collect { querySnapshot ->
            val users = querySnapshot.documents.map { documentSnapshot ->
                documentSnapshot.data<User>()
            }
            emit(users)
        }
    }
}