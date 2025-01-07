package org.towny.kaizen.data.repository

import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthWeakPasswordException
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.towny.kaizen.data.remote.FirebaseAuth
import org.towny.kaizen.domain.exceptions.DomainException
import org.towny.kaizen.domain.models.UserSession
import org.towny.kaizen.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    private val _watchUserSession =
        MutableStateFlow(firebaseAuth.getUserSession()?.toUserSession())
    override val watchUserSession = _watchUserSession.asStateFlow()

    override suspend fun signUp(email: String, password: String) {
        try {
            val authResult = firebaseAuth.signUp(email, password)
            _watchUserSession.tryEmit(authResult.user?.toUserSession())
        } catch (e: Exception) {
            if (e is FirebaseAuthWeakPasswordException) {
                throw DomainException.Auth.WeakPassword
            } else {
                throw e
            }
        }
    }

    override suspend fun sendEmailVerification() {
        firebaseAuth.getUserSession()?.sendEmailVerification()
    }

    override suspend fun signIn(email: String, password: String) {
        try {
            val authResult = firebaseAuth.signIn(email, password)
            _watchUserSession.tryEmit(authResult.user?.toUserSession())
        } catch (e: Exception) {
            if (e is FirebaseAuthInvalidCredentialsException) {
                throw DomainException.Auth.InvalidCredentials
            } else {
                throw e
            }
        }
    }

    override suspend fun logout() {
        firebaseAuth.logout()
        _watchUserSession.update { null }
    }

    override fun getUserSession(): UserSession? {
        return _watchUserSession.value
    }

    /**
     * Throws a firebase exception while trying to invoke reload() if the
     * user session has been deleted from firebase auth console.
     */
    @Throws(FirebaseException::class, IllegalStateException::class)
    override suspend fun reloadUserSession(): UserSession? {
        firebaseAuth.getUserSession()?.reload()

        _watchUserSession.update {
            firebaseAuth.getUserSession()?.toUserSession()
        }

        return _watchUserSession.value
    }

    private fun FirebaseUser.toUserSession(): UserSession? {
        val email = email ?: return null

        return UserSession(
            uid = uid,
            email = email,
            isEmailVerified = isEmailVerified
        )
    }
}
