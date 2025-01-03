package org.towny.kaizen.data.remote

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth

class FirebaseAuth {
    private val auth = Firebase.auth

    suspend fun signUp(email: String, password: String): AuthResult {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
    }

    suspend fun logout() {
        auth.signOut()
    }

    fun getUserSession(): FirebaseUser? = auth.currentUser
}