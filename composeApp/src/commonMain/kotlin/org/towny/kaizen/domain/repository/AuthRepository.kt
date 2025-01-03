package org.towny.kaizen.domain.repository

import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUp(email: String, password: String)
    suspend fun signIn(email: String, password: String)
    suspend fun sendEmailVerification(user: FirebaseUser)
    suspend fun logout()
    fun watchUserSession(): Flow<FirebaseUser?>
    suspend fun getUserSession(): FirebaseUser?
}