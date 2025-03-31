package com.makapp.kaizen.data.remote.firebase_auth

import com.makapp.kaizen.data.repository.sources.FirebaseAuthDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth

class FirebaseAuthDataSourceImpl: FirebaseAuthDataSource {
    private val auth = Firebase.auth

    override suspend fun signUp(email: String, password: String): AuthResult {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    override suspend fun signIn(email: String, password: String): AuthResult {
        return auth.signInWithEmailAndPassword(email, password)
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override fun getUserSession(): FirebaseUser? = auth.currentUser

    override suspend fun sendResetPasswordEmail(email: String) {
        auth.sendPasswordResetEmail(email)
    }
}
