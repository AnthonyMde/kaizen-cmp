package org.towny.kaizen.data.remote.firebase_auth

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import org.towny.kaizen.data.repository.sources.FirebaseAuthDataSource

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
}
