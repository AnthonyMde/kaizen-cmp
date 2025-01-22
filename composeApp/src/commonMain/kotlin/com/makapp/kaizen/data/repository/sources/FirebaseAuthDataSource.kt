package com.makapp.kaizen.data.repository.sources

import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.auth.FirebaseUser

interface FirebaseAuthDataSource {
    suspend fun signUp(email: String, password: String): AuthResult
    suspend fun signIn(email: String, password: String): AuthResult
    suspend fun logout()
    fun getUserSession(): FirebaseUser?
}
