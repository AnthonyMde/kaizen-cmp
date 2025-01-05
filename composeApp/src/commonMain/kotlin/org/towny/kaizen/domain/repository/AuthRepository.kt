package org.towny.kaizen.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.towny.kaizen.domain.models.UserSession

interface AuthRepository {
    suspend fun signUp(email: String, password: String)
    suspend fun signIn(email: String, password: String)
    suspend fun sendEmailVerification()
    suspend fun logout()
    suspend fun getUserSession(): UserSession?
    suspend fun reloadUserSession(): UserSession?
    val watchUserSession: StateFlow<UserSession?>
}