package com.makapp.kaizen.data.repository

import com.makapp.kaizen.data.local.room.app.AppDao
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException
import dev.gitlive.firebase.auth.FirebaseAuthWeakPasswordException
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.makapp.kaizen.data.repository.sources.FirebaseAuthDataSource
import com.makapp.kaizen.data.toDomainException
import com.makapp.kaizen.domain.exceptions.DomainException
import com.makapp.kaizen.domain.models.Resource
import com.makapp.kaizen.domain.models.user.UserSession
import com.makapp.kaizen.domain.repository.AuthRepository
import dev.gitlive.firebase.FirebaseNetworkException

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuthDataSource,
    private val appDao: AppDao
) : AuthRepository {
    private val _watchUserSession =
        MutableStateFlow(firebaseAuth.getUserSession()?.toUserSession())
    override val watchUserSession = _watchUserSession.asStateFlow()

    override suspend fun signUp(email: String, password: String): Resource<Unit> = try {
        val authResult = firebaseAuth.signUp(email, password)
        _watchUserSession.tryEmit(authResult.user?.toUserSession())
        Resource.Success()
    } catch (e: Exception) {
        when (e) {
            is FirebaseAuthWeakPasswordException -> Resource.Error(DomainException.Auth.WeakPassword)
            is FirebaseAuthUserCollisionException -> {
                Resource.Error(DomainException.Auth.EmailAddressAlreadyUsed)
            }

            else -> Resource.Error(e.toDomainException())
        }
    }

    override suspend fun sendVerificationEmail() {
        firebaseAuth.getUserSession()?.sendEmailVerification()
    }

    override suspend fun signIn(email: String, password: String): Resource<Unit> = try {
        val authResult = firebaseAuth.signIn(email, password)
        _watchUserSession.tryEmit(authResult.user?.toUserSession())
        Resource.Success()
    } catch (e: Exception) {
        when (e) {
            is FirebaseAuthInvalidCredentialsException -> {
                Resource.Error(DomainException.Auth.InvalidCredentials)
            }

            else -> Resource.Error(e.toDomainException())
        }
    }


    override suspend fun logout() {
        appDao.deleteNetworkCache()
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
    @Throws(Exception::class)
    override suspend fun reloadUserSession(): UserSession? {
        try {
            firebaseAuth.getUserSession()?.reload()
        } catch (e: Exception) {
            if (e is FirebaseNetworkException) {
                throw DomainException.Common.NoNetworkError
            } else {
                throw e
            }
        }

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

    override suspend fun sendResetPasswordEmail(email: String): Resource<Unit> {
        return try {
            firebaseAuth.sendResetPasswordEmail(email)
            Resource.Success()
        } catch (e: Exception) {
            Resource.Error(e.toDomainException())
        }
    }
}
