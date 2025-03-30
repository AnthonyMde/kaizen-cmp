package com.makapp.kaizen.domain.repository

import kotlinx.coroutines.flow.StateFlow
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.models.user.UserSession

interface AuthRepository {
    suspend fun signUp(email: String, password: String): Resource<Unit>
    suspend fun signIn(email: String, password: String): Resource<Unit>
    suspend fun sendVerificationEmail()
    suspend fun logout()
    fun getUserSession(): UserSession?
    @Throws(Exception::class)
    suspend fun reloadUserSession(): UserSession?
    val watchUserSession: StateFlow<UserSession?>
}