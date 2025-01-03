package org.towny.kaizen.data.repository

import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthWeakPasswordException
import dev.gitlive.firebase.auth.FirebaseUser
import org.towny.kaizen.data.remote.FirebaseAuth
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override suspend fun signUp(email: String, password: String) {
        try {
            val authResult = firebaseAuth.signUp(email, password)
            authResult.user?.sendEmailVerification()
        } catch (e: Exception) {
            if (e is FirebaseAuthWeakPasswordException) {
                throw DomainException.Auth.WeakPassword
            } else {
                throw e
            }
        }
    }

    override suspend fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification()
    }

    override suspend fun signIn(email: String, password: String) {
        try {
            firebaseAuth.signIn(email, password)
        } catch (e: Exception) {
            if (e is FirebaseAuthInvalidCredentialsException) {
                throw DomainException.Auth.InvalidCredentials
            } else {
                throw e
            }
        }
    }

    override suspend fun logout() {
        return firebaseAuth.logout()
    }

    override suspend fun getUserSession(): FirebaseUser? {
        return firebaseAuth.getUserSession()
    }

//    override fun login(email: String, password: String): Flow<Resource<Unit>> = flow {
//        emit(Resource.Loading())
//        val user = try {
//            remoteFirestoreDataSource.getUserBy(username)
//        } catch (e: Exception) {
//            emit(Resource.Error(e))
//            return@flow
//        }
//        if (user != null) {
//            preferencesDataSource.saveUserId(user.id)
//            emit(Resource.Success())
//        } else {
//            emit(Resource.Error(DomainException.Login.UserNotAuthorized))
//        }
//    }
}
